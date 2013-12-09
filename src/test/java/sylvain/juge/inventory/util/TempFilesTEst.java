package sylvain.juge.inventory.util;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.ArrayList;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Fail.fail;

public class TempFilesTest {

    @Test
    public void cleanupEmpty() {
        TempFolder tempFolder = TempFolder.createNew();
        assertThat(Files.isDirectory(tempFolder.getPath())).isTrue();
        assertThat(listFiles(tempFolder.getPath())).isEmpty();
        tempFolder.cleanup();
        assertThat(Files.exists(tempFolder.getPath())).isFalse();
    }

    private static List<Path> listFiles(Path dir){
        assertThat(Files.isDirectory(dir)).isTrue();
        ListInFolderVisitor visitor = new ListInFolderVisitor();
        try {
            Files.walkFileTree(dir, visitor);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    private static final class ListInFolderVisitor extends SimpleFileVisitor<Path> {
        private final List<Path> content;
        private final int maxDepth;
        private int currentDepth = 0;

        public ListInFolderVisitor(){
            this(Integer.MAX_VALUE);
        }
        public ListInFolderVisitor(int maxDepth){
            content = new ArrayList<>();
            this.maxDepth = maxDepth;
        }

        private List<Path> getContent() {
            return content;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            if (currentDepth >= maxDepth) {
                System.out.println("skip directory too deep :" + dir);
                return FileVisitResult.SKIP_SIBLINGS;
            }
            if (0 < currentDepth) {
                content.add(dir);
            }
            currentDepth ++;
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            System.out.println("post visit directory :" + dir);
            currentDepth--;
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            System.out.println("visit file : " + file);
            content.add(file);
            return FileVisitResult.CONTINUE;
        }
    }

    @Test
    public void pathTest(){
        Path root = Paths.get("/test/root");
        System.out.println(root.getFileName());
        System.out.println(root.getNameCount());
        System.out.println(root.getParent());
        System.out.println(root.getRoot()); // null for relative paths
        System.out.println(root.isAbsolute());
        System.out.println(root.toAbsolutePath());
        System.out.println(root.getFileSystem()); // reference to actual fs
    }


    @Test
    public void tempDirectoryCreationAndDeletion() throws IOException {
        TempFolder tempFolder = TempFolder.createNew();
        Path path = tempFolder.getPath();

        assertThat(Files.isDirectory(path)).isTrue();

        ListInFolderVisitor visitor = new ListInFolderVisitor();
        Files.walkFileTree(path, visitor);
        assertThat(visitor.getContent()).isEmpty();


        // TODO : find a more elegant way to create a file in a folder
        Path emptyFile = Paths.get(path.toString(), "emptyFile");
        System.out.println(emptyFile);
        Files.createFile(emptyFile);
        Files.walkFileTree(path, visitor);
        assertThat(visitor.getContent()).containsOnly(emptyFile);

        // does there exists a more simple syntax for this ??
//        Files.walkFileTree(dir, new HashSet<>(), 1, new FileVisitor extends SimpleFileVisitor<Path> {
//
//        });


        tempFolder.cleanup();
        assertThat(Files.exists(path)).isFalse();
    }

    // -- idea in progress : memory file system for tests
    // nice features :
    // - copy existing filesystem structure
    // - can be switched RO/RW at runtime
    // - can trigger exceptions when reading/writing (throw IOExceptions when they usually never happen in a real FS).

    // TODO : readonly filesystem
    // -> FileStore.isReadOnly()
    //  -> sounds like having a file-store readonly proxy does the job
    //  -> however, we need to find actual subclasses of file-store to see how they work
    // TODO : in memory filesystem (outside heap is better)
    //
    // file-system
    // -> list of file-stores
    // file-system-provider
    // -> factory on file-system instances
    //
    // Files
    // -----> sounds like We must properly register provider & FS for proper integration in Files

    //    @Test
    public void registerFileSystemProvider() {

        // Note : this test must not be run concurrently with other tests since it has global side effects

        // - default FS is already available and ready to use
        // - FileSystems.newFileSystem(..) : creates and lazily registers filesystems using scheme part of uri to find appropriate implementation for desired FS.
        // - ServiceLoader allows to provide service implementations of a given class/interface through descriptor in META-INF
        // -> thus adding a new FS is just a matter of defining the appropriate FS implementation in classpath, and associate a unique scheme to it
        /// - > more specific, we have to provide an implementation of FileSystemProvider,
        //  -> how to choose the appropriate class loader for that task ?

        List<FileSystemProvider> providers = FileSystemProvider.installedProviders();
        assertThat(providers).isNotEmpty();
        int beforeSize = providers.size();
        for (FileSystemProvider provider : providers) {

        }


    }

    //    @Test
    public void readOnlyFileStoreIsEnough() {
        // create a "readonly" files-tore that acts as a proxy on an actual file-store
        // try to write on this file-store, and see what happens

        FileSystem fileSystem = FileSystems.getDefault();

        for (FileStore store : fileSystem.getFileStores()) {
            assertThat(store.isReadOnly()).isFalse();

            FileStore readOnlyStore = new ReadOnlyFileStore(store);
            assertThat(readOnlyStore.isReadOnly()).isTrue();

            assertThat(readOnlyStore.name()).isEqualToIgnoringCase("test");

            // try to write to this filesystem, which must fail


        }


    }

    public boolean canWriteInFolder(Path dir) {

        boolean thrown = false;
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile(dir, "writeCheck", ".tmp");
        } catch (IOException e) {
            thrown = true;
        } finally {
            try {
                // TODO ?? no 'silent' version of this method ? and that removes not empty directories ?
                // -> sounds that commons-file still has some advantages here (unless there is another way to do this)
                Files.deleteIfExists(tempFile);
            } catch (IOException e) {
                fail(e.getMessage());
            }
        }
        return !thrown;

    }

    private static class ReadOnlyFileStore extends FileStore {

        private final FileStore store;

        public ReadOnlyFileStore(FileStore store) {
            this.store = store;
        }

        @Override
        public String name() {
            return store.name();
        }

        @Override
        public String type() {
            return store.type();
        }

        @Override
        public boolean isReadOnly() {
            return true;
        }

        @Override
        public long getTotalSpace() throws IOException {
            return store.getTotalSpace();
        }

        @Override
        public long getUsableSpace() throws IOException {
            return store.getUsableSpace();
        }

        @Override
        public long getUnallocatedSpace() throws IOException {
            return store.getUnallocatedSpace();
        }

        @Override
        public boolean supportsFileAttributeView(Class<? extends FileAttributeView> type) {
            return store.supportsFileAttributeView(type);
        }

        @Override
        public boolean supportsFileAttributeView(String name) {
            return store.supportsFileAttributeView(name);
        }

        @Override
        public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> type) {
            return store.getFileStoreAttributeView(type);
        }

        @Override
        public Object getAttribute(String attribute) throws IOException {
            return store.getAttribute(attribute);
        }
    }
}
