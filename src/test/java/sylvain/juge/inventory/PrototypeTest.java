package sylvain.juge.inventory;


import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class PrototypeTest {

//    @Test
    public void indexPhotos(){

        Path root = Paths.get("D:\\Photos & Vidéos\\2011");
        assertThat(Files.isDirectory(root)).isTrue();

        Index index = Index.newIndex(root);

        assertThat(index.getRoot()).isEqualTo(root);

        List<IndexEntry> entries = index.getEntries();
        assertThat(entries).isEmpty();
        index.update();
        entries = index.getEntries();
        assertThat(entries).isNotEmpty().hasSize(387);
        for( IndexEntry entry:entries){
            assertThat(entry.getHash()).isNotNull().hasSize(40);
            assertThat(entry.getPath().isAbsolute()).isFalse();
            Path actualPath = index.getRoot().resolve(entry.getPath());
            assertThat(Files.isRegularFile(actualPath)).isTrue();
        }



    }

}
