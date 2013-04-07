import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sha1 {
    private static final int BUFFER_SIZE = 8192;
    private static final String HASH_TYPE = "SHA-1";
    private static final Charset CHARSET;
    static {
        try {
            CHARSET = Charset.forName("UTF-8");
        } catch (UnsupportedCharsetException e){
            throw new RuntimeException(e);
        }
    }

    public static String compute(String s){
        InputStream input = new ByteArrayInputStream(s.getBytes(CHARSET));
        return compute(input);
    }

    // compute sha1 from a folder structure whose contents sha1s are known
    // result sha1 is a combination of all those sha1s
    public static String compute(Item item){
        if(item.isFile()){
            throw new RuntimeException("only computes hashes on tree items");
        }
        return "#folder# "+item.getName();
    }

    public static String compute(File file){
        if( file.isDirectory()){
            throw new RuntimeException("can't compute hash on folders");
        }
        InputStream input = null;
        try {
            input = new FileInputStream(file);
        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        }
        return compute(input);
    }

    private static String compute(InputStream input) {
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_TYPE);
            int n = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            do {
                n = input.read(buffer);
                if( 0 < n ){
                    digest.update(buffer, 0, n);
                }
            } while( 0 < n );
            StringBuilder sb = new StringBuilder();
            for(byte b:digest.digest()){
                sb.append(String.format("%02x",b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e){
            throw new RuntimeException(e);
        } catch (IOException e){
            throw new RuntimeException(e);
        } finally {
            try {
                input.close();
            } catch (IOException e){
                // silenly ignored
            }
        }
    }


}
