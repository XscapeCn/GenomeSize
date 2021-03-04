import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Date;

public class TestThread {
    public static void main(String[] args) {
//        long startTime1=System.currentTimeMillis();   //获取开始时间
        ThreadWriteFile t1 = new ThreadWriteFile("orsat.zemayB73.sing.maf");
        ThreadWriteFile t2 = new ThreadWriteFile("orsat.orlon.sing.maf");
        ThreadWriteFile t3 = new ThreadWriteFile("orsat.mubal.sing.maf");
        ThreadWriteFile t4 = new ThreadWriteFile("orsat.sobic.sing.maf");
        ThreadWriteFile t5 = new ThreadWriteFile("orsat.brdis.sing.maf");
        t5.start();
        t4.start();
        t1.start();
        t3.start();
        t2.start();
//        t1.run();
//        t2.run();
//        t3.run();
//        t4.run();
//        t5.run();
//        long endTime1=System.currentTimeMillis(); //获取结束时间
//
//        System.out.println("程序运行时间： "+(endTime1-startTime1)+"ms");


//
//        long startTime2=System.currentTimeMillis();   //获取开始时间
        writeFileNormal("orsat.sobic.sing.maf");
        writeFileNormal("orsat.mubal.sing.maf");
        writeFileNormal("orsat.sobic.sing.maf");
        writeFileNormal("orsat.brdis.sing.maf");
        writeFileNormal("orsat.zemayB73.sing.maf");
//        long endTime2=System.currentTimeMillis(); //获取结束时间

//        System.out.println("程序运行时间： "+(endTime2-startTime2)+"ms");




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

class ThreadWriteFile  extends Thread implements Runnable{
    String filename1;
//    String filename2;

    public ThreadWriteFile(String filename1){
        this.filename1 = filename1;
//        this.filename2 = filename2;
    }

    @Override
    public void run() {
        long start = System.currentTimeMillis();
        try{
            BufferedReader br = new BufferedReader(new FileReader(this.filename1));
            String filename2 = this.filename1.split("\\.sing\\.maf")[0] + ".test";
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
        long end = System.currentTimeMillis();
        System.out.println("Start of " + filename1 + " is: " + start);
        System.out.println("End of " + filename1 + " is: " + end);
        System.out.println("===============");

    }

}

