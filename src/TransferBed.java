import java.awt.desktop.SystemEventListener;
import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransferBed {
    HashMap<String, Integer> table = new HashMap<>();
    HashMap<String, Integer> table2 = new HashMap<>();

    public static void main(String[] args) {
//        String file = "C:/Users/Administrator/Desktop/A_E1_conservation_forSamtools.bed";
        for (int i = 0; i < args.length; i++) {
            TransferBed tf = new TransferBed(args[i]);
        }
    }

    public TransferBed(String filename){
        if (filename.contains("A")){
            this.table.put("0", 480980714);
            this.table.put("chr1", 471304005);
            this.table.put("chr2", 462376173);
            this.table.put("chr3", 454103970);
            this.table.put("chr4", 452555092);
            this.table.put("chr5", 453230519);
            this.table.put("chr6", 452440856);
            this.table.put("chr7", 450046986);

            this.table2.put("chr1", 1);
            this.table2.put("chr2", 7);
            this.table2.put("chr3", 13);
            this.table2.put("chr4", 19);
            this.table2.put("chr5", 25);
            this.table2.put("chr6", 31);
            this.table2.put("chr7", 37);
        } else if (filename.contains("B")){
            this.table.put("0", 480980714);
            this.table.put("chr1", 438720154);
            this.table.put("chr2", 453218924);
            this.table.put("chr3", 448155269);
            this.table.put("chr4", 451014251);
            this.table.put("chr5", 451372872);
            this.table.put("chr6", 452077197);
            this.table.put("chr7", 453822637);
//            this.table = (HashMap<String, Integer>) Map.of(
//                    "0", 480980714,
//                    "chr1", 438720154,
//                    "chr2", 453218924,
//                    "chr3", 448155269,
//                    "chr4", 451014251,
//                    "chr5", 451372872,
//                    "chr6", 452077197,
//                    "chr7", 453822637);
//
//            this.table2 = (HashMap<String, Integer>) Map.of(
////                    "0", 480980714,
//                    "chr1", 3,
//                    "chr2", 9,
//                    "chr3", 15,
//                    "chr4", 21,
//                    "chr5", 27,
//                    "chr6", 33,
//                    "chr7", 39);

            this.table2.put("chr1", 3);
            this.table2.put("chr2", 9);
            this.table2.put("chr3", 15);
            this.table2.put("chr4", 21);
            this.table2.put("chr5", 27);
            this.table2.put("chr6", 33);
            this.table2.put("chr7", 39);
        } else if (filename.contains("D")){
            this.table.put("0", 480980714);
            this.table.put("chr1", 452179604);
            this.table.put("chr2", 462216879);
            this.table.put("chr3", 476235359);
            this.table.put("chr4", 451004620);
            this.table.put("chr5", 451901030);
            this.table.put("chr6", 450509124);
            this.table.put("chr7", 453812268);

            this.table2.put("chr1", 5);
            this.table2.put("chr2", 11);
            this.table2.put("chr3", 17);
            this.table2.put("chr4", 23);
            this.table2.put("chr5", 29);
            this.table2.put("chr6", 35);
            this.table2.put("chr7", 41);
        }

//        readFile(filename);
        splitBed(filename);


    }

    public List<String[]> readFile(String filename){

//        Thread aaaaaa = new Thread();
//        aaaaaa.start();
        List<String[]> res = new ArrayList<>();
        String file = filename.split("\\.")[0] + "_transfer.bed";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            String str;
            while ((str = br.readLine()) != null){
                res.add(str.split("\t"));
            }

            for (String[] re : res) {
//                bw.write(re[0]);
//                bw.write("\t");
                if (Integer.parseInt(re[1]) <= this.table.get(re[0])) {
                    bw.write(this.table2.get(re[0]) + "\t");
                    bw.write(re[1]);
                    bw.write("\t");
                    bw.write(re[2] + "\n");
                } else {
                    bw.write(this.table2.get(re[0]) + 1 + "\t");
                    int  aa = Integer.parseInt(re[1]) - this.table.get(re[0]);
                    bw.write(aa + "\t");
//                    bw.write("\t");
                    int  bb = Integer.parseInt(re[2]) - this.table.get(re[0]);
                    bw.write(bb + "\n");
                }
            }
            bw.close();
            br.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return res;
    }

    public void splitBed(String filename){
//        List<String[]> res = new ArrayList<>();
        int index = Integer.parseInt(String.valueOf(filename.charAt(0)));
        String key = "chr" + index;
        String file1 = filename.split("\\.")[0] + ".1.transfer.bed";
        String file2 = filename.split("\\.")[0] + ".2.transfer.bed";

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            BufferedWriter bw1 = new BufferedWriter(new FileWriter(file1));
            BufferedWriter bw2 = new BufferedWriter(new FileWriter(file2));

            String str;
            while ((str = br.readLine()) != null){
                String[] temp = str.split("\t");
                if (Integer.parseInt(temp[2]) < table.get(key)){
                    bw1.write(table2.get(key) + "\t");
//                    bw1.write("\t");
                    for (int i = 1; i < temp.length; i++) {
                        bw1.write(temp[i] + "\t");
                    }
                    bw1.write("\n");
                }else{
                    bw2.write(table2.get(key)+1 + "\t");
//                    bw2.write("\t");
                    bw2.write(Integer.parseInt(temp[1]) - table.get(key) + "\t");
//                    bw2.write("\t");
                    bw2.write(Integer.parseInt(temp[2]) - table.get(key) + "\t");
//                    bw2.write("\t");
                    bw2.write(temp[3]);bw2.write("\t");
//                    bw2.write(temp[4]);bw2.write("\t");
                    bw2.write("\n");
                }
            }
            bw1.close();
            bw2.close();
            br.close();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }


}
