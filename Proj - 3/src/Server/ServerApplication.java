package Server;

import Log.LoggerUtil;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.logging.Logger;
import Shared.ServerParticipant;
import java.util.stream.Collectors;
import javax.management.InvalidAttributeValueException;

public class ServerApplication {
  private static final Logger log = Logger.getLogger(ServerApplication.class.getName());

  public static void main(String[] args) {
    int[] portArray = new int[5];
    try {
      if(args.length == 5) {
        int index = 0;
        for(String each: args) {
          portArray[index] = Integer.parseInt(each);
          index+=1;
        }
      }
      else {
        throw new InvalidAttributeValueException("5 server ports numbers are expected to connect");
      }
      // Start the coordinator
      Coordinator coordinator = new CoordinatorImpl(
          Arrays.asList("localhost", "localhost", "localhost", "localhost", "localhost"),
          Arrays.stream(portArray).boxed().collect(Collectors.toList())
      );
      Registry coordinatorRegistry = LocateRegistry.createRegistry(1234);
      coordinatorRegistry.bind("Coordinator", coordinator);
      // Start the participants
      for (int i = 0; i < 5; i++) {
        ServerParticipant participant = new ServerParticipantImpl("localhost", 1234);
        Registry participantRegistry = LocateRegistry.createRegistry(portArray[i]);
        participantRegistry.bind("Participant", participant);
      }
      coordinator.connectParticipants();
      LoggerUtil.initLogger(log, "src/Server/ServerLog.log");
      System.out.println("Servers ready");
    } catch (Exception e) {
      System.err.println("Server exception: " + e.toString());
      e.printStackTrace();
    }



//    try {
//      LocateRegistry.createRegistry(1099);
//      ServerParticipant rpc = new ServerParticipantImpl();
//      Naming.rebind("RMI", rpc);
//      System.out.println("Server up");
//      LoggerUtil.initLogger(log, "src/Server/ServerLog.log");
//    } catch (Exception e) {
//      System.out.println("Server can't run " + e);
//    }
  }
}
