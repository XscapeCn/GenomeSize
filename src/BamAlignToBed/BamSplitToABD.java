package BamAlignToBed;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;


public class BamSplitToABD extends StreamAlignWithCon{
    private static final Logger logger = Logger.getLogger(BamSplitToABD.class.getName());
    final int[] A = {1,2,7,8,13,14,19,20,25,26,31,32,37,38};
    final int[] B = {3,4,9,10,15,16,21,22,27,28,33,34,39,40};
    final int[] D = {5,6,11,12,17,18,23,24,29,30,35,36,41,42};
    HashSet<Integer> AA = new HashSet<>();
    HashSet<Integer> BB = new HashSet<>();
    HashSet<Integer> DD = new HashSet<>();
    String outFile;
    String path;
    List<String[]> out = new ArrayList<>();
    public static final Object lock = new Object();

    public BamSplitToABD(String path, int pool) {
        for (int j : A) this.AA.add(j);
        for (int j : B) this.BB.add(j);
        for (int j : D) this.DD.add(j);
        this.path = path;

        String[] files = readPath(path);
        for (String s : files) System.out.println(s);

        this.pool = checkPool(pool);

        ExecutorService es = Executors.newFixedThreadPool(this.pool);
        final CountDownLatch latch = new CountDownLatch(files.length);

        for (String file:files) {
            logger.info("The processing file is " + file);
//            System.out.println("The processing file is " + file);
            Thread t = new Thread(() -> compare(file, latch));
            es.submit(t);
        }

        try{
            latch.await();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            es.shutdown();
            writeOut();
            logger.info("EndEnd");
//            System.out.println("EndEnd");
        }
    }

    public String[] readPath(String path){
        if (path.endsWith("/")){
            List<String> files = new ArrayList<>();
            File[] tempList = (new File(path)).listFiles();
            assert tempList != null;
            for (File file : tempList) {
                if (file.isFile() && file.toString().endsWith(".bam")) {
                    files.add(file.getName());
                }
            }
            this.outFile = path + "ABDSplit.out";
            return files.toArray(new String[0]);
        }else if (path.endsWith(".bam")){
            this.outFile = path.split(".bam")[0] + ".out";
            return new String[]{path};
        }else {
            logger.warn("Invalid file input, input a path end with / or a file end with .bam");
//            System.out.println("Invalid file input, input a path end with / or a file end with .bam");
            System.exit(1);
            return null;
        }
    }

    public void compare(BufferedReader br){
        int ADepth = 0;
        int BDepth = 0;
        int DDepth = 0;

        System.out.println(ADepth);
        System.out.println(BDepth);
        System.out.println(DDepth);

        String str;
        try{
            while ((str = br.readLine()) != null){

                System.out.println(str);

                String[] temp = str.split("\t");
                int start = Integer.parseInt(temp[0]);
                if (this.AA.contains(start)){
                    ADepth += Integer.parseInt(temp[2]);
                    System.out.println("ADepth is " + ADepth);
                } else if (this.BB.contains(start)){
                    BDepth += Integer.parseInt(temp[2]);
                    System.out.println("BDepth is " + BDepth);
                } else if (this.DD.contains(start)){
                    DDepth += Integer.parseInt(temp[2]);
                }
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println(ADepth);
        System.out.println(BDepth);
        System.out.println(DDepth);
        System.out.println("EndEnd");
    }

    public void compare(String file, CountDownLatch latch){
        long ADepth = 0;
        long BDepth = 0;
        long DDepth = 0;

        try{
            StringBuilder command = new StringBuilder();
            command.append("samtools depth -Q 20 ").append(this.path).append(file);
//            String command = "samtools depth -Q 20 " + this.path + file;
            String [] cmdArray ={"/bin/bash","-c", command.toString()};
            Process p = Runtime.getRuntime().exec(cmdArray,null);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            logger.info("Start reading "+ file + " via BufferedRead.");
//            System.out.println("Start reading "+ file + " via BufferedRead.");

            String str;
            while ((str = br.readLine()) != null){

                String[] temp = str.split("\t");
                int start = Integer.parseInt(temp[0]);

                if (AA.contains(start)){
                    ADepth += Long.parseLong(temp[2]);
                } else if (BB.contains(start)){
                    BDepth += Long.parseLong(temp[2]);
                } else if (DD.contains(start)){
                    DDepth += Long.parseLong(temp[2]);
                }
            }
            br.close();
            p.waitFor();

        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        String[] res = new String[]{file, String.valueOf(ADepth), String.valueOf(BDepth), String.valueOf(DDepth), String.valueOf(ADepth + BDepth + DDepth)};

        synchronized (lock){
            this.out.add(res);
        }
        logger.info(file + " done.");
//        System.out.println(file + " done and the cost is " + (System.currentTimeMillis() - startTime));
        latch.countDown();
    }

    public void writeOut(){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
            for (String[] strings : out) {
                StringBuilder sb = new StringBuilder();
                sb.append(strings[0]).append("\t").append(strings[1]).append("\t").append(strings[2]).append("\t").append(strings[3]).append("\t").append(strings[4]).append("\n");
                bw.write(sb.toString());
            }
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {

//        logger.info("Remember source activate work");
        System.out.println("Remember source activate work");
        if (args.length != 2){
            logger.warn("This method require 2 parameters, one of which is a path or name of bam, another one is threads");
//            System.out.println("This method require 2 parameters, one of which is a path or name of bam, another one is threads");
            System.exit(1);
        }
        new BamSplitToABD(args[0], Integer.parseInt(args[1]));
    }
}

