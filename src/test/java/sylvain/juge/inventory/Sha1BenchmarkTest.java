package sylvain.juge.inventory;

import com.github.sylvainjuge.fsutils.FileDigest;
import org.testng.annotations.Test;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Test(enabled=false)
public class Sha1BenchmarkTest {

    @Test
    public void empty_file() throws IOException {
        try(TempFile file = new TempFile()){
            FileDigest digest = new FileDigest("SHA1",8192);
            digest.digest(file.getPath());
        }
    }

    private static final int START_SIZE_BYTES = 1024; // 1kb
    private static final int END_SIZE_BYTES = 1024*1024*1024; // 1gb

    @Test
    public void hash_plot_data(){
        for(int size=START_SIZE_BYTES;size<=END_SIZE_BYTES;size*=2){
            // TODO : print size in human-readable format
            // -> is there an enum listing all common units :

            hash_random_file(size);
        }
    }

    private long hash_random_file(int byteSize){
        try(TempFile file = new TempFile()){
            // TODO : check that we have enough space in target folder

            // write some random bytes into file

            // TODO : allocate a buffer and feed it with random data
            int left = byteSize;
            while(0 < left){
                int writtenBytes = 0;
                left += writtenBytes;
            }

            // start benchmark
            // hash file content
            // end benchmark
        }

        return 0L;
    }


    private class TempFile implements AutoCloseable {

        private final Path path;
        public TempFile(){
            try {
                this.path = Files.createTempFile("tmp","tmp");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public Path getPath(){
            return path;
        }

        @Override
        public void close() {
            try {
                Files.delete(path);
            } catch (IOException e) {
                // silently ignored
            }
        }
    }

}
