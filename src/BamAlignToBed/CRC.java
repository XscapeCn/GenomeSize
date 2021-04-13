package BamAlignToBed;
import org.apache.commons.cli.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CRC {
    private static final Logger logger = Logger.getLogger(BamSplitToABD.class.getName());
    final int[] A = {1,2,7,8,13,14,19,20,25,26,31,32,37,38};
    final int[] B = {3,4,9,10,15,16,21,22,27,28,33,34,39,40};
    final int[] D = {5,6,11,12,17,18,23,24,29,30,35,36,41,42};
    HashSet<String> AA = new HashSet<>();
    HashSet<String> BB = new HashSet<>();
    HashSet<String> DD = new HashSet<>();
    List<String> files;
    String outFile;
    String path;
    String bed;
    int reads;
    int pool;
    int sf;
    int ef;
    List<String> res = new ArrayList<>();
    List<Set<Integer>> refBed;
    public static final Object lock = new Object();
    Options options = new Options();

    public CRC(String[] args){
        createOptions();
        retrieveParameters(args);
        initialize();
        concurrent();
        writeOut();
    }

    public void initialize(){
        for (int a:A) {AA.add(String.valueOf(a));}
        for (int b:B) {BB.add(String.valueOf(b));}
        for (int d:D) {DD.add(String.valueOf(d));}

        if (sf < 0){
            files = readPath(this.path);
        } else {
            files = readPath(this.path, sf, ef);
        }

        this.refBed = readDic(bed);
    }

    public void createOptions() {
        options = new Options();
        options.addOption("sf", true, "The index of start file, optional");
        options.addOption("ef", true, "The index of end file, optional");
        options.addOption("t", true, "Thread, default to 32, optional");
        options.addOption("b", true, "Bed file location");
        options.addOption("p", true, "Bam file location");
        options.addOption("o", true, "Out file location, optional");
        options.addOption("r", true, "Out file location, optional");
    }

    public void retrieveParameters(String[] args) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter optionFormat = new HelpFormatter();
        try {
            CommandLine cmd = parser.parse(options, args);
            if (cmd.hasOption("p") && cmd.hasOption("r") && cmd.hasOption("b")){
                path = cmd.getOptionValue("p");
                bed = cmd.getOptionValue("b");
                reads = Integer.parseInt(cmd.getOptionValue("r"));

            }else {
                optionFormat.printHelp("CRC.jar", options );
                System.exit(0);
            }

            if (cmd.hasOption("t")){
                this.pool = checkPool(Integer.parseInt(cmd.getOptionValue("t")));
            }else {this.pool = 32;}

            if (cmd.hasOption("o")){
                outFile = cmd.getOptionValue("o");
            }else {outFile = "./ABDSplit.out";}

            if (cmd.hasOption("sf")){
                sf = Integer.parseInt(cmd.getOptionValue("sf"));
                ef = Integer.parseInt(cmd.getOptionValue("ef"));
            }else {
                sf = -1;
                ef = -1;
            }
        }
        catch(Exception e) {
            optionFormat.printHelp("CRC.jar", options );
//            e.printStackTrace();
            System.exit(0);
        }
    }

    List<Set<Integer>> readDic(String filename){
        List<Set<Integer>> res = new ArrayList<>();
        for (int i = 0; i < 42; i++) {
            res.add(new HashSet<Integer>()
            );
        }
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String str ;
            while ((str=br.readLine()) != null){
                String[] temp = str.split("\t");
                res.get(Integer.parseInt(temp[0]) -1).add(Integer.parseInt(temp[1]));
            }
            br.close();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        return res;
    }

    public int checkPool(int pool){
        if ( 10 <= pool && pool <= 40){
            System.out.println("Threads will be set as " + pool);
            return pool;
        }else {
            System.out.println("Invalid thread input, it will default to 32");
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
                    files.add(file.toString());
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

    public void writeOut(){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
            for (String strings : res) {
//                sb.append(strings[0]).append("\t").append(strings[1]).append("\t").append(strings[2]).append("\t").append(strings[3]).append("\t").append(strings[4]).append("\n");
                bw.write(strings);
                bw.write("\n");
            }
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void concurrent(){
        final CountDownLatch latch = new CountDownLatch(files.size());
        ExecutorService es = Executors.newFixedThreadPool(this.pool);

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
        }
    }

    public List<Long> nRow(String file){
        long AR = 0;
        long BR = 0;
        long DR = 0;
        List<Long> res = new ArrayList<>();
        StringBuilder cmd = new StringBuilder();
        cmd.append("samtools view ").append(file);
        String [] cmdArray ={"/bin/bash","-c", cmd.toString()};
        try{
            Process p = Runtime.getRuntime().exec(cmdArray,null);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str = null;
            long count = 0;


            while ((str = br.readLine()) != null){
                if (count == 100){
//                    System.out.println(str);
                    logger.info("Start calculate nRow, " + file);
                }
                count += 1;
                String temp = str.split("\t")[2];
//                System.out.println(temp);

                if (AA.contains(temp)){
                    AR += 1;
                } else if (BB.contains(temp)){
                    BR += 1;
                } else if (DD.contains(temp)){
                    DR += 1;
                }
            }
            res.add(AR);
            res.add(BR);
            res.add(DR);
            res.add(count);

            br.close();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        return res;
    }

    public void compare(String file, CountDownLatch latch){
//        long ADepth = 0;
//        long BDepth = 0;
//        long DDepth = 0;
        List<Long> longs = nRow(file);
        long ADepth = longs.get(0);
        long BDepth = longs.get(1);
        long DDepth = longs.get(2);
        long count = longs.get(3);


        long ACol = 0;
        long BCol = 0;
        long DCol = 0;

        long AC =0;
        long BC =0;
        long DC =0;

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

                int startInt = Integer.parseInt(temp[0]);
                if (startInt < 1 || startInt > 42) continue;

                String start = temp[0];
                int fst = Integer.parseInt(temp[1]);
                int sec = Integer.parseInt(temp[2]);

                if (AA.contains(start)){
//                    ADepth += sec;
//                    ACol += 1;
                    if (this.refBed.get(startInt - 1).contains(fst)){
                        AC += sec;
                    }
                } else if (BB.contains(start)){
//                    BDepth += sec;
//                    BCol += 1;
                    if (this.refBed.get(startInt - 1).contains(fst)){
                        BC += sec;
                    }
                } else if (DD.contains(start)){
//                    DDepth += sec;
//                    DCol += 1;
                    if (this.refBed.get(startInt - 1).contains(fst)){
                        DC += sec;
                    }
                }
            }
            br.close();
            p.waitFor();

        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        double AR = ((double) AC)/ADepth;
        double BR = ((double) BC)/BDepth;
        double DR = ((double) DC)/DDepth;
        double SR = ((double) (AC + BC + DC))/(count * reads);

        StringBuilder sb = new StringBuilder();
        sb.append(file)
                .append("\t").append(AC).append("\t").append(BC).append("\t").append(DC).append("\t").append(AC + BC + DC)
                .append("\t").append(ADepth).append("\t").append(BDepth).append("\t").append(DDepth).append("\t").append(count)
                .append("\t").append(AR).append("\t").append(BR).append("\t").append(DR).append("\t").append(SR);

//        String[] res = new String[]{file, String.valueOf(ADepth), String.valueOf(BDepth), String.valueOf(DDepth), String.valueOf(ADepth + BDepth + DDepth),String.valueOf(ACol), String.valueOf(BCol), String.valueOf(DCol), String.valueOf(ACol + BCol + DCol)};

        synchronized (lock){
//            br.write(sb.toString());
            this.res.add(sb.toString());
        }
        logger.info(file + " done.");
//        System.out.println(file + " done and the cost is " + (System.currentTimeMillis() - startTime));
        latch.countDown();
    }

    public static void main(String[] args) {
        CRC crc = new CRC(args);
//        System.out.println(crc.path);
//        System.out.println(crc.pool);
//        for (int a: crc.AA) { System.out.println(a); }
    }
}