package Server;

import Shared.ServerParticipant;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoordinatorImpl extends UnicastRemoteObject implements Coordinator {

  private List<ServerParticipant> participants;
  private List<String> hostNames;
  private List<Integer> hostPorts;
  private static final Logger log = Logger.getLogger(ServerApplication.class.getName());

  public CoordinatorImpl(List<String> participantHosts, List<Integer> participantPorts) throws RemoteException {
    super();
    this.hostNames = participantHosts;
    this.hostPorts = participantPorts;
  }

  public void connectParticipants() throws RemoteException {
    participants = new ArrayList<>();
    for (int i = 0; i < this.hostPorts.size(); i++) {
      try {
        Registry registry = LocateRegistry.getRegistry(hostNames.get(i), hostPorts.get(i));
        ServerParticipant participant = (ServerParticipant) registry.lookup("Participant");
        participants.add(participant);
      } catch (Exception e) {
        throw new RemoteException("Unable to connect to participant", e);
      }
    }
  }
  public boolean prepareTransaction(String transaction) throws RemoteException {
    for (ServerParticipant participant : participants) {
      if (!participant.prepare(transaction)) {
        // One of the participant has aborted the transaction with a abort vote.
        return false;
      }
    }
    this.showAllStores();
    for (ServerParticipant participant : participants) {
      participant.commit();
    }
    this.showAllStores();
    return true;
  }
  private void showAllStores() throws RemoteException {
    for(ServerParticipant each: participants) {
      System.out.println(each.getStoreDetails());
      log.log(Level.INFO,"Each Server Data" + each.getStoreDetails());
    }
  }
}

