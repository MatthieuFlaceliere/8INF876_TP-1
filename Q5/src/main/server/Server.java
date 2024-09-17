package main.server;

import main.server.utils.HttpRequest;
import main.server.utils.HttpStatusCode;
import main.server.utils.RequestMethod;

import java.io.*;
import java.net.*;
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
            return response("<h1>Internal Server Error</h1>", HttpStatusCode.INTERNAL_SERVER_ERROR);
        }

        if (!httpRequest.getMethod().equals(RequestMethod.GET)) {
            return response("<h1>Method Not Allowed</h1>", HttpStatusCode.METHOD_NOT_ALLOWED);
        }
        logger.info(httpRequest.getUri());
        return response("<h1>Hello World!</h1>", HttpStatusCode.OK);
    }

    public byte[] response(String html, HttpStatusCode statusCode) {
        final String CRLF = "\r\n"; // 13, 10

        String response =
                "HTTP/1.1 " + statusCode + CRLF +
                        "Content-Length: " + html.getBytes().length + CRLF +
                        CRLF +
                        html +
                        CRLF + CRLF;

        return response.getBytes();
    }

    public static void main(String[] args) {
        Server server = new Server(8080);
        server.start();
    }
}
