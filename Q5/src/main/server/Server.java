package main.server;

import main.server.utils.*;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            logger.info("Server started on port " + port);
        } catch (IOException e) {
            logger.warning("Impossible to create the server socket: " + e.getMessage());
        }
    }

    public void start() {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();

                HttpRequest httpRequest = new HttpRequest();
                Response response = httpRequest.processRequest(inputStream);

                outputStream.write(response.send());
                outputStream.close();
                inputStream.close();
            }
        } catch (Exception e) {
            logger.warning("Error while accepting the connection: " + e.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                logger.warning("Error while closing the streams: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8080);
        server.start();
    }
}
