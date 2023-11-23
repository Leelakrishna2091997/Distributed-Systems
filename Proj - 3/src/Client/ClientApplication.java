package Client;

import Log.LoggerUtil;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import Shared.ServerParticipant;
import javax.management.InvalidAttributeValueException;

public class ClientApplication {

  private static final Logger log = Logger.getLogger(ClientApplication.class.getName());
  private static ServerParticipant remoteMethods;

  public ClientApplication(Integer serverPort) throws NotBoundException, MalformedURLException, RemoteException {
    try {
      // Getting the registry for a server
      Registry registry = LocateRegistry.getRegistry("localhost", serverPort);

      // Looking up the registry for the remote object
      this.remoteMethods = (ServerParticipant) registry.lookup("Participant");
      LoggerUtil.initLogger(log, "src/Client/ClientLog.log");

      // Starting the transaction
      //      remoteMethods.printString("This is a string transaction");
    } catch (Exception e) {
      System.err.println("Client exception: " + e.toString());
      e.printStackTrace();
    }
  }

  public static void uiInitialize() throws IOException, InterruptedException {
    seedData();
    startInteractiveMode();
  }
  public static void threadInitialize() throws IOException, InterruptedException {
    seedData();
    executeConcurrentRequests();
  }

  private static void seedData() throws IOException, InterruptedException {
    String[] initialCommands = {
            "PUT a 1", "PUT b 2", "PUT c 3",
            "GET a", "GET y", "GET z","GET t",
            "DELETE p", "DELETE a"
    };

    for (String command : initialCommands) {
      executeCommand(command);
    }
  }

  private static void executeCommand(String command) {
    // Split the command string into parts. A real implementation might need more error checking.
    String[] parts = command.split(" ");
    if(parts.length == 0) {
      System.out.println("Server not able to connect");
      return;
    }
    try {
      String operation = parts[0];
      String key = parts[1];
      String value = (parts.length > 2) ? parts[2] : null;
      long timestamp = System.currentTimeMillis();

      switch (operation.toUpperCase()) {
        case "PUT":
          if (key == "" || value == ""){
            log.log(Level.SEVERE,"Server: Enter valid key value pair" + key + " Value " + value );
            String msg = "Server: invalid input" + key + value;
            logResult("SEVERE",msg);

          }
          else {
            Boolean res = remoteMethods.putKey(key, Integer.parseInt(value),  String.valueOf(timestamp));
            if (res == null) {
              log.log(Level.SEVERE,"Server: Critical section hit");
            } else if (res == false) {
              log.log(Level.SEVERE,"Key overlap" +  String.valueOf(timestamp));
            } else {
              log.log(Level.INFO,"Insertion of Key Value Success and replicated across all servers "
                  + "- Key " + key + " Packet id " +  String.valueOf(timestamp));
            }
          }
          break;
          case "GET":
          Integer keyValue = remoteMethods.getKey(key, String.valueOf(timestamp));
          if(keyValue == null) {
            log.log(Level.SEVERE,"Server: Critical section hit");
          }
          else if(keyValue == 0){
            log.log(Level.SEVERE,"Get failed not able to identify" + key );
          }
          else {
            log.log(Level.INFO,"Server: Get Successful " + key + " Value: " + keyValue);
          }
          break;
        case "DELETE":
          Boolean deleteRes = remoteMethods.deleteKey(key, String.valueOf(timestamp));
          if(deleteRes == null) {
            log.log(Level.SEVERE,"Server: Critical section hit");
          } else if(deleteRes == false) {
            log.log(Level.SEVERE,"Deletion failed" + key);
          } else {
            log.log(Level.INFO,"Deletion successful and data is consistently maintained across "
                + "all the servers." + key);
          }
          break;
        default:
          log.log(Level.WARNING, "Unknown command: " + command);
          break;
      }
    } catch (Exception e) {
      log.log(Level.SEVERE, "Error executing command: " + command, e);
    }
  }

  private static void logResult(String level, String msg) {
    Integer t = 0;

    switch (level){
      case "SEVERE":
        log.log(Level.SEVERE, msg);
      case "INFO":
        log.log(Level.INFO, msg);
      case "WARNING":
        log.log(Level.WARNING,msg);
    }
  }

  private static void executeConcurrentRequests() {
    // Multiple threads will be created.
    ExecutorService executor = Executors.newFixedThreadPool(3);
    Runnable taskOne = () -> {
      executeCommand("PUT a 4");
      executeCommand("PUT b 3");
      executeCommand("PUT z 3");
    };

    Runnable taskTwo = () -> {
      executeCommand("GET a");
      executeCommand("GET b");
      executeCommand("GET c");
      executeCommand("GET d");
    };


    Runnable taskThree = () -> {
      executeCommand("GET a");
      executeCommand("GET c");
      executeCommand("GET d");
    };

    executor.submit(taskOne);
    executor.submit(taskTwo);
    executor.submit(taskThree);
    executor.shutdown();
  }

  private static void startInteractiveMode() throws IOException {
    Scanner scanner = new Scanner(System.in);
    while (true) {
      System.out.println("Provide data PUT / GET / DELETE):");
      String userCommand = scanner.nextLine();
      if (userCommand.isEmpty()) {
        log.log(Level.WARNING, "Invalid input");
        continue;
      }
      executeCommand(userCommand);
    }
  }

  public static void main(String[] args) {



    try {
      System.out.println("Enter RMI server port you wanted to connect: among ports given");
      Scanner scanner = new Scanner(System.in);
      String inputValue = scanner.nextLine();
        new ClientApplication(Integer.parseInt(inputValue));
      while(true) {
        System.out.println("Enter UI mode: ui or thread or exit.");
        scanner = new Scanner(System.in);
        String userCommand = scanner.nextLine();
        if(userCommand.equals("exit")) {
          break;
        } else if(userCommand.equals("ui")) {
          uiInitialize();
        } else if(userCommand.equals("thread")) {
          threadInitialize();
        } else {
          System.out.println("Re enter input invalid");
        }
      }
    } catch (Exception e) {
      log.log(Level.SEVERE, "Error: ", e);
    }
  }
}
