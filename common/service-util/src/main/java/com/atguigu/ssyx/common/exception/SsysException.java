package com.atguigu.ssyx.common.exception;

import com.atguigu.ssyx.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-08-26 00:11
 * @Description:自定义异常
 **/
@Data
public class SsysException extends RuntimeException{
    //异常状态码
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     * @param code
     * @param message
     */
    public SsysException(Integer code,String message){
        //调用Exception的构造方法并且传入参数message
        super(message);
        this.code = code;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public SsysException(ResultCodeEnum resultCodeEnum){
        //调用Exception的构造方法并且传入参数message
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
    }

    @Override
    public String toString() {
        return "SsysException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
