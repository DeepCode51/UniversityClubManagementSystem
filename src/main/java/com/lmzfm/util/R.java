package com.lmzfm.util;



import java.io.Serializable;
import java.util.List;

/**
 * 返回前端 数据封闭类
 */
public class R implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String msg;
    private Object data;
    private Long count; // 分页信息：总条数

    public R() { }

    private R(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data=data;
    }

    private R(int code, String msg, Object data,long count) {
        this.code = code;
        this.msg = msg;
        this.data=data;
        this.count=count;
    }

    public static R ok() {
        return new R(Constants.OK_CODE, Constants.OK_MSG, null);
    }

    public static R ok(Object data) {
        return new R(Constants.OK_CODE, Constants.OK_MSG, data);
    }

    public static R ok(String msg,long count,Object data) {
        return new R(Constants.OK_CODE, Constants.OK_MSG, data,count);
    }

    public static R ok(String msg, Object data) {
        return new R(Constants.OK_CODE, msg, data);
    }

    public static R fail(String msg) {
        return new R(Constants.FAIL_CODE, msg, null);
    }

    public static R fail(int errorCode, String msg) {
        return new R(errorCode, msg, null);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getData() {
        return data;
    }

    public Long getCount() {
        return count;
    }

    public R setCount(Long count) {
        this.count =count;
        return this;
    }

}
