package Client;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * An interface that defines methods for a client to interact with a server.
 */
public interface ClientInterface {

  /**
   * Connects to a server with the specified hostname and port number.
   *
   * @param hostname The hostname or IP address of the server to connect to.
   * @param port     The port number on which to connect.
   * @throws SocketException     If there's an issue with the socket.
   * @throws UnknownHostException If the hostname is not found.
   */
  void connect(String hostname, int port) throws SocketException, UnknownHostException;

  /**
   * Sends a message to the connected server.
   *
   * @param message The message to be sent to the server.
   * @throws IOException If there's an issue sending the message.
   */
  void send(String message) throws IOException;

  /**
   * Receives a message from the connected server.
   *
   * @return The received message as a String.
   * @throws IOException If there's an issue receiving the message.
   */
  String receive() throws IOException;

  /**
   * Closes the connection to the server.
   */
  void closeConnection();
}
