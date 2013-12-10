package sylvain.juge.inventory.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.fest.assertions.api.Fail.fail;

/** temporary folder for tests
 * @deprecated use memory fs for that purpose */
@Deprecated
public final class TempFolder {

    private final Path root;

    // TODO
    // - see if choosing a proper FileStore implementation is enough to provide read-only view on an existing FS

    private TempFolder(Path root){
        this.root = root;
    }

    /** @return path of this temporary folder */
    public Path getPath(){
        return root;
    }

    public static TempFolder createNew(){
        Path dir;
        try {
            dir = Files.createTempDirectory("test");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new TempFolder(dir);
    }

    public void cleanup(){
        try {
            Files.deleteIfExists(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
