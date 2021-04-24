import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.neo4j.driver.*;


public class Test {

    public static void main(String[] args) throws IOException {
//        final int[] A = {1,2,7,8,13,14,19,20,25,26,31,32,37,38};
//        final int[] B = {3,4,9,10,15,16,21,22,27,28,33,34,39,40};
//        final int[] D = {5,6,11,12,17,18,23,24,29,30,35,36,41,42};
//        HashSet<Integer> AA = new HashSet<>();
//        HashSet<Integer> BB = new HashSet<>();
//        HashSet<Integer> DD = new HashSet<>();
//
//        for (int j : A) AA.add(j);
//        for (int j : B) BB.add(j);
//        for (int j : D) DD.add(j);
//
//        int AI = 0;
//        int BI = 0;
//        int DI = 0;
//
//        long ADepth = 0;
//        long BDepth = 0;
//        long DDepth = 0;
//
//        int index = 100000;

//        List<Integer> AAA = new ArrayList<>();
//        for (int i = 0 , length = A.length; i < length; i++) {
//            AAA.add(A[i]);
//        }

//        long startTime=System.currentTimeMillis();
//        for (int i = 0; i < index; i++) {
//            if (i%2 == 0){
//                ADepth += Long.parseLong("1");
//            }
//        }
//        System.out.println(ADepth);
//
//        System.out.println(System.currentTimeMillis() - startTime);
//
//        System.out.println("===============");
//
//
//        long startTime1=System.currentTimeMillis();
//        for (int i = 0; i < index; i++) {
//            if (i%2 == 0) BDepth += Integer.parseInt("1");
//        }
//        System.out.println(BDepth);
//
//        System.out.println(System.currentTimeMillis() - startTime1);
//        System.out.println("===============");

//        String str = "V300016179L3C004R020879409 83 RIX_Taes_Stasy_consensus-1 274 6 100M = 150 -224 TCCTTAGTGGCACGCTACATCGGCGCCCGGATGTAGTGGAAAATGTGCAAGGGTCTTCGCATTTGACTCGACGAGTGCGAAGGGTAAGGAAGCTAGCCGA ECFGFGGGFFGFGFFGFD=FFAGFF=F(GFFFEFFFDFF?EGFFDFFGFFGFFBGFFFDACFGFF+CFC@F@FFGFFFGGFF>FGGGCFFFDFEFFFFDF AS:i:-10XS:i:-15 XN:i:0 XM:i:2 XO:i:0 XG:i:0 NM:i:2 MD:Z:11G33A54 YS:i:0 YT:Z:CP";
//
//        int count1 = 0;
//        long startTime2=System.currentTimeMillis();
//        for (int i = 0; i < index; i++) {
//            if (str.contains("100M")){
//                count1 += 1;
//            }
//        }
//
//        System.out.println(System.currentTimeMillis() - startTime2);
//        System.out.println("===============");
//
//
//        long startTime3=System.currentTimeMillis();
//        int count = 0;
//        for (int i = 0; i < index; i++) {
//            String[] temp = str.split(" ");
//            if (temp[5] == "100M"){
//                count +=1;
//            }
//        }
//        System.out.println(System.currentTimeMillis() - startTime3);

//        for (int a:DD
//             ) {
//
//            System.out.println(a);
//        }
//
//        try{
//            BufferedReader br = new BufferedReader(new FileReader(args[0]));
//
//            String str;
//            while ((str = br.readLine()) != null){
//
//                String[] temp = str.split("\t");
//                int start = Integer.parseInt(temp[0]);
//
//                if (AA.contains(start)){
//                    ADepth += Long.parseLong(temp[2]);
//                } else if (BB.contains(start)){
//                    BDepth += Long.parseLong(temp[2]);
//                } else if (DD.contains(start)){
//                    DDepth += Long.parseLong(temp[2]);
//                }
//            }
//            br.close();
//
//        }catch(Exception e){
//            e.printStackTrace();
//            System.exit(1);
//        }
//        String[] res = new String[]{args[0], String.valueOf(ADepth), String.valueOf(BDepth), String.valueOf(DDepth), String.valueOf(ADepth + BDepth + DDepth)};
//
//        for (String re : res) {
//            System.out.println(re);
//        }
        int DHH = 0;
        int DTA = 0;
        int DTB = 0;
        int DTH = 0;
        int DTT = 0;
        int DXX = 0;
        int DTC = 0;
        int DTM = 0;
        int DTX = 0;
        int RLC = 0;
        int RLX = 0;
        int RIX = 0;
        int RLG = 0;
        int RSX = 0;
        int count =0;

        BufferedReader br = new BufferedReader(new FileReader(args[0]));

        String str;
        String[] temp;
        String te;

        while( (str = br.readLine()) != null){
            if (!str.contains("@SQ")){
                count += 1;
            }

            if (str.contains("100M")){
                temp = str.split("\t");
                te = temp[2].substring(0,3);
//                System.out.println(te);
                switch(te)
                {
                    case "DHH" :
                        DHH += 1;
                        break;
                    case "DTA" :
                        DTA += 1;
                        break;
                    case "DTB" :
                        DTB += 1;
                        break;
                    case "DTC" :
                        DTC += 1;
                        break;
                    case "DTH" :
                        DTH += 1;
                        break;
                    case "DTM" :
                        DTM += 1;
                        break;
                    case "DTT" :
                        DTT += 1;
                        break;
                    case "DTX" :
                        DTX += 1;
                        break;
                    case "DXX" :
                        DXX += 1;
                        break;
                    case "RIX" :
                        RIX += 1;
                        break;
                    case "RLC" :
                        RLC += 1;
                        break;
                    case "RLG" :
                        RLG += 1;
                        break;
                    case "RLX" :
                        RLX += 1;
                        break;
                    case "RSX" :
                        RSX += 1;
                        break;
                }
            }
        }

        br.close();
        System.out.println(DHH);
        System.out.println(DTA);
        System.out.println(DTB);
        System.out.println(DTH);
        System.out.println(DTT);
        System.out.println(DXX);
        System.out.println(DTC);
        System.out.println(DTM);
        System.out.println(DTX);
        System.out.println(RLC);
        System.out.println(RLX);
        System.out.println(RIX);
        System.out.println(RLG);
        System.out.println(RSX);
    }
}

