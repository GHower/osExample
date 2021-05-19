package os.enums;

/**
 * 状态枚举类
 */
public enum MyStatus {

    READY("就绪"),RUN("运行"),WAIT("等待");
    private String name;
    MyStatus(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

}
