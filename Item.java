import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class Item {

    private String hash;
    private String name;
    private List<Item> children;

    public String getHash(){ return hash; }
    public String getName(){ return name; }
    public List<Item> getChildren(){ return children; }

    public boolean isFile(){
        return children.isEmpty();
    }

    private Item(String hash, String name, List<Item> children){
        this.hash = hash;
        this.name = name;
        this.children = null != children ? children : new ArrayList<Item>();
    }

    public static Item tree(String name, List<Item> children){
        return new Item(null, name, children);
    }

    public static Item fromFile(File f){
        return new Item(Sha1.compute(f), f.getName(), null);
    }

    public static Item root(){
        return new Item(null, null, null);
    }

}
