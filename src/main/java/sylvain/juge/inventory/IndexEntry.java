package sylvain.juge.inventory;

import java.nio.file.Path;

class IndexEntry {
    private final String hash;
    private final Path path;

    public IndexEntry(String hash, Path path) {
        this.hash = hash;
        this.path = path;
    }

    public String getHash() {
        return hash;
    }

    public Path getPath() {
        return path;
    }
}
