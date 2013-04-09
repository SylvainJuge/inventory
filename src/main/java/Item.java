import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class Item {

    private static final char FIELD_SEPARATOR = '\n';

    private final String hash;
    private final String name;
    private final List<Item> children;

    public String getHash(){ return hash; }
    public String getName(){ return name; }
    public List<Item> getChildren(){ return children; }

    private Item(String hash, String name, List<Item> children){
        this.hash = hash;
        this.name = name;
        this.children = null != children ? children : new ArrayList<Item>();
    }

    public static Item tree(String name, List<Item> children){
        StringBuilder sb = new StringBuilder(name);
        sb.append(FIELD_SEPARATOR);
        for(Item i:children){
            sb.append(FIELD_SEPARATOR);
            sb.append(i.getHash());
            sb.append(FIELD_SEPARATOR);
            sb.append(i.getName());
        }
        return new Item(Sha1.compute(sb.toString()), name, children);
    }

    public static Item fromFile(File f){
        return new Item(Sha1.compute(f), f.getName(), null);
    }

    public boolean isFile(){
        return children.isEmpty();
    }

}
