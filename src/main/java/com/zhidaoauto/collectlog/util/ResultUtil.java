package com.zhidaoauto.collectlog.util;

import com.zhidaoauto.collectlog.entity.Result;
import com.zhidaoauto.collectlog.enums.ResultEnum;

//定义成功和失败的返回格式
public class ResultUtil {
    public static Result success(Object object){

        Result result=new Result();
        result.setCode(200);
        result.setMsg("成功");
        //根据传来的值返回成功的返回信息
        result.setData(object);
        return result;
    }
    //succes方法object传值为空，返回的结果为null
    public static Result success(){
        return success(null);
    }
    public static Result error(ResultEnum resultEnum){

        Result result=new Result();
        //根据传值不同的code和msg代表不同的错误值
        result.setCode(resultEnum.getCode());
        result.setMsg(resultEnum.getMsg());
        //失败时data不设置值，默认为null
        return result;
    }
}
