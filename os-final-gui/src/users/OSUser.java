package users;

import config.OSConfig;
import file_system.Dentry;

/**
 * 操作系统用户实体,主要用在文件系统
 */
public class OSUser {

    public String userName;
    public String groupName;
    public String password = "123456";
    public Dentry current = OSConfig.ROOT;


    public OSUser(String userName,String groupName) {
        this.userName = userName;
        this.groupName = groupName;
        current.dirTree.get("home").creatFile(userName,userName,groupName, true);
        current = current.cd("./home/"+userName);
        OSConfig.userList.put(userName, this);
    }
}
