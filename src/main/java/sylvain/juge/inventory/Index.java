package sylvain.juge.inventory;


import com.github.sylvainjuge.fsutils.FileDigester;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import sylvain.juge.inventory.util.NotImplementedException;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Index {

    private final static String INDEX_FILE_NAME = ".inventory";
    private final Path root;
    private final HashFunction hashFunction;
    private     List<IndexEntry> entries;

    Index(Path root, HashFunction hashFunction) {
        this.root = root;
        this.hashFunction = hashFunction;
        this.entries = new ArrayList<>();
    }

    public Path getRoot() {
        return root;
    }

    public Path getEntryPath(IndexEntry entry){
        return root.resolve(entry.getPath());
    }

    /**
     * Creates a new empty index in folder
     *
     * @param path folder to index
     * @param hashFunction hash function for index
     * @return empty index
     */
    public static Index newIndex(Path path, HashFunction hashFunction){
        return new Index(path, hashFunction);
    }

    /**
     * Loads an existing index in directory
     *
     * @param path folder where the index file is stored
     */
    public static Index loadIndex(Path path) {
        throw new NotImplementedException();
    }

    private static class UpdateFileVisitor extends SimpleFileVisitor<Path> {

        private final List<Future<IndexEntry>> entries;
        private final FileDigester fileDigester;
        private final Path indexRoot;
        ExecutorService hashPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        private UpdateFileVisitor(Path indexRoot, HashFunction hashFunction) {
            this.entries = new ArrayList<>();
            // TODO : buffer size does not have much effect on speed
            // however, too big (1mb) makes wasting time allocating
            this.fileDigester = new FileDigester(hashFunction, 64*1024);
            this.indexRoot = indexRoot;
        }

        @Override
        public FileVisitResult visitFile(final Path file, BasicFileAttributes attrs) throws IOException {
            if (Files.isRegularFile(file)) {

                entries.add(
                        hashPool.submit(new Callable<IndexEntry>() {
                            @Override
                            public IndexEntry call() throws Exception {
                                String digest = fileDigester.digest(file);
                                long size = 0;
                                long timestamp = 0;
                                return new IndexEntry(digest, indexRoot.relativize(file), size, timestamp);
                            }
                        }));
            }

            // FIXME : no proper shutdown for execution pool
            return FileVisitResult.CONTINUE;
        }

        public List<Future<IndexEntry>> getEntries() {
            return entries;
        }

        public void close() {
            try {
                hashPool.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                hashPool.shutdown();
            }
        }
    }

    /**
     * refresh the whole index with filesystem
     */
    public void update() {
        UpdateFileVisitor visitor = new UpdateFileVisitor(root, hashFunction);
        try {
            Files.walkFileTree(root, visitor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            this.entries = new ArrayList<>();
            for (Future<IndexEntry> entry : visitor.getEntries()) {
                try {
                    entries.add(entry.get());
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }

            }
        } finally {
            visitor.close();
        }
    }


    /**
     * writes index into its root folder
     */
    public void write() {
        throw new NotImplementedException();
    }

    /**
     * writes index to a specific file
     *
     * @param file where to store index data
     */
    public void write(Path file) {
        throw new NotImplementedException();
    }


    public List<IndexEntry> getEntries() {
        return entries;
    }
}
