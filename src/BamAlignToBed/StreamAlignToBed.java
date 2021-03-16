package BamAlignToBed;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class StreamAlignToBed{
    List<Set<Integer>> refBed;
    int index;

    public StreamAlignToBed(){};

    public StreamAlignToBed(String path, String refBedPath, int index){
        this.refBed = readDic(refBedPath, index);
        String[] files = readPath(path);
        this.index = index;

        for (String file : files) {
            compare(readBamViaStream(file), file);
        }
    }

    public String[] readPath(String path){
        if (path.endsWith("/")){
            List<String> files = new ArrayList<>();
            File[] tempList = (new File(path)).listFiles();
            assert tempList != null;
            for (File file : tempList) {
                if (file.isFile() && file.toString().endsWith(".bam")) {
                    files.add(file.toString());
                }
            }
            return files.toArray(new String[0]);
        }else if (path.endsWith(".bam")){
            return new String[]{path};
        }else {
            System.out.println("Invalid file input, input a path end with / or a file end with .bam");
            System.exit(1);
            return null;
        }
    }

    public static @NotNull
    List<Set<Integer>> readDic(String filename, int index){
        List<Set<Integer>> res = new ArrayList<>();
        for (int i = 0; i < index; i++) {
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

    public BufferedReader readBamViaStream(String file){
        BufferedReader aa = null;
        try{
            String command = "samtools depth -Q 20 " + file;
            String [] cmdArray ={"/bin/bash","-c", command};
            Process p =Runtime.getRuntime().exec(cmdArray,null);
            aa = new BufferedReader(new InputStreamReader(p.getInputStream()));
            p.waitFor();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        return aa;
    }

    public void compare(BufferedReader br, String file){
        String str;
        try{
            String out = file.split("\\.bam")[0]+ ".out";
            BufferedWriter bw = new BufferedWriter(new FileWriter(out));
            while ((str=br.readLine()) != null){
                String[] temp = str.split("\t");
                int start = Integer.parseInt(temp[0]);
                if (0 < start && start <= this.index){
                    if (this.refBed.get(start - 1).contains(Integer.parseInt(temp[1]))){
                        bw.write(str);
                        bw.write("\n");
                    }
                }
            }
            bw.close();
            br.close();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}
