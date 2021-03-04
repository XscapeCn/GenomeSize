import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TransferBed {
    HashMap<String, Integer> table = new HashMap<>();
    HashMap<String, Integer> table2 = new HashMap<>();

    public static void main(String[] args) {
//        String file = "C:/Users/Administrator/Desktop/A_E1_conservation_forSamtools.bed";
        TransferBed tf = new TransferBed(args[0]);
    }

    public TransferBed(String filename){
        if (filename.startsWith("A")){
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
        } else if (filename.startsWith("B")){
            this.table.put("0", 480980714);
            this.table.put("chr1", 438720154);
            this.table.put("chr2", 453218924);
            this.table.put("chr3", 448155269);
            this.table.put("chr4", 451014251);
            this.table.put("chr5", 451372872);
            this.table.put("chr6", 452077197);
            this.table.put("chr7", 453822637);

            this.table2.put("chr1", 3);
            this.table2.put("chr2", 9);
            this.table2.put("chr3", 15);
            this.table2.put("chr4", 21);
            this.table2.put("chr5", 27);
            this.table2.put("chr6", 33);
            this.table2.put("chr7", 39);
        } else if (filename.startsWith("D")){
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

        readFile(filename);
    }

    public List<String[]> readFile(String filename){

        Thread aaaaaa = new Thread();
        aaaaaa.start();
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
                    int  aa = Integer.valueOf(re[1]) - this.table.get(re[0]);
                    bw.write(aa + "\t");
//                    bw.write("\t");
                    int  bb = Integer.valueOf(re[2]) - this.table.get(re[0]);
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

}
