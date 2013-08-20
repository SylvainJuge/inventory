package sylvain.juge.inventory;

import sylvain.juge.inventory.util.NotImplementedException;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

class Index {

    private final static String INDEX_FILE_NAME = ".inventory";
    private final Path root;
    private List<IndexEntry> entries;

    Index(Path root){
        this.root = root;
        this.entries = new ArrayList<>();
    }

    public Path getRoot(){
        return root;
    }

    /**
     * creates a new empty index in folder
     * @param path folder to index
     * @return empty index
     */
    public static Index newIndex(Path path){
        return new Index(path);
    }

    /**
     * loads an existing index in directory
     * @param path folder where the index file is stored
     */
    public static Index loadIndex(Path path){
        throw new NotImplementedException();
    }

    private static class UpdateFileVisitor extends SimpleFileVisitor<Path> {

        private final List<IndexEntry> entries;

        private UpdateFileVisitor() {
            this.entries = new ArrayList<>();
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            IndexEntry entry = new IndexEntry("", file);
            entries.add(entry);
            return FileVisitResult.CONTINUE;
        }

        public List<IndexEntry> getEntries(){
            return entries;
        }
    }

    /** refresh the whole index with filesystem */
    public void update(){
        UpdateFileVisitor visitor = new UpdateFileVisitor();
        try {
            Files.walkFileTree(root, visitor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.entries = visitor.getEntries();
    }

    /** writes index into its root folder */
    public void write(){
        throw new NotImplementedException();
    }

    /** writes index to a specific file
     * @param file where to store index data */
    public void write(Path file){
        throw new NotImplementedException();
    }


    public List<IndexEntry> getEntries(){
        return entries;
    }
}
