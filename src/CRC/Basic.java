package CRC;

import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Basic {
    static final Logger logger = Logger.getLogger(Basic.class.getName());
    public static final Object lock = new Object();
    List<String> res = new ArrayList<>();
    Options options = new Options();
    List<String> files;
    String outFile;
    String path;
    int pool;
    int sf;
    int ef;

    public void createOptions() {
        options = new Options();
        options.addOption("sf", true, "The index of start file, optional");
        options.addOption("ef", true, "The index of end file, optional");
        options.addOption("t", true, "Thread, default to 32, optional");
        options.addOption("b", true, "Bed file location");
        options.addOption("p", true, "Bam file location");
        options.addOption("o", true, "Out file location, optional");
        options.addOption("r", true, "Bp per read");
    }

    public int checkPool(int pool){
        if ( 5 <= pool && pool <= 64){
            logger.info("Threads will be set as " + pool);
            return pool;
        }else {
            logger.warn("Invalid thread input, it will default to 32");
            return 32;
        }
    }

    public List<String> readPath(String path){
        List<String> files = new ArrayList<>();
        if (path.endsWith(".bam")){
            files.add(path);
        }else {
            File[] tempList = (new File(path)).listFiles();
            assert tempList != null;
            for (File file : tempList) {
                if (file.isFile() && file.toString().endsWith(".bam")) {
                    files.add(file.getName());
                }
            }
        }
        return files;
    }

    public List<String> readPath(String path, int start, int end){
        List<Integer> range = IntStream.rangeClosed(start, end)
                .boxed().collect(Collectors.toList());
        List<String> files = new ArrayList<>();
        File[] tempList = (new File(path)).listFiles();
        assert tempList != null;
        int index;
        for (File file : tempList) {
            if (file.isFile() && file.toString().endsWith(".bam")) {
                index = obtainNum(file.getName());
                if (range.contains(index)){
                    files.add(file.getName());
                }
            }
        }
        return files;
    }

    public int obtainNum(String content) {
        String regEx="[^0-9]+";
        Pattern pattern = Pattern.compile(regEx);
        String[] cs = pattern.split(content);
        return Integer.parseInt(String.join("", cs));
    }

    public void writeOut(){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
            for (String strings : res) {
                bw.write(strings);
                bw.write("\n");
            }
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}
