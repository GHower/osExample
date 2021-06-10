package users;

import config.OSConfig;
import file_system.Dentry;

import java.io.Serializable;

/**
 * 操作系统用户实体,主要用在文件系统
 */
public class OSUser implements Serializable {

    public String userName;
    public String groupName;
    public String password = "123456";
    // 用户当前所在目录
    public Dentry current = OSConfig.ROOT;


    public OSUser(String userName,String groupName) {
        this.userName = userName;
        this.groupName = groupName;
        current.dirTree.get("home").creatFile(userName,userName,groupName, true);
        current = current.cd("./home/"+userName);
        OSConfig.userList.put(userName, this);
    }
}
