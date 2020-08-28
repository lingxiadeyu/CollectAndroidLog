package com.zhidaoauto.collectlog.enums;

//枚举定义返回编码和返回信息
public enum  ResultEnum {
    DEVICESNOTFOUNd(100,"devices not found"),
    NOCRASHFILE(101,"no crash file"),
    NOLOGS(102,"no logs");

    private Integer code;
    private String msg;
    //枚举的构造函数，能自定义编码和返回值信息
    ResultEnum(Integer code,String msg){
        this.code=code;
        this.msg=msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
