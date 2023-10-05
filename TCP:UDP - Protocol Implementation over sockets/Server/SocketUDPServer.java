package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * A Java class implementing the ServerInterface for UDP socket communication.
 */
public class SocketUDPServer implements ServerInterface {
  private DatagramSocket udpSocket;
  private int clientPort;
  private byte[] dataGramPacket = new byte[512];
  private InetAddress address;

  /**
   * Default constructor for the SocketUDPServer class.
   *
   * @throws SocketException If there is an issue creating the DatagramSocket.
   */
  public SocketUDPServer() throws SocketException {
  }

  // Main method for SocketUDPServer class (unused).
  public static void main(String args[]) throws IOException {
  }

  /**
   * Starts the UDP server on the specified port.
   *
   * @param port The port number on which to start the server.
   * @return A message indicating the success or failure of server startup (not used in UDP).
   * @throws IOException If there is an issue with server initialization.
   */
  @Override
  public String startServer(int port) throws IOException {
    udpSocket = new DatagramSocket(port);
    return null;
  }

  /**
   * Sends a message to the connected client using UDP.
   *
   * @param message The message to be sent to the client.
   * @throws IOException If there is an issue sending the message.
   */
  @Override
  public void send(String message) throws IOException {
    byte[] newSendingMessageBytes = new byte[512];
    newSendingMessageBytes = message.getBytes();
    DatagramPacket packet = new DatagramPacket(newSendingMessageBytes, newSendingMessageBytes.length,
        address, clientPort);
    udpSocket.send(packet);
  }

  /**
   * Receives a message from the connected client using UDP.
   *
   * @return The received message as a String.
   * @throws IOException If there is an issue receiving the message.
   */
  @Override
  public String receive() throws IOException {
    DatagramPacket packet
        = new DatagramPacket(dataGramPacket, dataGramPacket.length);
    udpSocket.receive(packet);
    address = packet.getAddress();
    clientPort = packet.getPort();
    String received
        = new String(packet.getData(), 0, packet.getLength());
    return received;
  }

  /**
   * Closes the UDP connection to the client by closing the DatagramSocket.
   *
   * @throws IOException If there is an issue closing the connection.
   */
  @Override
  public void closeConnection() throws IOException {
    udpSocket.close();
  }

  /**
   * Retrieves the IP address of the connected client.
   *
   * @return The IP address of the connected client as a String.
   */
  @Override
  public String getClientIp() {
    return address.toString();
  }
}
