package sylvain.juge.inventory;

import java.nio.file.Path;

import static sylvain.juge.inventory.util.ParamAssert.notNull;

public final class IndexEntry {

    private final String hash;
    private final Path path;
    private long size;
    private long timestamp;

    public IndexEntry(String hash, Path path, long size, long timestamp){
        notNull(hash, "hash");
        notNull(path, "path");
        this.hash = hash;
        this.path = path;
        this.size = size;
        this.timestamp = timestamp;
    }

    @Deprecated
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

    public long getTimestamp(){
        return timestamp;
    }

    public long getSize(){
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IndexEntry that = (IndexEntry) o;

        if (size != that.size) return false;
        if (timestamp != that.timestamp) return false;
        if (!hash.equals(that.hash)) return false;
        if (!path.equals(that.path)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = hash.hashCode();
        result = 31 * result + path.hashCode();
        result = 31 * result + (int) (size ^ (size >>> 32));
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "IndexEntry{" +
                "hash='" + hash + '\'' +
                ", path=" + path +
                ", size=" + size +
                ", timestamp=" + timestamp +
                '}';
    }
}