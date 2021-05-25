package os.model.entity;

import lombok.Data;

import java.util.List;
@Data
public class MyRequest {
    /**
     * 对应进程的PID,进程标识符,即 MyPCB.pid => MyProcess.id
     * 相当于指针，指向process,用来找到真正的进程实体
     */
    private Integer id;
    /**
     * 进程需要申请的资源
     */
    private List<MyResource> request;
}
