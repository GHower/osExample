package utils;

/**
 * 随机数生成工具
 */
public class RandomTool {
    public static int random(int max) {
        return (int) (Math.random() * max);
    }

    public static int random(int down, int up) {
        return (int) (Math.random() * (up - down) + down);
    }

    public static double random(double down, double up) {
        return Math.random() * (up - down) + down;
    }

    public static long random(long down, long up) {
        return (long) (Math.random() * (up - down) + down);
    }
    // 随机产生true或false
    public static boolean randomBool() {
        return Math.random() > 0.5;
    }
    // 有p概率为true
    public static boolean randomBool(double p) {
        return p>=0 && p <= 1.0 && Math.random() < p;
    }
}
