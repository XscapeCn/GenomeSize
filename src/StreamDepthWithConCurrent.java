import javax.management.relation.RoleUnresolved;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StreamDepthWithConCurrent extends StreamDepth{
    List<Set<Integer>> refBed;
    private int pool = 32;

    public StreamDepthWithConCurrent(String path, String redBedPath, int refIndex, int pool) {
        if (pool > 10 && pool <= 40){
            this.pool = pool;
        }

        List<String> files = new ArrayList<>();
        File[] tempList = (new File(path)).listFiles();

        this.refBed = readDic(redBedPath, refIndex);

        assert tempList != null;
        for (File file : tempList) {
            if (file.isFile() && file.toString().endsWith(".bam")) {
                files.add(file.toString());
//                System.out.println(file.toString());
            }
        }

        ExecutorService es = Executors.newFixedThreadPool(this.pool);

        for (String file:files) {
            try {
                es.submit(new RunnableDemo(file, this.refBed));
            }catch(Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
        es.shutdown();
    }

    public StreamDepthWithConCurrent(String path, String redBedPath, int refIndex) {
        List<String> files = new ArrayList<>();
        File[] tempList = (new File(path)).listFiles();

        this.refBed = readDic(redBedPath, refIndex);

        assert tempList != null;
        for (File file : tempList) {
            if (file.isFile() && file.toString().endsWith(".bam")) {
                files.add(file.toString());
            }
        }

        ExecutorService es = Executors.newFixedThreadPool(this.pool);

        for (String file:files) {
            try {
                es.submit(new RunnableDemo(file, this.refBed));
            }catch(Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
        es.shutdown();
    }

    public static void main(String[] args) {
        if (args.length == 3){
            new StreamDepthWithConCurrent(args[0], args[1], Integer.parseInt(args[2]));
        }else if (args.length == 4){
            new StreamDepthWithConCurrent(args[0], args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        }else {
            System.out.println("You need 3 or 4 parameters, bamPath, refBedPath, refChroNum, and optional threads number, the default of which is 32");
        }
    }
}

class RunnableDemo implements Runnable{
    String file;
    List<Set<Integer>> refBed;

    public RunnableDemo(String file, List<Set<Integer>> refBed){
        this.file = file;
        this.refBed = refBed;
    }

    @Override
    public void run() {
        String out = file.split("\\.")[0] + ".out";

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


