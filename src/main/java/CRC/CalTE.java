package CRC;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CalTE extends Basic{
    public CalTE(String[] args){
        createOptions();
        retrieveParameters(args);
        initialize();
        concurrent();
        writeOut();
    }

    public void initialize(){
        if (!path.endsWith("/")) path += "/";

        if (sf < 0){
            files = readPath(this.path);
        } else {
            files = readPath(this.path, sf, ef);
        }
    }

    public void retrieveParameters(String[] args) {
        CommandLineParser parser = new DefaultParser();
        HelpFormatter optionFormat = new HelpFormatter();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("p")){
                path = cmd.getOptionValue("p");
            }else {
                optionFormat.printHelp("CRC.jar", options );
                System.exit(1);
            }

            if (cmd.hasOption("t")){
                this.pool = checkPool(Integer.parseInt(cmd.getOptionValue("t")));
            }else {this.pool = 32;}

            if (cmd.hasOption("o")){
                outFile = cmd.getOptionValue("o");
            }else {outFile = "./TERatio.out";}

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

    public File[] setFq(String filename){
        String out1 = filename.split(".bam")[0] + ".1.fastq";
        String out2 = filename.split(".bam")[0] + ".2.fastq";
        StringBuilder cmd = new StringBuilder();
        cmd.append("java -Xmx2g -jar ~/jarjarjar/picard.jar SamToFastq -I ").append(filename).append(" -F ").append(out1).append(" -F2 ").append(out2);
        String [] cmdArray ={"/bin/bash","-c", cmd.toString()};
        try{
            Process p = Runtime.getRuntime().exec(cmdArray,null);
            p.waitFor();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        return new File[]{new File(out1), new File(out2)};
    }

    public int[] getOut(File[] fq){
        int DHH = 0;
        int DTA = 0;
        int DTB = 0;
        int DTH = 0;
        int DTT = 0;
        int DXX = 0;
        int DTC = 0;
        int DTM = 0;
        int DTX = 0;
        int RLC = 0;
        int RLX = 0;
        int RIX = 0;
        int RLG = 0;
        int RSX = 0;
        int count =0;

        StringBuilder cmd = new StringBuilder();
        cmd.append("bowtie2 -p 8 -x /data1/home/songxu/task/20210224GenomeSize/TriticumTE/Triticum -1 ").append(fq[0].getName()).append(" -2 ").append(fq[1].getName());
        String [] cmdArray ={"/bin/bash","-c", cmd.toString()};
        try{
            Process p = Runtime.getRuntime().exec(cmdArray,null);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            logger.info(fq[0].getName() + " " + fq[1].getName() + " start via Bowtie2");

            String str;
            String[] temp;
            String te;

            while( (str = br.readLine()) != null){
                if (!str.contains("@SQ")){
                    count += 1;
                }

                if (str.contains("100M")){
                    temp = str.split("\t");
                    te = temp[2].substring(0,3);
                    switch(te)
                    {
                        case "DHH" :
                            DHH += 1;
                            break;
                        case "DTA" :
                            DTA += 1;
                            break;
                        case "DTB" :
                            DTB += 1;
                            break;
                        case "DTC" :
                            DTC += 1;
                            break;
                        case "DTH" :
                            DTH += 1;
                            break;
                        case "DTM" :
                            DTM += 1;
                            break;
                        case "DTT" :
                            DTT += 1;
                            break;
                        case "DTX" :
                            DTX += 1;
                            break;
                        case "DXX" :
                            DXX += 1;
                            break;
                        case "RIX" :
                            RIX += 1;
                            break;
                        case "RLC" :
                            RLC += 1;
                            break;
                        case "RLG" :
                            RLG += 1;
                            break;
                        case "RLX" :
                            RLX += 1;
                            break;
                        case "RSX" :
                            RSX += 1;
                            break;
                    }
                }
            }

            br.close();

            for (File file:fq){
                file.delete();
            }

        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        return new int[]{DHH,DTA,DTB,DTC,DTH,DTM,DTT,DTX,DXX,RIX,RLC,RLG,RLX,RSX,count};
    }

    public void calTE(String file, CountDownLatch latch){
        int[] out = getOut(setFq(file));
        int count = out[out.length - 1];
        StringBuilder sb = new StringBuilder();
        sb.append(file).append("\t");
        for (int i: out) {
            sb.append(i).append("\t");
        }
        for (int i: out) {
            sb.append((double) i / count).append("\t");
        }
        res.add(sb.toString());
        logger.info(file + " done.");
        latch.countDown();

    }

    public void concurrent(){
        final CountDownLatch latch = new CountDownLatch(files.size());
        ExecutorService es = Executors.newFixedThreadPool(this.pool);

        for (String file:files) {
            logger.info("The processing file is " + file);
            Thread t = new Thread(() -> calTE(file, latch));
            es.submit(t);

            try {
                Thread.sleep(600000);
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
}