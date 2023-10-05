package Server;

import Log.LoggerUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Java class representing a server application for handling client requests.
 */
public class ServerApplication {
  // Logger for server application logging.
  private static final Logger log = Logger.getLogger(ServerApplication.class.getName());
  // A map to store key-value pairs.
  private static Map<String, Integer> store = new HashMap<String, Integer>();
  // A reference to the network gateway interface for communication.
  private static ServerInterface networkGateWay = null;

  /**
   * Default constructor for the ServerApplication class.
   */
  ServerApplication() {
    store.put("a", 1);
    store.put("b", 1);
    store.put("c", 1);
    store.put("d", 1);
    store.put("e", 1);
  }

  // Method to handle invalid commands and send error messages to the client.
  private static void invalidCommandMode(String message) throws IOException {
    System.out.println(message);
    log.log(Level.SEVERE, message);
    networkGateWay.send(message);
  }

  // Method to log exceptions and send error messages to the client.
  private static void logException(Exception e, String message) throws IOException {
    log.log(Level.SEVERE, message);
    networkGateWay.send(message);
  }

  // Method to log successful operation messages.
  private static void logMessage(String msg) {
    serverLog("Successfully performed operation. " + msg);
  }

  // Method to log messages in the server's log.
  private static void serverLog(String msg) {
    log.log(Level.INFO, msg);
  }

  /**
   * Main method to start the server and handle client requests.
   *
   * @param args Command line arguments. Expects the server port as an argument.
   * @throws IOException If there is an issue with network communication.
   */
  public static void main(String args[]) throws IOException {
    LoggerUtil.initLogger(log, "src/Server/ServerLog.log");
    ServerApplication serverApp = new ServerApplication();

    if (args.length == 1) {
      int port = Integer.valueOf(args[0]);
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter protocol mode of the server.");
      String mode = sc.nextLine();
      String inetAddress = "";

      if (mode.equals("TCP")) {
        networkGateWay = new SocketTCPServer();
        networkGateWay.startServer(port);
      } else if (mode.equals("UDP")) {
        networkGateWay = new SocketUDPServer();
        networkGateWay.startServer(port);
      }

      while (true) {
        if (mode.equals("TCP") || mode.equals("UDP")) {
          String receivedMsg = networkGateWay.receive();

          if (receivedMsg != null) {
            String[] commands = receivedMsg.split(" ");
            inetAddress = networkGateWay.getClientIp();

            if (commands.length == 3 || commands.length == 4) {
              if (commands[0].equals("PUT") && commands.length == 4) {
                try {
                  log.log(Level.INFO, "Performing put operation" + " InetAddress: " + inetAddress + " port: " + port);
                  store.put(commands[1], Integer.valueOf(commands[2]));
                  String logDataMessage = "Put operation success";
                  logMessage(logDataMessage + " packet_id: " + commands[3] + " InetAddress: " + inetAddress + " port: " + port);
                  networkGateWay.send(logDataMessage + " " + String.valueOf(commands[3]));
                } catch (Exception e) {
                  logException(e, "Put operation terminated with exception, packed_id: " +
                      String.valueOf(commands[3]));
                }
              } else if (commands[0].equals("GET") && commands.length == 3) {
                try {
                  log.log(Level.INFO, "Performing get operation" + " InetAddress: " + inetAddress + " port: " + port);
                  int keyValue = store.get(commands[1]);
                  String logDataMessage = "Get operation success";
                  logMessage(logDataMessage + " packet_id: " + commands[2] + " InetAddress: " + inetAddress + " port: " + port);
                  networkGateWay.send(String.valueOf(keyValue) + " " + String.valueOf(commands[2]));
                } catch (Exception e) {
                  logException(e, "Get operation terminated with exception, packed_id: " +
                      String.valueOf(commands[2]));
                }
              } else if (commands[0].equals("DELETE") && commands.length == 3) {
                try {
                  if (store.containsKey(commands[1])) {
                    log.log(Level.INFO, "Performing delete operation" + " InetAddress: " + inetAddress + " port: " + port);
                    store.remove(commands[1]);
                    String logDataMessage = "Delete operation success";
                    logMessage(logDataMessage + " packet_id: " + commands[2] + " InetAddress: " + inetAddress + " port: " + port);
                    networkGateWay.send(String.valueOf(logDataMessage) + " " + String.valueOf(commands[2]));
                  } else {
                    invalidCommandMode("Invalid operation provided by user. #" + receivedMsg.split("#")[1]);
                  }
                } catch (Exception e) {
                  logException(e, "Delete operation terminated with exception, packed_id: " +
                      String.valueOf(commands[2]));
                }
              } else {
                invalidCommandMode("Invalid operation provided by user. #" + receivedMsg.split("#")[1]);
              }
            } else {
              invalidCommandMode("Invalid operation provided by user. #" + receivedMsg.split("#")[1]);
            }
          }
        } else {
          serverLog("Invalid protocol Mode");
        }
      }
    } else {
      System.out.println("Server not started.");
    }
  }
}
