import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SortGenes {
    public static int splitNum(String str){
        return Integer.parseInt(str.split("\t")[1].substring(12,18));
    }


    public static void st1(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("D:/Desktop/ArinaLrFor.1A.gene"));
//            BufferedWriter bw = new BufferedWriter(new FileWriter("D:/Desktop/testGene.txt"));
            String s1 = br.readLine();
            String s2 = br.readLine();
            String s3 = br.readLine();
            String write = sort(s1,s1,s2,s3);
//            bw.write(write + "\n");
            if (write!=null){System.out.println(write);}
            s1 = s2;
            s2 = s3;

            while (!((s3 = br.readLine()) == null)){
                write = sort(s1,s1,s2,s3);
//                bw.write(write + "\n");
                if (write !=null){
                    System.out.println(write);}
                s1 = s2;
                s2 = s3;
            }

//            bw.close();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String sort(String str, String str1, String str2, String str3){
        int num = splitNum(str);
        int num1 = splitNum(str1);
        int num2 = splitNum(str2);
        int num3 = splitNum(str3);
        if ( num2 > num1){
            if (num3 > num1){
                if(num1 >= num){return str1;}}}
        return null;
    }


    public static void nd2(String filename,int n){
        int index = n;
        List<String> strList = new ArrayList<>();
//        List<Integer> numList = new ArrayList<>();
        List<String> res = new ArrayList<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String str = null;
            while((str=br.readLine()) != null){
                strList.add(str);
//                numList.add(splitNum(str));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String small = strList.get(0);


        for (int i = 0; i < strList.size()-2; i++) {

            String s1 = strList.get(i);
            String s2 = strList.get(i+1);
            String s3 = strList.get(i+2);
            int num = splitNum(small);
            int num1 = splitNum(s1);
            int num2 = splitNum(s2);
            int num3 = splitNum(s3);


            if (i < index && num1 < num){
                small = s1;
                num = num1;
                res = new ArrayList<>();
            }

            if (num2 > num1){
                if (num3 > num1){
                    if(num1 >= num){
                        res.add(s1);
                    }
                }
            }
        }

        List<String> res2 = new ArrayList<>();
        res2.add(res.get(0));

        for (int i = 1; i < res.size(); i++) {
            if (splitNum(res.get(i)) > splitNum(res.get(i-1))){
                res2.add(res.get(i));
            }else {
                res.set(i,res.get(i-1));
            }
        }

        for (String re : res2) {
            System.out.println(re);
        }

    }

    public static void main(String[] args) {
        nd2(args[0], Integer.parseInt(args[1]));
    }
}
