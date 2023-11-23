package Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import Shared.RPCMethods;

public class RPCFunctions extends UnicastRemoteObject implements RPCMethods{

  private static Map<String, Integer> keyValueStore = new HashMap<String, Integer>();
  private static Map<String, Boolean> raceSection = new HashMap<>();
  private static final Logger log = Logger.getLogger(ServerApplication.class.getName());
  protected RPCFunctions() throws RemoteException {
    super();
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
  public Boolean deleteKey(String key, String packetId) throws InterruptedException {
    if(raceSection.containsKey(key) &&  raceSection.get(key)) {
      log.log(Level.INFO,"Critical section conflict");
      return null;
    } else {
      if (keyValueStore.containsKey(key)) {
        raceSection.put(key, true);
        log.log(Level.INFO,"Server: Deletion " + key);
        Thread.sleep(3000);
        keyValueStore.remove(key);
        log.log(Level.INFO,"Delete Successful");
        raceSection.put(key, false);
        return true;
      } else {
        String msg = "Invalid Key " + "Key " + key + " #" + packetId;
        System.out.println(msg);
        log.log(Level.SEVERE,msg);
        return false;
      }
    }
  }

  @Override
  public Boolean putKey(String key, int value, String packetId) throws InterruptedException {
    if(raceSection.containsKey(key) &&  raceSection.get(key)) {
      log.log(Level.INFO,"Critical section conflict");
      return null;
    } else
    {
      raceSection.put(key, true);
      log.log(Level.INFO,"Server: Inserting Key - " + key + " Value - " + value);
      Thread.sleep(3000);
      keyValueStore.put(key, value);
      log.log(Level.INFO,"Server: Insertion Successful");
      raceSection.put(key, false);
      return true;
    }
  }
}
