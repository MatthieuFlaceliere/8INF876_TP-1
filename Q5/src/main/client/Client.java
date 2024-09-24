package main.client;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an option:");
        System.out.println("1. Connect to local server");
        System.out.println("2. Retrieve HTML from any website");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                connectToLocalServer(scanner);
                break;
            case 2:
                retrieveFromWebsite(scanner);
                break;
            default:
                System.out.println("Invalid choice");
        }
    }

    private static void connectToLocalServer(Scanner scanner) {
        System.out.print("Enter the page you want to retrieve (e.g., index.html, page1.html, ...): ");
        String page = scanner.nextLine();

        try (Socket socket = new Socket("localhost", 8080);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send HTTP GET request
            out.println("GET /" + page + " HTTP/1.1");
            out.println("Host: localhost");
            out.println("Connection: close");
            out.println();

            // Read the response
            String responseLine;
            StringBuilder response = new StringBuilder();
            while ((responseLine = in.readLine()) != null) {
                response.append(responseLine).append("\n");
            }

            // Print the response to the standard output
            System.out.println("Response from server:");
            System.out.println(response.toString());

            // Save the response to a local file
            try (FileWriter fileWriter = new FileWriter("response.html")) {
                fileWriter.write(response.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void retrieveFromWebsite(Scanner scanner) {
        System.out.print("Enter the URL of the website (e.g., http://www.google.com): ");
        String urlString = scanner.nextLine();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine).append("\n");
            }
            in.close();

            // Print the response to the standard output
            System.out.println("Response from website:");
            System.out.println(response.toString());

            // Save the response to a local file
            try (FileWriter fileWriter = new FileWriter("response.html")) {
                fileWriter.write(response.toString());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}