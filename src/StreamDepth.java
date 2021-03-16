import java.io.*;
import java.util.*;

import java.util.stream.Stream;

public class StreamDepth extends AlignToDepths{
    List<Set<Integer>> refBed;

    public StreamDepth(String path, String redBedPath, int refIndex){
        List<String> files = new ArrayList<>();
//        File file = new File(path);
        File[] tempList = (new File(path)).listFiles();

        this.refBed = readDic(redBedPath, refIndex);

        assert tempList != null;
        for (File file : tempList) {
            if (file.isFile() && file.toString().endsWith(".bam")) {
                files.add(file.toString());
//                System.out.println(file.toString());
            }
        }

        for (String file:files) {
            try {
                streamRead(file);
//                BufferedReader br = new BufferedReader(new FileReader(file));

            }catch(Exception e){
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public StreamDepth() {

    }

    public void streamRead(String file) throws IOException, InterruptedException {
        String out = file.split("\\.")[0] + ".out";

        String command = "samtools depth -Q 20 " + file;
        String [] cmdArray ={"/bin/bash","-c", command};
        Process p =Runtime.getRuntime().exec(cmdArray,null);
        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));

        BufferedWriter bw = BufferedFileWriter(out);
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
//        String temp = null;
//        while ((temp = br.readLine()) != null) {
//            int geneNumber = Integer.parseInt(temp.split(" ")[0]);
//        }
    }

    public static BufferedWriter BufferedFileWriter(String aa) throws IOException {
        return new BufferedWriter(new FileWriter(aa));
    }

//    public void writeFile(BufferedReader br, String out){
//        try {
//            BufferedWriter bw = new BufferedWriter(new FileWriter(out));
//            String str;
//            while ((str=br.readLine()) != null){
//                String[] temp = str.split("\t");
//                if (!str.startsWith("0") && !str.startsWith("43") && !str.startsWith("44")){
//                    if (refBed.get(Integer.parseInt(temp[0]) - 1).contains(Integer.parseInt(temp[1]))){
//                        bw.write(str);
//                        bw.write("\n");
//                    }
//                }
//            }
//            bw.close();
//            br.close();
//        }catch (Exception e){
//            e.printStackTrace();
//            System.exit(1);
//        }
//    }


    public static void main(String[] args) {
        StreamDepth sd = new StreamDepth(args[0], args[1], Integer.parseInt(args[2]));
    }
}

