package Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * A Java class implementing the ClientInterface for TCP socket communication.
 */
public class SocketTCPClient implements ClientInterface {
    private Socket socketPort = null;
    private DataInputStream clientIn = null;
    private DataOutputStream clientOut = null;

    /**
     * Default constructor for the SocketTCPClient class.
     */
    public SocketTCPClient() {}

    /**
     * Establishes a connection to a server with the specified hostname and port.
     *
     * @param hostname The hostname or IP address of the server.
     * @param port     The port number on which to connect to the server.
     */
    @Override
    public void connect(String hostname, int port) {
        try {
            socketPort = new Socket(hostname, port);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * Sends a message to the connected server.
     *
     * @param message The message to be sent to the server.
     * @throws IOException If there is an issue sending the message.
     */
    @Override
    public void send(String message) throws IOException {
        System.out.println("Client Connected");
        clientOut = new DataOutputStream(socketPort.getOutputStream());
        clientOut.writeUTF(message);
    }

    /**
     * Receives a message from the connected server.
     *
     * @return The received message as a String.
     * @throws IOException If there is an issue receiving the message.
     */
    @Override
    public String receive() throws IOException {
        clientIn = new DataInputStream(socketPort.getInputStream());
        return clientIn.readUTF();
    }

    /**
     * Closes the connection to the server by closing streams and the socket.
     */
    @Override
    public void closeConnection() {
        try {
            clientIn.close();
            clientOut.close();
            socketPort.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
