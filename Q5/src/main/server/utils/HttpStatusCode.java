package main.server.utils;

public enum HttpStatusCode {
    OK(200, "OK", "200.html"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed", "405.html"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error", "500.html"),
    NOT_FOUND(404, "Not Found", "404.html");

    public final int statusCode;
    public final String message;
    public final String htmlFile;

    HttpStatusCode(int statusCode, String message, String htmlFile) {
        this.statusCode = statusCode;
        this.message = message;
        this.htmlFile = htmlFile;
    }

    @Override
    public String toString() {
        return this.statusCode + " " + this.message + " " + this.htmlFile;
    }
}
