package com.zhidaoauto.collectlog.controller;

import com.zhidaoauto.collectlog.entity.Result;
import com.zhidaoauto.collectlog.enums.ResultEnum;
import com.zhidaoauto.collectlog.service.CollectLogService;
import com.zhidaoauto.collectlog.util.DosCmd;
import com.zhidaoauto.collectlog.util.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/*
实现的内容：
1、ok--设备没连上提示要连接设备
2、日志文件过大时要重新创建一个文件
3、ok--搜索不到内容时的提示
4、ok--判断是windows系统还是mac系统，解决路径目录的问题
5、ok--通过swssweui显示接口
6、no--（没意义）通过adb命令获取设备的信息，并打印出来
7、no--（需要前端）查询出来的日志根据颜色进行标注关键字
8、ok--把接口的实现放到service层
9、ok--实现根据关键字筛选后能选择该行的上下前5行信息
10、ok--实现所有的接口返回的格式是一样的
11、ok--到sdcard/crash目录下查看崩溃的日志
12、本地搭建前端框架调接口
 */
@RestController
@Api(value = "/",description = "抓取日志接口")
public class CollectLogController {


    @Autowired
    CollectLogService collectLogService;

    @RequestMapping(value = "/startcollectlogs",method = RequestMethod.GET)
    @ApiOperation(value = "启动收集日志",httpMethod = "GET",notes = "启动收集日志保存到项目根目录log.txt中")
    public Result startCollectLogs(){
        //判断设备是否连接，连接启动收集日志命令，未连接提示设备未找到
        List<String> devicesList=collectLogService.getDevices();
        if (devicesList !=null){
            return collectLogService.startCollectLogs();
        }else {
            return ResultUtil.error(ResultEnum.DEVICESNOTFOUNd);
        }
    }

    @RequestMapping(value = "/collectcrashlogs",method = RequestMethod.GET)
    @ApiOperation(value = "收集crash日志",httpMethod = "GET",notes = "收集最新的crash日志")
    public Result startCollectCrashLogs(){
        //判断设备是否连接，连接启动收集日志命令，未连接提示设备未找到
        List<String> devicesList=collectLogService.getDevices();
        if (devicesList !=null){
            return collectLogService.startCollectCrashLogs();
        }else {
            return ResultUtil.error(ResultEnum.DEVICESNOTFOUNd);
        }

    }

    @RequestMapping(value = "/searchkeylog/{searchstr}",method = RequestMethod.GET)
    @ApiOperation(value = "筛选日志信息",httpMethod = "GET",notes = "筛选日志后返回")
    public Result searchKeyLog(@PathVariable("searchstr")  String searchstr){
        //判断设备是否连接，连接启动收集日志命令，未连接提示设备未找到
        List<String> devicesList=collectLogService.getDevices();
        if (devicesList !=null){
            return collectLogService.searchKeyLog(searchstr);
        }else {
            return ResultUtil.error(ResultEnum.DEVICESNOTFOUNd);
        }



    }

}
