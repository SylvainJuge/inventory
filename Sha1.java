import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Sha1 {
    private static final int BUFFER_SIZE = 8192;
    public static void main(String[] args){
        File file = new File(args[0]);
        System.out.println(compute(file)+" " + file);
    }

    public static String compute(File file){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            InputStream fis = new FileInputStream(file);
            int n = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            do {
                n = fis.read(buffer);
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
        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
