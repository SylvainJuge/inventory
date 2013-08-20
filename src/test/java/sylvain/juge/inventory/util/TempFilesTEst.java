package sylvain.juge.inventory.util;

import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Fail.fail;

public class TempFilesTest {

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

    @Test
    public void registerFileSystemProvider(){

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
        for (FileSystemProvider provider : providers ) {

        }


        









    }

    @Test
    public void readOnlyFileStoreIsEnough(){
        // create a "readonly" files-tore that acts as a proxy on an actual file-store
        // try to write on this file-store, and see what happens

        FileSystem fileSystem = FileSystems.getDefault();

        for(FileStore store:fileSystem.getFileStores()){
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
        public ReadOnlyFileStore(FileStore store){
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
