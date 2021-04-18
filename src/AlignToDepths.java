import java.io.*;
import java.util.*;

public class AlignToDepths {
    public static void main(String[] args) throws IOException {
        writeFile(args[0], args[1]);
    }

    public static List<Set<Integer>> readDic(String filename, int index){
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

    public static void writeFile(String depth, String bed){
        if (!depth.contains("depth") || !bed.contains("bed")){
            System.out.println("File is not right, end plz");
        }
        List<Set<Integer>> lists = readDic(bed, 42);
        String out = depth.split("\\.")[0].split("/")[5] + ".out";
        try {
            BufferedReader br = new BufferedReader(new FileReader(depth));
            BufferedWriter bw = new BufferedWriter(new FileWriter(out));
            String str;
            while ((str=br.readLine()) != null){
                String[] temp = str.split("\t");
                if (!str.startsWith("0") && !str.startsWith("43") && !str.startsWith("44")){
                    if (lists.get(Integer.parseInt(temp[0]) - 1).contains(Integer.parseInt(temp[1]))){
                        bw.write(str);
                        bw.write("\n");
                    }
                }
            }
            bw.close();
            br.close();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}