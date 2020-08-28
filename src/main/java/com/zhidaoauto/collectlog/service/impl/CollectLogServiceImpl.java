package com.zhidaoauto.collectlog.service.impl;

import com.zhidaoauto.collectlog.entity.Result;
import com.zhidaoauto.collectlog.enums.ResultEnum;
import com.zhidaoauto.collectlog.service.CollectLogService;
import com.zhidaoauto.collectlog.util.DosCmd;
import com.zhidaoauto.collectlog.util.ResultUtil;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class CollectLogServiceImpl implements CollectLogService {
    DosCmd dosCmd=new DosCmd();

    //获取连接的设备号
    @Override
    public List<String> getDevices() {

        List<String> devices = dosCmd.execCmdConsole("adb devices");
        List<String> getdevicesUdid = new ArrayList<String>();
        //因为有表头，所以从2开始
        if (devices.size() > 2){
            //因为有表头所以从get(1)开始，因为有空白行，所以要减1
            for (int i=1;i<devices.size()-1;i++){
                //“\t” 分隔符是\t 空格
                String[] devicesudid = devices.get(i).split("\t");
                //获取设备状态正常 为device的设备信息
                if (devicesudid[1].trim().equals("device")){
                    //获取设备号udid，trim是过滤空格
                    getdevicesUdid.add(devicesudid[0].trim());
                }
            }
            return getdevicesUdid;
        }else {
            return null;
        }
    }


    //启动收集日志
    @Override
    public Result startCollectLogs() {
        StringBuffer sb=new StringBuffer();
        sb.append("adb logcat -c && adb logcat -v time > ");
        String osname = System.getProperty("user.dir");
        sb.append(osname+"\\log.txt");
        System.out.println(sb.toString());

        dosCmd.execCmdConsole(sb.toString());
        return ResultUtil.success("start collect logs");
    }

    //收集crash日志
    @Override
    public Result startCollectCrashLogs() {
        //先判断crash文件夹是否存在,如果存在就删除，并删除该文件夹下所有的文件
        String osname = System.getProperty("user.dir");
        File file=new File(osname+"\\crash");

        //判断crash文件存在就循环删除文件夹下所有的文件
        if (file.exists()){
            File[] files=file.listFiles();
            for (File file1:files){
                file1.delete();
            }
        }

        dosCmd.execCmdConsole("adb pull sdcard/crash "+osname+"\\crash");
        File file1=new File(osname+"\\crash");
        File[] crashfile=file1.listFiles();
        List<String> crashfileconment=new ArrayList<>();
        if (crashfile.length != 0){
            try {
                FileInputStream fis=new FileInputStream(crashfile[crashfile.length-1]);
                InputStreamReader isr=new InputStreamReader(fis,"GBK");
                BufferedReader br=new BufferedReader(isr);
                String readline=null;
                while ((readline=br.readLine())!=null){
                    crashfileconment.add(readline);
                }

                fis.close();
                isr.close();
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            return  ResultUtil.error(ResultEnum.NOCRASHFILE);
        }
        return ResultUtil.success(crashfileconment);
    }

    //根据关键字搜索
    @Override
    public Result searchKeyLog(String searchstr) {
        List<String> resultlist=new ArrayList<>();
        List<String> searchstrresultlist=new ArrayList<>();
        String osname = System.getProperty("user.dir");
        File file=new File(osname+"\\log.txt");
        try {
            FileInputStream fis=new FileInputStream(file);
            InputStreamReader isr=new InputStreamReader(fis,"GBK");
            BufferedReader br=new BufferedReader(isr);
            String readlinestr=null;


            while ((readlinestr=br.readLine()) != null){
                resultlist.add(readlinestr);
            }
            fis.close();
            isr.close();
            br.close();

            int n=resultlist.size();
            //要判断小于0的情况，如果小于0就从0添加
            for (int i=0;i<n;i++){
                if (resultlist.get(i).contains(searchstr)){

                    int j=i-10;//10可以设置成变量，因为有空格，实际获取上下5行日志，所以写成10，设置变量要乘以2
                    int m=i+10;
                    if (j<=0 && m<n){
                        for (int a=0;a<=i+10;a++){
                            searchstrresultlist.add(resultlist.get(a));
                        }
                    }
                    if (j<=0 && m>=n){
                        for (int a=0;a<n;a++){
                            searchstrresultlist.add(resultlist.get(a));
                        }
                    }
                    if (j>0 && m<n){
                        for (int a=i-10;a<=i+10;a++){
                            searchstrresultlist.add(resultlist.get(a));
                        }
                    }
                    if (j>0 && m>=n){
                        for (int a=i-10;a<n;a++){
                            searchstrresultlist.add(resultlist.get(a));
                        }
                    }

                }
            }

            //查询出来有多个重复数据，去重操作
            for (int e=0;e<searchstrresultlist.size();e++){
                for (int f=e+1;f<searchstrresultlist.size();f++){
                    if (searchstrresultlist.get(e).equals(searchstrresultlist.get(f))){
                        searchstrresultlist.remove(f);
                    }
                }
            }

            //判断搜索结果为空抛出异常
            if (searchstrresultlist.isEmpty()){
                return  ResultUtil.error(ResultEnum.NOLOGS);
            }

        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return ResultUtil.success(searchstrresultlist);
    }

    @Override
    public Result isConnected(Result result){
        //判断设备是否连接，连接启动收集日志命令，未连接提示设备未找到
        List<String> devicesList=getDevices();
        if (devicesList !=null){
            return result;
        }else {
            return ResultUtil.error(ResultEnum.DEVICESNOTFOUNd);
        }
    }
}
