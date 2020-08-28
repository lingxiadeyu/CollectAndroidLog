package com.zhidaoauto.collectlog.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
/*
执行cmd命令
 */
public class DosCmd {
    private Log log = LogFactory.getLog(DosCmd.class);
    String osname = System.getProperty("os.name");

    //执行dos命令有返回结果，用来判断设备是否连接、或者连接设备的信息、抓取设备的日志
    public List<String> execCmdConsole(String cmdString){
        //定义一个list，用来接收输入dos命令后返回的信息
        List<String> dosres = new ArrayList<String>();
        Process process = null;

        try{

            //判断是mac还是windows
            if (osname.toLowerCase().contains("mac")){
                String[] command = {"/bin/sh","-c",cmdString};
                process = Runtime.getRuntime().exec(command);

            }else if (osname.toLowerCase().contains("win")){
                //执行dos命令，并把返回结果存储起来
                process = Runtime.getRuntime().exec("cmd /c "+cmdString);
            }

            //使用流的方式对结果解析
            InputStream in = process.getInputStream();
            BufferedReader bf = new BufferedReader(new InputStreamReader(in,"GBK"));
            String line = null;
            //使用while循环，如果没有结果可存储了就为null 就为false
            while ((line = bf.readLine()) != null){
                //没有办法过滤空行，所以就直接加入
//                if (bf.readLine().equals(null)){
//                    //返回的结果中有空行，过滤空行
//                }else {
//                    //存放到list中并返回
//                    dosres.add(line);
//                }
                dosres.add(line);
            }
            process.waitFor();
            process.destroy();
            log.debug("get result of command after execute dos command "+cmdString+" Succeed ");

        }catch (Exception e){
            log.error("get result of command after execute dos command "+cmdString+" Failed ",e);
        }
        return dosres;
    }
}
