package ssw;

import org.junit.Assert;
import parasail.Matrix;
import scala.Char;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * verify the scoreMatrix  SparkSW Vs DSW
 * Created by xubo on 2016/12/6.
 */
public class MatrixTest {
    public static void main(String[] args) {
        int[][] sswMatrix = SSW.scoreMatrix50;

        int[][] sparkSWMatrix = new int[26][26];
        String fileName = "file\\input\\subMatrix\\hash50.csv";
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            while ((tempString = reader.readLine()) != null) {
                System.out.println("line " + line + ": " + tempString);
                String[] str = tempString.split(",");
                for (int i = 0; i < str.length; i++) {
                    sparkSWMatrix[line - 1][i] = Integer.valueOf(str[i]);
                }
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }

        System.out.println("ssw");
//        System.out.println(Integer.valueOf('M'-64));
        for (int i = 65; i < 91; i++) {
            System.out.print("line " + (i - 64) + ":");
            for (int j = 65; j < 91; j++) {
                System.out.print(sswMatrix[i][j] + ",");
//                assert(sswMatrix[i][j]==sparkSWMatrix[i-65][j-65]);
//                assert(false);

//                System.out.println();
                try {
                    Assert.assertEquals(sswMatrix[i][j], sparkSWMatrix[i - 65][j - 65]);
                } catch (AssertionError e) {
                    System.out.println();
                    char m = (char) i;
                    char n = (char) j;
                    System.out.println(m + ":" + n);
                    e.printStackTrace();
                    return;
                }


            }
            System.out.println();
        }

    }
}
