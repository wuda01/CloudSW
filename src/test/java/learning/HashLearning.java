package learning;

import java.util.Hashtable;

/**
 * Created by xubo on 2017/1/13.
 */
public class HashLearning {
    public static void main(String[] args) {
        Hashtable hashtable=new Hashtable();
        hashtable.put("a","b");
        hashtable.put(1,1);

        for(Object o:hashtable.entrySet()){
            System.out.println(o);
        }


    }
}
