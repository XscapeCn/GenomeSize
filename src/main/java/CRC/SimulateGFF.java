package CRC;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimulateGFF {
    public static void main(String[] args) {

//        String aa = "D:/Desktop/test.gff";
//        String bb = "D:/Desktop/test11.gff";
//        writeGFF(setBlock(getBlock(aa)), bb);

        writeGFF(setBlock(getBlock(args[0])), args[1]);
    }

    public static List<List<String[]>> getBlock(String file){
        String str = null;
        List<List<String[]>> res = new ArrayList<>();
        List<String[]> temp = new ArrayList<>();

        try{
            BufferedReader br = new BufferedReader(new FileReader(file));

            while ((str = br.readLine()) != null){
                String[] split = str.split("\t");
                if (split[2].equals("pseudogene") || split[2].equals("gene")){
                    res.add(temp);
                    temp = new ArrayList<>();
                }
                temp.add(split);
            }

            res.add(temp);
            res.remove(0);
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public  static List<List<String[]>> setBlock(List<List<String[]>> block){
        List<List<String[]>> res = new ArrayList<>();
        List<String[]> temp = new ArrayList<>();

        for (int i = 0; i < block.size(); i++) {
            if (i > 0){
                String s = block.get(i - 1).get(block.get(i - 1).size() - 1)[4];
                String e = block.get(i).get(0)[3];
                if ((Integer.parseInt(e) - 3003)> (Integer.parseInt(s) + 3003)){
                    temp.add(getInter(block.get(i).get(0),s,e));
                    res.add(temp);
                }
                temp = new ArrayList<>();
            }
            res.add(operate(block.get(i)));
        }

        return res;
    }

    public static List<String[]> operate(List<String[]> block){
        List<String[]> res = new ArrayList<>();
        res.add(getStart(block.get(0)));
        for (int i = 0; i < block.size(); i++) {
            if (block.get(i)[2].equals("exon")){
                res.add(block.get(i));
                if (i == block.size() - 1){
                    res.add(getEnd(block.get(block.size() -1)));
                    return res;
                }else {
                    if (block.get(i+1)[2].equals("exon")){
                        if (Integer.parseInt(block.get(i+1)[3]) - Integer.parseInt(block.get(i)[4]) > 2){
                            res.add(getIntro(block.get(i),block.get(i + 1)[3]));
                        }
                    }
                }
            }else {
                res.add(block.get(i));
            }
        }
        res.add(getEnd(block.get(block.size() -1)));
        return res;
    }

    public static String[] getStart(String[] row){
        String[] clone = row.clone();
        int temp = Integer.parseInt(row[3]) - 1;
        clone[2] = "promoter";
        clone[4] = String.valueOf(temp);
        clone[3] = String.valueOf(temp - 3000);
        return clone;
    }

    public static String[] getIntro(String[] row, String num){
        String[] clone = row.clone();
        clone[2] = "intron";
        clone[3] = String.valueOf(Integer.parseInt(row[4]) + 1);
        clone[4] = String.valueOf(Integer.parseInt(num) - 1);
        return clone;
    }

    public static String[] getEnd(String[] row){
        String[] clone = row.clone();
        int temp = Integer.parseInt(row[4]) + 1;
        clone[2] = "down";
        clone[3] = String.valueOf(temp);
        clone[4] = String.valueOf(temp + 3000);
        return clone;
    }

    public static String[] getInter(String[] row, String s, String e){
        String[] clone = row.clone();
        clone[2] = "intergenic";
        clone[3] = String.valueOf(Integer.parseInt(s) + 3002);
        clone[4] = String.valueOf(Integer.parseInt(e) - 3002);
        return clone;
    }

    public static void writeGFF(List<List<String[]>> res, String outFile){
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
            for (List<String[]> re : res) {
                for (String[] strings : re) {
                    for (String string : strings) {
                        bw.write(string + "\t");
                    }
                    bw.write("\n");
                }

            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
