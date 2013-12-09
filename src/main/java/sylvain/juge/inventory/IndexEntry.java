package sylvain.juge.inventory;

import java.nio.file.Path;

import static sylvain.juge.inventory.util.ParamAssert.notNull;

public final class IndexEntry {

    private final String hash;
    private final Path path;

    public IndexEntry(String hash, Path path) {
        notNull(hash, "hash");
        notNull(path, "path");
        this.hash = hash;
        this.path = path;
    }

    public String getHash() {
        return hash;
    }

    public Path getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "IndexEntry{" +
                "hash='" + hash + '\'' +
                ", path=" + path +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexEntry that = (IndexEntry) o;

        if (!hash.equals(that.hash)) return false;
        if (!path.equals(that.path)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hash.hashCode();
        result = 31 * result + path.hashCode();
        return result;
    }
}
