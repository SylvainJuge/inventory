package sylvain.juge.inventory;


import com.github.sylvainjuge.fsutils.ByteUnit;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.util.List;
import java.util.Locale;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.fest.assertions.api.Assertions.assertThat;
import static com.github.sylvainjuge.fsutils.ByteUnit.BYTES;

public class PrototypeTest {

    public void indexPhotos() throws IOException {

        Path root = Paths.get("D:\\Photos & Vidéos\\2011");
        assertThat(Files.isDirectory(root)).isTrue();

        Index index = Index.newIndex(root);

        assertThat(index.getRoot()).isEqualTo(root);

        List<IndexEntry> entries = index.getEntries();
        assertThat(entries).isEmpty();
        long start = System.currentTimeMillis();
        index.update();
        long updateTime = System.currentTimeMillis() - start;
        entries = index.getEntries();
        long totalSize = 0;
        for (IndexEntry entry : entries) {
            assertThat(entry.getHash()).isNotNull().hasSize(40);
            assertThat(entry.getPath().isAbsolute()).isFalse();
            Path actualPath = index.getEntryPath(entry);
            assertThat(Files.isRegularFile(actualPath)).isTrue();
            BasicFileAttributeView attributeView = Files.getFileAttributeView(actualPath, BasicFileAttributeView.class);
            totalSize += attributeView.readAttributes().size();
        }

        ByteUnit.Converter converter = BYTES.toPretty(totalSize);
        System.out.format(Locale.ENGLISH, "%.2f %s in %d s", converter.convert(totalSize),converter.target().abbreviation(), SECONDS.convert(updateTime, MILLISECONDS));

    }

}
