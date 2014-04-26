package sylvain.juge.inventory;


import com.google.common.hash.Hashing;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.fest.assertions.api.Assertions.assertThat;

public class IndexTest {

    private static final String EMPTY_FILE_SHA1 = "da39a3ee5e6b4b0d3255bfef95601890afd80709";

    @Test
    public void emptyIndex() throws IOException {

        try (FileSystem fs = newMemoryFs()) {

            Path root = fs.getPath("/");

            Index index = Index.newIndex(root, Hashing.sha1());
            assertThat(index.getEntries()).isEmpty();
            assertThat(index.getRoot()).isEqualTo(root);

        }
    }

    @Test
    public void emptyFileIndex() throws IOException {
        try (FileSystem fs = newMemoryFs()) {
            Path root = fs.getPath("/");
            Files.createFile(root.resolve("file"));

            Index index = Index.newIndex(root, Hashing.sha1());
            index.update();
            assertThat(index.getEntries()).hasSize(1);

            IndexEntry entry = index.getEntries().get(0);
            assertThat(entry.getSize()).isEqualTo(0);
            assertThat(entry.getHash()).isEqualTo(EMPTY_FILE_SHA1);
        }
    }

    private FileSystem newMemoryFs() throws IOException {
        return FileSystems.newFileSystem(URI.create("memory:/test"), null);
    }

    // create index with specific items

    // intersection btw two indexes :
    // - with items in common
    // - with items not in common

    // read index form file
    // write index to file

    // build index from directory structure
    // - in an actual filesystem (will require some files in test resources)
    // - in a fake memory filesystem

}
