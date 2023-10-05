package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A Java class implementing the ServerInterface for TCP socket communication.
 */
public class SocketTCPServer implements ServerInterface {
    private Socket socketPort = null;
    private ServerSocket serverSocket = null;
    private DataInputStream serverIn = null;
    private DataOutputStream serverOut = null;

    /**
     * Default constructor for the SocketTCPServer class.
     */
    public SocketTCPServer() {
    }

    /**
     * Starts the TCP server on the specified port and waits for client connections.
     *
     * @param port The port number on which to start the server.
     * @return The IP address of the connected client as a String.
     * @throws IOException If there is an issue with server initialization or accepting connections.
     */
    @Override
    public String startServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        socketPort = serverSocket.accept();
        return socketPort.getInetAddress().toString();
    }

    /**
     * Sends a message to the connected client using TCP.
     *
     * @param message The message to be sent to the client.
     * @throws IOException If there is an issue sending the message.
     */
    @Override
    public void send(String message) throws IOException {
        serverOut = new DataOutputStream(socketPort.getOutputStream());
        serverOut.writeUTF(message);
    }

    /**
     * Receives a message from the connected client using TCP.
     *
     * @return The received message as a String.
     * @throws IOException If there is an issue receiving the message.
     */
    @Override
    public String receive() throws IOException {
        serverIn = new DataInputStream(socketPort.getInputStream());
        String line = serverIn.readUTF();
        return line;
    }

    /**
     * Closes the connection to the client by closing streams and the socket.
     *
     * @throws IOException If there is an issue closing the connection.
     */
    @Override
    public void closeConnection() throws IOException {
        serverIn.close();
        serverOut.close();
        socketPort.close();
    }

    /**
     * Retrieves the IP address of the connected client.
     *
     * @return The IP address of the connected client as a String.
     */
    @Override
    public String getClientIp() {
        return socketPort.getInetAddress().toString();
    }
}
