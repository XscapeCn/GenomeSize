package BamAlignToBed;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StreamAlignWithCon extends StreamAlignToBed{
    int pool;

    public StreamAlignWithCon(){};

    public StreamAlignWithCon(String path, String refBedPath, int index, int pool) {
        this.refBed = readDic(refBedPath, index);
        String[] files = readPath(path);
        this.pool = checkPool(pool);
        this.index = index;

        ExecutorService es = Executors.newFixedThreadPool(this.pool);

        for (String file:files) {
            try {
                es.submit(new Thread(
                        () -> compare(readBamViaStream(file), file)
                ));
            }catch(Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
        es.shutdown();
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
}
