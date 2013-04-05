import java.io.File;
import java.util.List;
import java.util.ArrayList;

public class TreeItem {

    private String hash;
    private String name;
    private List<TreeItem> children;

    public String getHash(){ return hash; }
    public String getName(){ return name; }
    public List<TreeItem> getChildren(){ return children; }

    public boolean isFile(){
        return children.isEmpty();
    }

    private TreeItem(String hash, String name, List<TreeItem> children){
        this.hash = hash;
        this.name = name;
        this.children = null != children ? children : new ArrayList<TreeItem>();
    }

    public static TreeItem tree(String name, List<TreeItem> children){
        return new TreeItem(null, name, children);
    }

    public static TreeItem fromFile(File f){
        return new TreeItem(Sha1.compute(f), f.getName(), null);
    }

    public static TreeItem root(){
        return new TreeItem(null, null, null);
    }

}
