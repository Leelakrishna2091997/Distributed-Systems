package Server;

import java.io.IOException;

/**
 * An interface defining methods for a server to interact with clients.
 */
public interface ServerInterface {

  /**
   * Starts the server on the specified port.
   *
   * @param port The port number on which to start the server.
   * @return A message indicating the success or failure of server startup.
   * @throws IOException If there is an issue with server initialization.
   */
  String startServer(int port) throws IOException;

  /**
   * Sends a message to the connected client.
   *
   * @param message The message to be sent to the client.
   * @throws IOException If there is an issue sending the message.
   */
  void send(String message) throws IOException;

  /**
   * Receives a message from the connected client.
   *
   * @return The received message as a String.
   * @throws IOException If there is an issue receiving the message.
   */
  String receive() throws IOException;

  /**
   * Closes the connection to the client.
   *
   * @throws IOException If there is an issue closing the connection.
   */
  void closeConnection() throws IOException;

  /**
   * Retrieves the IP address of the connected client.
   *
   * @return The IP address of the connected client as a String.
   */
  String getClientIp();
}
