package main.client;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        logger.info("Enter the URL of the website (e.g., http://localhost:8080/index.html or http://www.google.com): ");
        String urlString = scanner.nextLine();
        fetchContent(urlString);
    }

    private static void fetchContent(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();
            int port = url.getPort() == -1 ? 80 : url.getPort();
            String path = url.getPath().isEmpty() ? "/" : url.getPath();

            try (Socket socket = new Socket(host, port);
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                // Send HTTP GET request
                out.println("GET " + path + " HTTP/1.1");
                out.println("Host: " + host);
                out.println("Connection: close");
                out.println();

                // Read the response
                String responseLine;
                StringBuilder response = new StringBuilder();
                while ((responseLine = in.readLine()) != null) {
                    response.append(responseLine).append("\n");
                }

                // Log the response
                logger.info("Response from website:");
                logger.info(response.toString());

                // Save the response to a local file
                try (FileWriter fileWriter = new FileWriter("response.html")) {
                    fileWriter.write(response.toString());
                }

            } catch (IOException e) {
                logger.severe("Error while fetching content: " + e.getMessage());
            }

        } catch (IOException e) {
            logger.severe("Invalid URL: " + e.getMessage());
        }
    }
}