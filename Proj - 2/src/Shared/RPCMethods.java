package Shared;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RPCMethods extends Remote {
  Integer getKey(String key, String packetId) throws RemoteException, IOException;
  Boolean putKey(String key, int value, String packetId) throws RemoteException, IOException, InterruptedException;
  Boolean deleteKey(String key, String packetId) throws RemoteException, IOException, InterruptedException;
}

