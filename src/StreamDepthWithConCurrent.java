import javax.management.relation.RoleUnresolved;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamDepthWithConCurrent extends StreamDepth{
    List<Set<Integer>> refBed;
    private int pool = 32;

    public StreamDepthWithConCurrent(String path, String redBedPath, int refIndex) {
        this.refBed = readDic(redBedPath, refIndex);

        List<String> files = readPath(path);

        ExecutorService es = Executors.newFixedThreadPool(this.pool);

        for (String file:files) {
                es.submit(new UsingSamtools(file, this.refBed));
        }
        es.shutdown();
    }

    public StreamDepthWithConCurrent(String path, String redBedPath, int refIndex, int pool) {
        if (pool > 10 && pool <= 40){
            this.pool = pool;
        }

        this.refBed = readDic(redBedPath, refIndex);


        List<String> files = readPath(path);

        ExecutorService es = Executors.newFixedThreadPool(this.pool);

        for (String file:files) {
                es.submit(new UsingSamtools(file, this.refBed));
        }
        es.shutdown();
    }

    public StreamDepthWithConCurrent(String path, int start, int end, String outPath, String redBedPath, int refIndex, int pool) {
        if (pool > 10 && pool <= 40){
            this.pool = pool;
        }
        this.refBed = readDic(redBedPath, refIndex);

        List<String> files = readPath(path,start,end);

        ExecutorService es = Executors.newFixedThreadPool(this.pool);

        for (String file:files) {
                es.submit(new UsingSamtools(file, this.refBed, outPath));
        }
        es.shutdown();
    }

    public List<String> readPath(String path){
        List<String> files = new ArrayList<>();
        File[] tempList = (new File(path)).listFiles();

        assert tempList != null;
        for (File file : tempList) {
            if (file.isFile() && file.toString().endsWith(".bam")) {
                files.add(file.toString());
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
        for (File file : tempList) {
            if (file.isFile() && file.toString().endsWith(".bam")) {
                int index = obtainNum(file.getName());
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

    public static void main(String[] args) {
        if (args.length == 3){
            new StreamDepthWithConCurrent(args[0], args[1], Integer.parseInt(args[2]));
        } else if (args.length == 4) {
            new StreamDepthWithConCurrent(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        } else if (args.length == 7) {
            new StreamDepthWithConCurrent(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]),args[3], args[4], Integer.parseInt(args[5]),Integer.parseInt(args[6]));
        } else {
            System.out.println("You need 3, 4 or 7 parameters, bamPath, refBedPath, refChrNum, and optional threads number, the default of which is 32");
        }
    }
}

class UsingSamtools implements Runnable{
    String file;
    List<Set<Integer>> refBed;
    String outPath = null;

    public UsingSamtools(String file, List<Set<Integer>> refBed){
        this.file = file;
        this.refBed = refBed;
    }

    public UsingSamtools(String file, List<Set<Integer>> refBed, String outPath){
        this.file = file;
        this.refBed = refBed;
        this.outPath = outPath;
    }

    @Override
    public void run() {
        String out = null;
        if (outPath != null){
            out = outPath + file.split("\\.")[0] + ".out";
        }else { out = file.split("\\.")[0] + ".out";}

        String command = "samtools depth -Q 20 " + file;
        String [] cmdArray ={"/bin/bash","-c", command};
        try{
            Process p =Runtime.getRuntime().exec(cmdArray,null);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedWriter bw = StreamDepth.BufferedFileWriter(out);
            String str;
            while ((str=br.readLine()) != null){
                if (!str.startsWith("0") && !str.startsWith("43") && !str.startsWith("44")){
                    String[] temp = str.split("\t");
                    if (this.refBed.get(Integer.parseInt(temp[0]) - 1).contains(Integer.parseInt(temp[1]))){
                        bw.write(str);
                        bw.write("\n");
                    }
                }
            }
            p.waitFor();
            bw.close();
            br.close();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}