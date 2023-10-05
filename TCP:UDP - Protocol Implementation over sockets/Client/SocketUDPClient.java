package Client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * A Java class implementing the ClientInterface for UDP socket communication.
 */
public class SocketUDPClient implements ClientInterface {
  private DatagramSocket udpSocket;
  private InetAddress address;
  private String hostName;
  private int port;
  private byte[] dataGramPacket;
  private DatagramPacket packet;

  /**
   * Default constructor for the SocketUDPClient class.
   *
   * @throws SocketException     If there is an issue creating the DatagramSocket.
   * @throws UnknownHostException If the hostname cannot be resolved to an IP address.
   */
  public SocketUDPClient() throws SocketException, UnknownHostException {
  }

  /**
   * Establishes a UDP connection to a server with the specified hostname and port.
   *
   * @param hostname The hostname or IP address of the server.
   * @param port     The port number on which to connect to the server.
   * @throws SocketException     If there is an issue creating the DatagramSocket.
   * @throws UnknownHostException If the hostname cannot be resolved to an IP address.
   */
  @Override
  public void connect(String hostname, int port) throws SocketException, UnknownHostException {
    udpSocket = new DatagramSocket();
    this.hostName = hostname;
    this.port = port;
    address = InetAddress.getByName("localhost");
  }

  /**
   * Sends a message to the connected server using UDP.
   *
   * @param message The message to be sent to the server.
   * @throws IOException If there is an issue sending the message.
   */
  @Override
  public void send(String message) throws IOException {
    dataGramPacket = message.getBytes();
    packet = new DatagramPacket(dataGramPacket, dataGramPacket.length,
        InetAddress.getByName(this.hostName), this.port);
    udpSocket.send(packet);
  }

  /**
   * Receives a message from the connected server using UDP.
   *
   * @return The received message as a String.
   * @throws IOException If there is an issue receiving the message.
   */
  @Override
  public String receive() throws IOException {
    dataGramPacket = new byte[512];
    packet = new DatagramPacket(dataGramPacket, dataGramPacket.length);
    udpSocket.receive(packet);
    String received = new String(
        packet.getData(), 0, packet.getLength());
    return received;
  }

  /**
   * Closes the UDP connection to the server by closing the DatagramSocket.
   */
  @Override
  public void closeConnection() {
    udpSocket.close();
  }
}
