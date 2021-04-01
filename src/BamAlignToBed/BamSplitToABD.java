package BamAlignToBed;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BamSplitToABD extends StreamAlignWithCon{

    final int[] A = {1,2,7,8,13,14,19,20,25,26,31,32,37,38};
    final int[] B = {3,4,9,10,15,16,21,22,27,28,33,34,39,40};
    final int[] D = {5,6,11,12,17,18,23,24,29,30,35,36,41,42};
    HashSet<Integer> AA = new HashSet<>();
    HashSet<Integer> BB = new HashSet<>();
    HashSet<Integer> DD = new HashSet<>();
//    final HashSet BB = new HashSet(Collections.singletonList(B));
//    final HashSet DD = new HashSet(Collections.singletonList(D));

//    public static void main(String[] args) {
//        BamSplitToABD bast = new BamSplitToABD();
//        System.out.println(bast.AA.contains(1));
//    }

    public BamSplitToABD(){
        for (int j : A) {
            AA.add(j);
        }
        for (int j : B) {
            BB.add(j);
        }
        for (int j : D) {
            DD.add(j);
        }
    }

    public BamSplitToABD(String path, int pool) {
        for (int j : A) {
            this.AA.add(j);
        }
        for (int j : B) {
            this.BB.add(j);
        }
        for (int j : D) {
            this.DD.add(j);
        }

        String[] files = readPath(path);
        for (int i = 0; i < files.length; i++) {
            System.out.println(
                    files[i]
            );
        }

        this.pool = checkPool(pool);

//        ExecutorService es = Executors.newFixedThreadPool(this.pool);

//        for (String file:files) {
//            System.out.println("The processing file is " + file);
//            es.submit(new Thread(
//                    () -> this.compare(readBamViaStream( file ))
//            ));
//        }

        for (String file:files){
//            compare(readBamViaStream(file));
            compare(file);
        }
//        es.shutdown();
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

    public void compare(String file){
        int ADepth = 0;
        int BDepth = 0;
        int DDepth = 0;

        System.out.println(ADepth);
        System.out.println(BDepth);
        System.out.println(DDepth);

        String str;
        try{
            BufferedReader br;

            String command = "samtools depth -Q 20 " + file;
            String [] cmdArray ={"/bin/bash","-c", command};
            Process p = Runtime.getRuntime().exec(cmdArray,null);
            br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            System.out.println("Start reading bam "+ file);

            while ((str = br.readLine()) != null){

                String[] temp = str.split("\t");
                int start = Integer.parseInt(temp[0]);
                if (this.AA.contains(start)){
                    ADepth += Integer.parseInt(temp[2]);
//                    System.out.println("ADepth is " + ADepth);
                } else if (this.BB.contains(start)){
                    BDepth += Integer.parseInt(temp[2]);
//                    System.out.println("BDepth is " + BDepth);
                } else if (this.DD.contains(start)){
                    DDepth += Integer.parseInt(temp[2]);
                }
            }
            p.waitFor();
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
}