package sylvain.juge.inventory.old;

import com.github.sylvainjuge.fsutils.FileDigester;
import com.google.common.hash.Hashing;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Item implements Comparable<Item> {

    private static final char FIELD_SEPARATOR = '\n';

    private static final String HASH_ALGORITHM = "SHA-1";
    private static final String ENCODING = "UTF-8";
    private static final int HASH_BUFFER_SIZE = 8192;

    private static final FileDigester DIGEST = new FileDigester(Hashing.sha1(),HASH_BUFFER_SIZE);

    private final String hash;
    private final String name;
    private final List<Item> children;

    public String getHash(){ return hash; }
    public String getName(){ return name; }
    public List<Item> getChildren(){ return Collections.unmodifiableList(children); }

    private Item(String hash, String name, List<Item> children){
        this.hash = hash;
        this.name = name;
        this.children = null != children ? children : new ArrayList<Item>();
    }

    public static Item tree(String name, List<Item> children){
        StringBuilder sb = new StringBuilder();
        List<Item> childList = new ArrayList<>();
        childList.addAll(children);
        Collections.sort(childList);
        for(Item i:childList){
            sb.append(FIELD_SEPARATOR);
            sb.append(i.getHash());
            sb.append(FIELD_SEPARATOR);
            sb.append(i.getName());
        }

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(sb.toString().getBytes(ENCODING));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        StringBuilder hash = new StringBuilder();
        for (byte b : digest.digest()) {
            hash.append(String.format("%02x", b));
        }

        return new Item(hash.toString(), name, childList);
    }

    public static Item fromFile(File f){
        String sha1;
        try {
            sha1 = DIGEST.digest(f.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Item(sha1, f.getName(), null);
    }

    /**
     * Builds an Item from file name and hash
     * @param name file name
     * @param hash file hash
     * @return
     */
    public static Item file(String name, String hash){
        return new Item(hash, name, null);
    }

    public boolean isFile(){
        return children.isEmpty();
    }

    @Override
    public int compareTo(Item o) {
        int result = hash.compareTo(o.hash);
        if( 0 == result) result = name.compareTo(o.name);
        return result;
    }
}
