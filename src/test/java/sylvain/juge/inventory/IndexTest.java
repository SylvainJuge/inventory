package sylvain.juge.inventory;


import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import sylvain.juge.inventory.util.TempFolder;

import java.nio.file.Path;

import static org.fest.assertions.api.Assertions.assertThat;

public class IndexTest {

    private final TempFolder tempFolder = TempFolder.createNew();

    @AfterTest
    private void after(){
        tempFolder.cleanup();
    }

    @Test
    public void emptyIndex(){

        Path root = tempFolder.getPath();

        Index index = new Index(root);
        assertThat(index.getEntries()).isEmpty();
        assertThat(index.getRoot()).isEqualTo(root);
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