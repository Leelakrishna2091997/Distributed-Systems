package Server;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import Shared.ServerParticipant;

public class ServerParticipantImpl extends UnicastRemoteObject implements ServerParticipant {


  private String transaction;
  private Coordinator coordinator;

  private Map<String, Integer> keyValueStore = new HashMap<String, Integer>();
  private static Map<String, Boolean> raceSection = new HashMap<>();
  private static final Logger log = Logger.getLogger(ServerApplication.class.getName());
  private Object lock = new Object();
  private boolean isProcessing = false;

  protected ServerParticipantImpl(String coordinatorHost, Integer coordinatorPort) throws RemoteException {
    super();
    try {
      Registry registry = LocateRegistry.getRegistry(coordinatorHost, coordinatorPort);
      coordinator = (Coordinator) registry.lookup("Coordinator");
      System.out.println("Connected to Coordinator");
    } catch (Exception e) {
      throw new RemoteException("Unable to connect to coordinator", e);
    }
  }

  @Override
  public Integer getKey(String key, String packetId) {
    if(raceSection.containsKey(key) &&  raceSection.get(key)) {
      log.log(Level.INFO,"Critical section conflict");
      return null;
    } else {
      if (keyValueStore.containsKey(key)) {
        int keyValue = keyValueStore.get(key);
        log.log(Level.INFO,"Server: Finding Key" + key);
        log.log(Level.INFO,"Server: Key Found" + "Key: " + key + " Value: " + keyValue);
        return keyValue;
      } else {
        String msg = "Invalid " + "Key " + key + " #" + packetId;
        System.out.println(msg);
        log.log(Level.SEVERE,msg);
        return 0;
      }
    }
  }

  @Override
  public Boolean deleteKey(String key, String packetId) throws InterruptedException, RemoteException {

      if (raceSection.containsKey(key) && raceSection.get(key)) {
        log.log(Level.INFO, "Critical section conflict");
        return null;
      } else {
        synchronized (lock) {
          // Initiating 2Phase commit transaction in the distributed environment.

          isProcessing = true;
        if (keyValueStore.containsKey(key)) {
          return this.printString("DELETE "+key+ " "+ packetId);
//          return true;
        } else {
          String msg = "Invalid Key " + "Key " + key + " #" + packetId;
          System.out.println(msg);
          log.log(Level.SEVERE, msg);
          isProcessing = false;
          return false;
        }
      }
    }
  }

  @Override
  public Boolean putKey(String key, int value, String packetId)
      throws InterruptedException, RemoteException {

      if (raceSection.containsKey(key) && raceSection.get(key)) {
        log.log(Level.INFO, "Critical section conflict");
        return null;
      } else {
        synchronized (lock) {
          // If 2Phase commit is successful then.
          return this.printString("INSERT "+key+" "+value+ " "+packetId);

//        return true;
      }
    }
  }

  private void insertKey(String key, int value) throws InterruptedException {
    isProcessing = true;
    raceSection.put(key, true);
    log.log(Level.INFO, "Server: Inserting Key - " + key + " Value - " + value);
    Thread.sleep(1000);
    keyValueStore.put(key, value);
    log.log(Level.INFO, "Server: Insertion Successful");
    raceSection.put(key, false);
    isProcessing = false;
  }

  private void removeKey(String key) throws InterruptedException {
    raceSection.put(key, true);
    log.log(Level.INFO, "Server: Deletion " + key);
    Thread.sleep(1000);
    keyValueStore.remove(key);
    log.log(Level.INFO, "Delete Successful");
    raceSection.put(key, false);
    isProcessing = false;
  }


  public boolean prepare(String transaction) throws RemoteException {
    this.transaction = transaction;
    if(isProcessing){
      return false; // If a write transaction is in progress then the vote to abort.
    }
    return true;  // vote to commit.
  }

  public String commit() throws RemoteException {
    // Committing the transaction provided by the coordinator.
    this.executingCommandOnParticipant(this.transaction);
    return "Committing: " + this.transaction;
  }



  private void executingCommandOnParticipant(String command) {
    // Split the command string into parts. A real implementation might need more error checking.
    String[] parts = command.split(" ");
    try {
      if(parts.length == 3 && parts[0].equals("DELETE")) {
        String key  = parts[1];
        removeKey(key);
      }
      else if(parts.length == 4 && parts[0].equals("INSERT")) {
        String key  = parts[1];
        int value  = Integer.parseInt(parts[2]);
        insertKey(key, value);
      }
      else {
        log.log(Level.SEVERE, "Invalid command provided by coordinator: " + command);
        throw new Exception("Invalid command provided by coordinator.");
      }
    } catch (Exception e) {
      log.log(Level.SEVERE, "Error executing command: " + command, e);
    }
  }
  // Populate transactions to the peer participants by initiating 2Phase commit to coordinator.
  public boolean printString(String transaction) throws RemoteException {
    this.transaction = transaction;
    System.out.println("String to print: " + this.transaction);
    return coordinator.prepareTransaction(transaction);
  }

  public Map<String, Integer> getStoreDetails() {
    return this.keyValueStore;
  }

}
