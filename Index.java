import java.io.File;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Index {
    private TreeItem root;
    private List<TreeItem> items;

    private Index(){
        this.root = TreeItem.root();
        this.items = new ArrayList<>();
    }
    public TreeItem getRoot(){ return root; }
    public List<TreeItem> getChildren(){ return items; }

    public void print(PrintStream out){
        Deque<TreeItem> stack = new ArrayDeque<>();
        stack.add(root);
        while( !stack.isEmpty()){
            TreeItem item = stack.removeFirst();
            if(item.isFile()){
                out.println(String.format("%s | %s", item.getHash(), item.getName()));
            } else {
                List<TreeItem> children = item.getChildren();
                for(int i=(children.size()-1);0<=i;i--){
                    stack.addFirst(children.get(i));
                }
            }
        }
    }

    public static Index fromFolder(File folder){
        Index result = new Index();
        for(File child:folder.listFiles()){
            result.root.getChildren().add(result.addRecursively(child));
        }
        return result;
    }

    public TreeItem addRecursively(File child){
        String hash = null;
        TreeItem item = null;
        if(!child.isDirectory()){
            item = TreeItem.fromFile(child) ;
        } else {
            List<TreeItem> childItems = new ArrayList<>();
            for(File f:child.listFiles()){
                childItems.add(addRecursively(f));
            }
            item = TreeItem.tree(child.getName(), childItems);
        }
        items.add(item);
        return item;
    }
}
