package com.atguigu.ssyx.common.result;

import lombok.Data;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-08-24 21:22
 * @Description:统一返回结果类
 **/
@Data
public class Result<T> {
    //状态码
    private Integer code;
    //信息
    private String message;
    //数据
    private T data;

    //构造私有化
    public Result(){}

    public static<T> Result<T> build(T data,Integer code,String message){
        //设置Result对象，即本类的对象
        Result<T> result = new Result<>();
        //判断返回结果中是否有数据
        if(data != null){
            //则给对象中添加数据
            result.setData(data);
        }
        //给其他对象设置值
        result.setCode(code);
        result.setMessage(message);
        //返回设置值之后的对象
        return result;
    }

    //<T>表示data的类型，Result<T>表示表示包含 T 类型的数据和 ResultCodeEnum 类型的状态码的结果
    public static<T> Result<T> build(T data,ResultCodeEnum resultCodeEnum){
        //设置Result对象，即本类的对象
        Result<T> result = new Result<>();
        //判断返回结果中是否有数据
        if(data != null){
            //则给对象中添加数据
            result.setData(data);
        }
        //给其他对象设置值
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        //返回设置值之后的对象
        return result;
    }

    //<T>表示data的类型，Result<T>表示表示包含 T 类型的数据和 ResultCodeEnum 类型的状态码的结果
    public static<T> Result<T> build(ResultCodeEnum resultCodeEnum){
        //设置Result对象，即本类的对象
        Result<T> result = new Result<>();
        //给其他对象设置值
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        //返回设置值之后的对象
        return result;
    }


    //成功的方法
    public static<T> Result<T> ok(T data){
        return build(data,ResultCodeEnum.SUCCESS);
    }

    //成功但是不用添加数据
    public static<T> Result<T> ok(){
        return build(ResultCodeEnum.SUCCESS);
    }
    //失败的方法
    public static<T> Result<T> fail(T data){
        return build(data,ResultCodeEnum.FAIL);
    }


}
