package Shared;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ServerParticipant extends Remote {
  boolean printString(String transaction) throws RemoteException;
  boolean prepare(String transaction) throws RemoteException;

  Map<String, Integer> getStoreDetails() throws RemoteException;
  String commit() throws RemoteException;
  Integer getKey(String key, String packetId) throws RemoteException, IOException;
  Boolean putKey(String key, int value, String packetId) throws RemoteException, IOException, InterruptedException;
  Boolean deleteKey(String key, String packetId) throws RemoteException, IOException, InterruptedException;
}

