package CRC;
import org.apache.commons.cli.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CRC extends Basic{
    HashSet<String> AA = new HashSet<>();
    HashSet<String> BB = new HashSet<>();
    HashSet<String> DD = new HashSet<>();
    String bed;
    int reads;

    List<HashSet<Integer>> refBed;

    public CRC(String[] args){
        createOptions();
        retrieveParameters(args);
        initialize();
        concurrent();
        writeOut();
    }

    public void initialize(){
        int[] A = {1,2,7,8,13,14,19,20,25,26,31,32,37,38};
        int[] B = {3,4,9,10,15,16,21,22,27,28,33,34,39,40};
        int[] D = {5,6,11,12,17,18,23,24,29,30,35,36,41,42};

        for (int a:A) {AA.add(String.valueOf(a));}
        for (int b:B) {BB.add(String.valueOf(b));}
        for (int d:D) {DD.add(String.valueOf(d));}
        if (!path.endsWith("/")) path += "/";

        if (sf < 0){
            files = readPath(this.path);
        } else {
            files = readPath(this.path, sf, ef);
        }
        this.refBed = readDic(bed);
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
            System.exit(0);
        }
    }

    List<HashSet<Integer>> readDic(String filename){
        List<HashSet<Integer>> res = new ArrayList<>();
        for (int i = 0; i < 42; i++) {
            res.add(new HashSet<Integer>());
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



    public void concurrent(){
        final CountDownLatch latch = new CountDownLatch(files.size());
        ExecutorService es = Executors.newFixedThreadPool(this.pool);

        for (String file:files) {
            logger.info("The processing file is " + file);
//            Thread t = new Thread(() -> calTE(file, latch));
            Thread t = new Thread(() -> compare(file, latch));
            es.submit(t);

            //specify for the calTE
            try {
//                Thread.sleep(600000);
            }catch (Exception e){
                e.printStackTrace();
                System.exit(1);
            }


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
        cmd.append("samtools view ").append(path).append(file);
        String [] cmdArray ={"/bin/bash","-c", cmd.toString()};
        try{
            Process p = Runtime.getRuntime().exec(cmdArray,null);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String str;
            String temp;
            long count = 0;
            logger.info("Start calculate nRow, " + file);
            while ((str = br.readLine()) != null){
                count += 1;
                temp = str.split("\t")[2];

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
        List<Long> longs = nRow(file);
        long ADepth = longs.get(0);
        long BDepth = longs.get(1);
        long DDepth = longs.get(2);
        long count = longs.get(3);

        long AC =0;
        long BC =0;
        long DC =0;

        try{
            StringBuilder command = new StringBuilder();
            command.append("samtools depth -Q 20 ").append(path).append(file);
            String [] cmdArray ={"/bin/bash","-c", command.toString()};
            Process p = Runtime.getRuntime().exec(cmdArray,null);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            logger.info("Start reading "+ file + " via BufferedRead.");

            String str;
            int startInt;
            String[] temp;
            String start;
            while ((str = br.readLine()) != null){
                temp = str.split("\t");
                startInt = Integer.parseInt(temp[0]);

                if (startInt < 1 || startInt > 42) continue;

                start = temp[0];
                if (this.refBed.get(startInt - 1).contains(Integer.parseInt(temp[1]))){
//                    int sec = Integer.parseInt(temp[2]);
                    if (AA.contains(start)) {
                        AC += Integer.parseInt(temp[2]);
                    } else if (BB.contains(start)){
                        BC += Integer.parseInt(temp[2]);
                    }else if (DD.contains(start)){
                        DC += Integer.parseInt(temp[2]);
                    }
                }
            }
            br.close();
            p.waitFor();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        double AR = ((double) AC)/ADepth/reads, BR = ((double) BC)/BDepth/reads, DR = ((double) DC)/DDepth/reads, SR = ((double) (AC + BC + DC))/(count * reads);

        StringBuilder sb = new StringBuilder();
        sb.append(file)
                .append("\t").append(AC).append("\t").append(BC).append("\t").append(DC).append("\t").append(AC + BC + DC)
                .append("\t").append(ADepth).append("\t").append(BDepth).append("\t").append(DDepth).append("\t").append(count)
                .append("\t").append(AR).append("\t").append(BR).append("\t").append(DR).append("\t").append(SR);

        synchronized (lock){
            this.res.add(sb.toString());
        }
        logger.info(file + " done.");
        latch.countDown();
    }
}