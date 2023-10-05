package Client;

import Log.LoggerUtil;
import Server.ServerInterface;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A Java class representing a client application for communication with a server.
 */
public class ClientApplication {
  // Initialize a logger for logging client actions.
  private static final Logger log = Logger.getLogger(ClientApplication.class.getName());
  // A reference to the network gateway interface for communication.
  private static ClientInterface networkGateWay = null;

  /**
   * Default constructor for the ClientApplication class.
   */
  ClientApplication() {
  }

  /**
   * Log a message when the client sends a message to the server.
   *
   * @param message The message sent to the server.
   */
  private static void logClientSendingMessage(String message) {
    System.out.println("Client sending this: " + message);
    log.log(Level.INFO, message);
  }

  /**
   * Log a message when the client receives a response from the server.
   *
   * @param message   The received message from the server.
   * @param sentTime  The timestamp when the message was sent.
   */
  private static void logClientReceivingMessage(String message, long sentTime) {
    // Split the received message to check for validity.
    String[] messages = message.split("#");
    System.out.println(message);
    if (messages.length == 2) {
      if (Long.valueOf(messages[1]).equals(sentTime)) {
        // Log a message when the operation is performed and received correctly.
        log.log(Level.INFO, "Operation performed and received from: " + message);
      } else {
        // Log an error message when an unexpected datagram packet is received.
        log.log(Level.SEVERE, "Unrequested datagram packet received from server: " + message);
      }
    } else {
      // Log an error message when an invalid response is received.
      log.log(Level.SEVERE, "Invalid response received from server: " + message);
    }
  }

  /**
   * Main method to start the client application and handle server communication.
   *
   * @param args Command line arguments. Expects server hostname and port as arguments.
   * @throws IOException           If there is an issue with network communication.
   * @throws InterruptedException  If there is an issue with thread interruption.
   */
  public static void main(String args[]) throws IOException, InterruptedException {
    // Initialize the logger for client logging.
    LoggerUtil.initLogger(log, "src/Client/ClientLog.log");
    ClientApplication clientApp = new ClientApplication();

    if (args.length == 2) {
      int port = Integer.valueOf(args[1]);
      String host = (args[0]);
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter protocol mode of the client.");
      String mode = sc.nextLine();

      if (mode.equals("TCP")) {
        // Initialize the network gateway for TCP communication.
        networkGateWay = new SocketTCPClient();
        networkGateWay.connect(host, port);
      } else if (mode.equals("UDP")) {
        // Initialize the network gateway for UDP communication.
        networkGateWay = new SocketUDPClient();
        networkGateWay.connect(host, port);
      }

      while (true) {
        if (mode.equals("TCP") || mode.equals("UDP")) {
          long currentTime = System.currentTimeMillis();
          System.out.println("Enter operation to perform and operands.");
          String clientCommandMsg = sc.nextLine();

          if (clientCommandMsg != null && clientCommandMsg.length() < 80) {
            // Log the message being sent to the server.
            logClientSendingMessage(clientCommandMsg + " " + String.valueOf(currentTime));
            networkGateWay.send(clientCommandMsg + " #" + String.valueOf(currentTime));
            long timeToLive = 5000;

            while (timeToLive > 0) {
              String response = networkGateWay.receive();
              // Log the response received from the server.
              logClientReceivingMessage(response, currentTime);

              if (response == null) {
                timeToLive -= 1000;
              } else {
                break;
              }
            }
          } else {
            System.out.println("Enter a non-empty command / Enter message in less than 80 characters");
          }
        } else {
          System.out.println("Invalid protocol Mode");
        }
      }
    } else {
      System.out.println("Client was provided with bad args.");
    }
  }
}
