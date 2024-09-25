package main.server.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class ResourceManager {
    private static final Logger logger = Logger.getLogger(ResourceManager.class.getName());

    private final Path rootPath;
    private final Path errorPath = Path.of("src/main/server/errors");

    public ResourceManager(String rootPath) {
        this.rootPath = formatRootPath(rootPath);
    }

    private Path formatRootPath(String rootPath) {
        if (rootPath.endsWith("/")) {
            rootPath = rootPath.substring(0, rootPath.length() - 1);
        }

        return Path.of(rootPath);
    }

    public byte[] getErrorPage(HttpStatusCode error) {
        Path errorPage = Path.of(errorPath.toString(), error.htmlFile);
        return getString(errorPage).getBytes();
    }

    public Path getResource(String name) {
        name = formatName(name);

        Path resourcePath = Path.of(rootPath.toString(), name);

        if (!Files.exists(resourcePath)) {
            return Path.of(errorPath.toString(), "404.html");
        }

        return resourcePath;
    }

    private String formatName(String name) {
        if (!name.startsWith("/")) {
            name = "/" + name;
        }

        if (name.endsWith("/")) {
            name += "index.html";
        }

        if (getExtension(name).isEmpty()) {
            name += ".html";
        }

        return name;
    }

    private String getExtension(String name) {
        int lastDotIndex = name.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return name.substring(lastDotIndex + 1);
    }

    public String getString(Path htmlFile) {
        String content = "";
        try {
            content = Files.readString(htmlFile);
        } catch (Exception e) {
            logger.warning("Error while reading the file: " + htmlFile);
            content = "Error while reading the file: " + htmlFile;
        }

        return content;
    }
}
