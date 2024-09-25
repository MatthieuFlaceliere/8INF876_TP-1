package main.server.utils;

import java.nio.file.Path;

public class Response {
    private static final ResourceManager resourceManager = new ResourceManager("src/main/server/public");

    private HttpStatusCode statusCode;
    private byte[] body;

    public Response() {
        this.statusCode = HttpStatusCode.OK;
        this.body = new byte[0];
    }

    public Response setStatusCode(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
        if (statusCode != HttpStatusCode.OK) {
            this.body = resourceManager.getErrorPage(statusCode);
        }
        return this;
    }

    public Response setBody(Path path) {
        this.body = resourceManager.getString(path).getBytes();
        return this;
    }

    public byte[] send() {
        String crlf = "\r\n";
        String response =
                "HTTP/1.1 " + statusCode + crlf +
                        "Content-Length: " + body.length + crlf +
                        crlf +
                        new String(body) +
                        crlf + crlf;
        return response.getBytes();
    }
}
