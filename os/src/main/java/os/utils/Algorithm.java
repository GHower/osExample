package os.utils;

import java.text.Normalizer;

public class Algorithm {
    public static boolean randomBool(){
        return Math.random() > 0.5;
    }

    /**
     * 概率返回true
     */
    public static boolean randomBool(double p){
        return Math.random() < p;
    }
    public static int randomRange(int down,int up){
        return (int) (down + Math.random()*(up-down));
    }
    public static long randomRange(long down,long up){
        return (long) (down + Math.random()*(up-down));
    }
}
