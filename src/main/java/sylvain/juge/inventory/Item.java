package sylvain.juge.inventory;

import sylvain.juge.inventory.util.ReadOnlyList;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class Item implements Comparable<Item> {

    private static final char FIELD_SEPARATOR = '\n';

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
        return new Item(Sha1.compute(sb.toString()), name, childList);
    }

    public static Item fromFile(File f){
        return new Item(Sha1.compute(f), f.getName(), null);
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
