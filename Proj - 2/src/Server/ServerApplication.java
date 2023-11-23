package Server;

import Log.LoggerUtil;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import Shared.RPCMethods;

public class ServerApplication {
  private static final Logger log = Logger.getLogger(ServerApplication.class.getName());

  public static void main(String[] args) {
    try {
      LocateRegistry.createRegistry(1099);
      RPCMethods rpc = new RPCFunctions();
      Naming.rebind("RMI", rpc);
      System.out.println("Server up");
      LoggerUtil.initLogger(log, "src/Server/ServerLog.log");
    } catch (Exception e) {
      System.out.println("Server can't run " + e);
    }
  }
}
