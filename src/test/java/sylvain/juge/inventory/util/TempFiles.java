package sylvain.juge.inventory.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * handles temporary files, either in memory or on disk, with a cleanup option
 * for tests : allow fast execution of test using filesystem operations and handle cleanup
 * */
public class TempFiles {

    private List<Path> items;

    // TODO
    // - see if choosing a proper FileStore implementation is enough to provide read-only view on an existing FS

    private TempFiles(){
        items = new ArrayList<>();
    }

    public static TempFiles inMemory(){
        return new TempFiles();
    }

    public static TempFiles onDisk(){
        return new TempFiles();
    }

    public Path newTempFile(){
        // generate new temp file
        return null;
    }

    public Path newTempFolder(){
        // generate new temp folder
        return null;
    }

    /**
     * removes all temporary files & folders created through this {@link TempFiles} instance
     * @throws IllegalStateException if not able to properly delete files
     * */
     public void cleanup(){
        for(Path p:items){
            try {
                Files.deleteIfExists(p);
            } catch (IOException e) {
                throw new IllegalStateException("unable to delete file", e);
            }
        }
    }
}
