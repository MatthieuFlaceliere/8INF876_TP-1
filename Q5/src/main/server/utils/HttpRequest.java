package main.server.utils;

import java.io.*;
import java.util.HashMap;

public class HttpRequest {
    private RequestMethod method;
    private String uri;
    private String httpVersion;
    private final HashMap<String, String> requestHeaders;

    public HttpRequest() {
        requestHeaders = new HashMap<>();
    }

    public void parseRequest(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        readRequestLine(reader);

        readHeaders(reader);
    }

    private void readRequestLine(BufferedReader reader) throws IOException {
        String requestLineText = reader.readLine();

        String[] parts = requestLineText.split(" ");
        method = RequestMethod.valueOf(parts[0]);
        uri = parts[1];
        httpVersion = parts[2];
    }

    private void readHeaders(BufferedReader reader) throws IOException {
        String header = reader.readLine();
        while (header != null && !header.isEmpty()) {
            appendHeaderParameter(header);
            header = reader.readLine();
        }
    }

    private void appendHeaderParameter(String header) {
        String[] parts = header.split(": ");
        if (parts.length != 2) {
            return;
        }
        requestHeaders.put(parts[0], parts[1]);
    }

    public RequestMethod getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public String getHeader(String key) {
        return requestHeaders.get(key);
    }
}


