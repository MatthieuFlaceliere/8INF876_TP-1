package main.server;

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
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();

                displayInputStream(inputStream);

                outputStream.write(response("<h1>Hello World!</h1>", 200));
            }
        } catch (Exception e) {
            logger.warning("Error while accepting the connection: " + e.getMessage());
        } finally {
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                logger.warning("Error while closing the server socket: " + e.getMessage());
            }
        }
    }

    public void displayInputStream(InputStream inputStream) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.isEmpty()) {
                    break;
                }
            }
        } catch (IOException e) {
            logger.warning("Error while reading the input stream: " + e.getMessage());
        }
    }

    public byte[] response(String html, int code) {
        final String CRLF = "\r\n"; // 13, 10

        String response =
                "HTTP/1.1" + code + "OK" + CRLF +
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
