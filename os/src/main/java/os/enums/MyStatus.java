package os.enums;

/**
 * 状态枚举类
 */
public enum MyStatus {

    BACK("后备"),
    READY("就绪"),
    RUN("运行"),
    WAIT("等待"),
    FINISH("完成");

    private final String name;
    MyStatus(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

}
