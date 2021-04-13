import java.awt.desktop.SystemEventListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class SplitMaf {
    public static void main(String[] args) {
        List<List<List<String>>> res = new ArrayList<>();
        for (int i = 0; i < Integer.parseInt(args[1]); i++) {
            List<List<String>> temp = new ArrayList<>();
            res.add(temp);
        }
        try{
            BufferedReader br = new BufferedReader(new FileReader(args[0]));
            String str = br.readLine();
            while(str.startsWith("#")){
                str = br.readLine();
            }
            List<String> tempArr1 = new ArrayList<>();


            if (str.startsWith("a")){
                tempArr1.add(str);
            }

            while((str = br.readLine()) != null){
                if (str.startsWith("a") || str.startsWith("s")){
                    tempArr1.add(str);
                }else if (str.equals("")){

                    for (int i = 1; i < Integer.parseInt(args[1]) +1 ; i++) {
                        if (tempArr1.get(1).contains("chr" + i + " ")){
                            res.get(i-1).add(tempArr1);
                        }else {
                            continue;
                        }
                    }
                    tempArr1 = new ArrayList<>();
                }
            }
            for (int i = 0; i < res.size(); i++) {
                writeMaf(res.get(i), Integer.parseInt(args[1]));
            }

            br.close();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void writeMaf(List<List<String>> arr, int number){

        String mark = null;
        if (arr.size() != 0){
            for (int i = 1; i < number + 1; i++) {
                if (arr.get(0).get(1).contains("chr" + i +" ")){mark = "chr" + i + ".maf";}
            }
            try{
                BufferedWriter bw  = new BufferedWriter(new FileWriter(mark));
                for (int i = 0; i < arr.size(); i++) {
                    for (int j = 0; j < arr.get(i).size(); j++) {
                        bw.write(arr.get(i).get(j));
                        bw.newLine();
                    }
                    bw.newLine();
                }
                bw.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}

//        List<List<String>> chr01 = new ArrayList<>();
//        List<List<String>> chr02 = new ArrayList<>();
//        List<List<String>> chr03 = new ArrayList<>();
//        List<List<String>> chr04 = new ArrayList<>();
//        List<List<String>> chr05 = new ArrayList<>();
//        List<List<String>> chr06 = new ArrayList<>();
//        List<List<String>> chr07 = new ArrayList<>();
//        List<List<String>> chr08 = new ArrayList<>();
//        List<List<String>> chr09 = new ArrayList<>();
//        List<List<String>> chr10 = new ArrayList<>();
//        List<List<String>> chr11 = new ArrayList<>();
//        List<List<String>> chr12 = new ArrayList<>();

//            while (str != null){
//                List<String> tempArr = new ArrayList<>();
//                if (str.startsWith("a")){
//                    String temp = str;
//                    tempArr.add(temp);
//                    while ((str = br.readLine()) != null){
//                        if (str.startsWith("s")){
//                            tempArr.add(str);
//                        }else {break;}
//                    }
//                } else if (str.equals("")){
//                    str = br.readLine();
//                    String temp = str;
//                    tempArr.add(temp);
//                    while ((str = br.readLine()) != null){
//                        if (str.startsWith("s")){
//                            tempArr.add(str);
//                        }else {break;}
//                    }
//                }
//            }

//                else if (tempArr.get(1).contains("chr1 ")){
//                    chr01.add(tempArr);
//                }else if (tempArr.get(1).contains("chr2")){
//                    chr02.add(tempArr);
//                }else if (tempArr.get(1).contains("chr3")){
//                    chr03.add(tempArr);
//                }else if (tempArr.get(1).contains("chr4")){
//                    chr04.add(tempArr);
//                }else if (tempArr.get(1).contains("chr5")){
//                    chr05.add(tempArr);
//                }else if (tempArr.get(1).contains("chr6")){
//                    chr06.add(tempArr);
//                }else if (tempArr.get(1).contains("chr7")){
//                    chr07.add(tempArr);
//                }else if (tempArr.get(1).contains("chr8")){
//                    chr08.add(tempArr);
//                }else if (tempArr.get(1).contains("chr9")){
//                    chr09.add(tempArr);
//                }else if (tempArr.get(1).contains("chr10")){
//                    chr10.add(tempArr);
//                }else if (tempArr.get(1).contains("chr11")){
//                    chr11.add(tempArr);
//                }else if (tempArr.get(1).contains("chr12")){
//                    chr12.add(tempArr);
//                }

//            writeMaf(chr01);
//            writeMaf(chr02);
//            writeMaf(chr03);
//            writeMaf(chr04);
//            writeMaf(chr05);
//            writeMaf(chr06);
//            writeMaf(chr07);
//            writeMaf(chr08);
//            writeMaf(chr09);
//            writeMaf(chr10);
//            writeMaf(chr11);
//            writeMaf(chr12);

//        else if (arr.get(0).get(1).contains("chr1 ")){
//            mark = "chr1.maf";
//        }else if (arr.get(0).get(1).contains("chr2")){
//            mark = "chr2.maf";
//        }else if (arr.get(0).get(1).contains("chr3")){
//            mark = "chr3.maf";
//        }else if (arr.get(0).get(1).contains("chr4")){
//            mark = "chr4.maf";
//        }else if (arr.get(0).get(1).contains("chr5")){
//            mark = "chr5.maf";
//        }else if (arr.get(0).get(1).contains("chr6")){
//            mark = "chr6.maf";
//        }else if (arr.get(0).get(1).contains("chr7")){
//            mark = "chr7.maf";
//        }else if (arr.get(0).get(1).contains("chr8")){
//            mark = "chr8.maf";
//        }else if (arr.get(0).get(1).contains("chr9")){
//            mark = "chr9.maf";
//        }else if (arr.get(0).get(1).contains("chr10")){
//            mark = "chr10.maf";
//        }else if (arr.get(0).get(1).contains("chr11")){
//            mark = "chr11.maf";
//        }else if (arr.get(0).get(1).contains("chr12")){
//            mark = "chr12.maf";
//        }