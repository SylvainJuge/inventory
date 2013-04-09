package sylvain.juge.inventory;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Index {
    private List<Item> rootItems;
    private List<Item> items;

    private Index(){
        this.items = new ArrayList<>();
        this.rootItems = new ArrayList<>();
    }
    public List<Item> getRootItems(){ return rootItems; }
    public List<Item> getItems(){ return items; }

    public void print(PrintStream out){
        Deque<Item> stack = new ArrayDeque<>();
        for(int i=(rootItems.size()-1);0<=i;i--){
            stack.add(rootItems.get(i));
        }
        while( !stack.isEmpty()){
            Item item = stack.removeFirst();
            out.println(String.format("%s | %s", item.getHash(), item.getName()));
            if(!item.isFile()){
                List<Item> children = item.getChildren();
                for(int i=(children.size()-1);0<=i;i--){
                    stack.addFirst(children.get(i));
                }
            }
        }
    }

    public static Index fromFolder(File folder){
        Index result = new Index();
        for(File child:folder.listFiles()){
            result.rootItems.add(result.addRecursively(child));
        }
        return result;
    }

    public Item addRecursively(File child){
        String hash = null;
        Item item = null;
        if(!child.isDirectory()){
            item = Item.fromFile(child) ;
        } else {
            List<Item> childItems = new ArrayList<>();
            for(File f:child.listFiles()){
                childItems.add(addRecursively(f));
            }
            item = Item.tree(child.getName(), childItems);
        }
        items.add(item);
        return item;
    }
}
