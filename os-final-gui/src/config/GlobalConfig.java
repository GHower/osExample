package config;

import java.awt.*;

public class GlobalConfig {
    public static Font font(int type,int size){
        return new Font("宋体",type,size);
    }
    public static Color color(PrepareColor prepareColor){
        return new Color(prepareColor.r,prepareColor.g,prepareColor.b);
    }
    public static Color color(){
        return new Color(PrepareColor.MEMORY.r,PrepareColor.MEMORY.g,PrepareColor.MEMORY.b);
    }
}

