import javax.management.relation.RoleUnresolved;
import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThread {
    public static void main(String[] args) throws InterruptedException {

//        List<Set<String>> bb = null;
//
//        WriteFile aa = new WriteFile("Aa", bb);
//        aa.run();
//        Thread aaa = new Thread(aa);
//        aaa.start();

//        RunnableDemo rd = ;


//        Thread th = new Thread(new RunnableDemo("ABC", 1));
//        th.start();

        int[] index = new int[]{1,2,3};
        for (int ii :
                index) {
//            RunnableDemo aa = new RunnableDemo("H:/Nature/2020PreExperiment/result/Supplement/wheat_v1.1_Lulab.gff3", ii);
//            Thread th = new Thread(aa);
//            th.start();
        }

        ExecutorService es = Executors.newFixedThreadPool(32);




    }
    public static void writeFileNormal(String filename1){
        long startTime2=System.currentTimeMillis();   //获取开始时间

        try{
            BufferedReader br = new BufferedReader(new FileReader(filename1));
            String filename2 = filename1.split("\\.sing\\.maf")[0] + ".test";
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename2));
            String str ;
            while((str = br.readLine()) != null){
                bw.write(str);
                bw.newLine();
            }
            bw.close();
            br.close();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }

        long endTime2=System.currentTimeMillis(); //获取结束时间

        System.out.println("程序运行时间： "+(endTime2-startTime2)+"ms");
    }
}

//class ThreadWriteFile  extends Thread implements Runnable{
//    String filename1;
////    String filename2;
//
//    public ThreadWriteFile(String filename1){
//        this.filename1 = filename1;
////        this.filename2 = filename2;
//    }
//
//    @Override
//    public void run() {
//        long start = System.currentTimeMillis();
//        try{
//            BufferedReader br = new BufferedReader(new FileReader(this.filename1));
//            String filename2 = this.filename1.split("\\.sing\\.maf")[0] + ".test";
//            BufferedWriter bw = new BufferedWriter(new FileWriter(filename2));
//            String str ;
//            while((str = br.readLine()) != null){
//                bw.write(str);
//                bw.newLine();
//            }
//            bw.close();
//            br.close();
//        }catch (Exception e){
//            e.printStackTrace();
//            System.exit(1);
//        }
//        long end = System.currentTimeMillis();
//        System.out.println("Start of " + filename1 + " is: " + start);
//        System.out.println("End of " + filename1 + " is: " + end);
//        System.out.println("===============");
//
//    }
//
//}


class HelloThread extends Thread {
    public volatile boolean running = true;
    public void run() {
        int n = 0;
        while (running) {
            n ++;
            System.out.println(n + " hello!");
        }
        System.out.println("end!");
    }
}

//class RunnableDemo implements Runnable{
//    String filename;
//    int index;
//
//    public RunnableDemo(String aa, int index){
//        this.filename = aa;
//        this.index = index;
//    }
//     public void run(){
//         System.out.println("It is the " + index + " time to launch the thread");
//         String out = filename.split("\\.gff")[0] + index + ".txt";
//         try{
//             BufferedWriter bw = new BufferedWriter(new FileWriter(out));
//             BufferedReader br = new BufferedReader(new FileReader(filename));
//             String str;
//             while((str = br.readLine()) != null){
//                 bw.write(str);
//                 bw.write("\n");
//                 bw.write(str);
//                 bw.write("\n");
//             }
//             bw.close();
//             br.close();
//         }catch(Exception e){
//             e.printStackTrace();
//             System.exit(1);
//         }
//         System.out.println("It is the " + index + " time to end the thread");
//
//     }
//
//}


class WriteFile implements Runnable{
    private String file;
    private List<Set<String>> refBed;

    public WriteFile(String file, List<Set<String>> refBed){
        this.file = file;
        this.refBed = refBed;
    }

    @Override
    public void run() {
        String out = file.split("\\.")[0] + ".out";

        String command = "samtools depth -Q 20 " + file;
        String [] cmdArray ={"/bin/bash","-c", command};

        try {
            BufferedWriter bw = null;
            Process p =Runtime.getRuntime().exec(cmdArray,null);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            bw = StreamDepth.BufferedFileWriter(out);
            String str;
            while ((str=br.readLine()) != null){
                if (!str.startsWith("0") && !str.startsWith("43") && !str.startsWith("44")){
                    String[] temp = str.split("\t");
                    if (refBed.get(Integer.parseInt(temp[0]) - 1).contains(Integer.parseInt(temp[1]))){
                        bw.write(str);
                        bw.write("\n");
                    }
                }
            }
            p.waitFor();
            bw.close();
            br.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}