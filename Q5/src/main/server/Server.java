package main.server;

import main.server.utils.HttpRequest;
import main.server.utils.HttpStatusCode;
import main.server.utils.RequestMethod;
import main.server.utils.ResourceManager;

import java.io.*;
import java.net.*;
import java.nio.file.Path;
import java.util.logging.Logger;

public class Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());
    private static final ResourceManager resourceManager = new ResourceManager("src/main/server/public");

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

                outputStream.write(processRequest(inputStream));
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

    public byte[] processRequest(InputStream inputStream) {
        HttpRequest httpRequest = new HttpRequest();
        try {
            httpRequest.parseRequest(inputStream);
        } catch (Exception e) {
            return response(resourceManager.getErrorPath(HttpStatusCode.INTERNAL_SERVER_ERROR), HttpStatusCode.INTERNAL_SERVER_ERROR);
        }

        if (!httpRequest.getMethod().equals(RequestMethod.GET)) {
            return response(resourceManager.getErrorPath(HttpStatusCode.METHOD_NOT_ALLOWED), HttpStatusCode.METHOD_NOT_ALLOWED);
        }
        logger.info(httpRequest.getUri());
        return response(resourceManager.getResource(httpRequest.getUri()), HttpStatusCode.OK);
    }

    public byte[] response(Path htmlFile, HttpStatusCode statusCode) {
        final String CRLF = "\r\n"; // 13, 10

        String content = resourceManager.getString(htmlFile);

        String response =
                "HTTP/1.1 " + statusCode + CRLF +
                        "Content-Length: " + content.length() + CRLF +
                        CRLF +
                        content +
                        CRLF + CRLF;

        return response.getBytes();
    }

    public static void main(String[] args) {
        Server server = new Server(8080);
        server.start();
    }
}
