package sylvain.juge.inventory;


import java.io.File;
import java.io.PrintStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Prototype {


    private static class IndexEntry {
        String hash;
        Path path;
    }

    private static class Index {

        private final static String INDEX_FILE_NAME = ".inventory";
        private final Path root;

        private Index(Path root){
            this.root = root;
        }

        /**
         * creates a new empty index in folder
         * @param path folder to index (and where index file is stored)
         * @return empty index
         */
        public static Index newIndex(Path path){
            return new Index(path);
        }

        /**
         * loads an existing index in directory
         * @param path folder where the index file is stored
         */
        public static Index loadIndex(Path path){
            return null;
        }

        /** refresh the whole index with filesystem */
        public void refresh(){

        }



        public List<FileEntry> getEntries(){
            return null;
        }
    }

    public static void main(String[] args) {

        Path projectDir = getProjectDir(Prototype.class);

        Index index = Index.newIndex();


        // add items to index
        // store index into file
        // load index from file

        /*
        long start = System.currentTimeMillis();
        OldIndex index = OldIndex.fromFolder(Paths.get(args[0]));
        long end = System.currentTimeMillis();

        long total = end - start;
        System.out.println(String.format("%d seconds"));


        //index.print(System.out);


        System.out.println(" -- duplicated files --");
        for (FileEntry fe : index.getDuplicated(2)) {
            System.out.println(fe.getHash());
            for (String path : fe.getInstancesPaths()) {
                System.out.println("  " + path);
            }
        }
        */

    }

    private static Path getProjectDir(Class<?> type) {
        URL classUrl = type.getClassLoader().getResource(type.getName() + ".class");
        // TODO : retrieve current project folder
        return null;
    }

    private static final class IndexStatistics {
        private long bytesCount;
        private long startTime;
        private long totalTimeMs;

        public void start(long startMs){
            startTime = startMs;

        }
        public void end(long endMs){
            if( endMs < startTime){
                throw new RuntimeException("end time must be greater than or equal to start time");
            }
            totalTimeMs = endMs - startTime;
        }
        public void addBytes(int count){
            if( count < 0){
                throw new RuntimeException("byte count must be greater or equal to zero");
            }
            bytesCount += count;
        }

        public long getTotalTime(){
            return totalTimeMs;
        }

        public long getBytesCount(){
            return bytesCount;
        }

        // TODO : pretty-print total speed

        @Override
        public String toString(){
            return "";
        }
    }

    private static final class OldIndex {

        private HashMap<String,FileEntry> content;

        private OldIndex(){
            content = new HashMap<>();
        }

        public static OldIndex fromFolder(Path folder){
            if(!Files.isDirectory(folder)){
                throw new RuntimeException(String.format("%s is not a readable directory",folder));
            }
            OldIndex index = new OldIndex();
            // stack of folders to explore, used as DFS
            Deque<File> stack = new LinkedList<>();
            stack.add(folder.toFile());
            while (!stack.isEmpty()) {
                File fold = stack.removeFirst();
                for (File file : fold.listFiles()) {
                    if(file.isDirectory()){
                        stack.addFirst(file);
                    } else if(file.isFile()){
                        index.addFile(file);
                    }

                }
            }
            return index;
        }

        private void addFile(File f) {
            String hash = Sha1.compute(f);
            FileEntry entry = content.get(hash);
            String path = f.getAbsolutePath();
            if( null == entry){
                entry = new FileEntry(hash, path);
                content.put(hash,entry);
            } else {
                entry.addInstance(path);
            }
        }

        public void print(PrintStream out) {
            for(Map.Entry<String,FileEntry> e:content.entrySet()){
                out.println(e.getKey());
                for(String path:e.getValue().instances){
                    out.print("  ");
                    out.println(path);
                }
            }
        }

        public List<FileEntry> getDuplicated(int i) {
            List<FileEntry> result = new ArrayList<>();
            for(Map.Entry<String,FileEntry> e:content.entrySet()){
                if(e.getValue().getInstancesCount() == i){
                    result.add(e.getValue());
                }
            }
            return result;
        }
    }

    private static final class FileEntry {
        private final String hash; // file content hash
        private final Set<String> instances; // file instances

        private FileEntry(String hash, String path){
            this.hash = hash;
            this.instances = new HashSet<>();
            this.instances.add(path);
        }

        public void addInstance(String path){
            this.instances.add(path);
        }

        public int getInstancesCount(){ return instances.size(); }
        public List<String> getInstancesPaths(){ return new ArrayList<>(instances); }

        public String getHash() {
            return hash;
        }
    }
}
