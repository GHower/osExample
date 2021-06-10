package kernel;

import config.OSConfig;
import file_system.BinCommands;

/**
 * 命令解释器，用来匹配命令
 */
public class CommendInterpreter {
    public static void CommendToProcess(String commend){
        String[] strings = commend.split(" ");
        if(strings.length<=0)return;
        switch (strings[0]) {
            case "mkdir":
                BinCommands.Command_mkdir(OSConfig.root,strings[1]);
                break;
            case "touch":
                BinCommands.Command_touch(OSConfig.root,strings[1]);
                break;
            case "rm":
                BinCommands.Command_rm(OSConfig.root,strings[1]);
                break;
            case "ls":
                BinCommands.Command_ls(OSConfig.root,false);
                break;
            case "chmod":
                BinCommands.Command_chmod(OSConfig.root,strings[1]);
                break;
            case "read":
                BinCommands.Command_read(OSConfig.root,strings[1]);
                break;
            case "write":
                BinCommands.Command_write(OSConfig.root,strings[1]);
                break;
            default:
                System.out.println("没有这个命令");
        }
    }
}
