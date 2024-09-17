package main.server.utils;

public enum HttpStatusCode {
    OK(200, "OK"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    public final int statusCode;
    public final String message;

    HttpStatusCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public String toString() {
        return this.statusCode + " " + this.message;
    }
}
