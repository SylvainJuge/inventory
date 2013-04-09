package sylvain.juge.inventory;

import java.io.File;

public class Main {

    public static void main(String[] args){
        File file = new File(args[0]);
        if(!file.isDirectory()){
            throw new RuntimeException("expected folder name");
        }
        Index index = Index.fromFolder(file);
        System.out.println("index for folder "+file.getAbsolutePath());
        index.print(System.out);
    }

}
