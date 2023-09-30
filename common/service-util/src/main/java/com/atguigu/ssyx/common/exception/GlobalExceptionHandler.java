package com.atguigu.ssyx.common.exception;

import com.atguigu.ssyx.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @program: guigu-ssyx-parent
 * @author: fzh
 * @create: 2023-08-26 00:06
 * @Description:全局异常处理
 **/
//AOP
@ControllerAdvice//全局异常处理的方式
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)//异常处理器
    @ResponseBody//返回json数据
    public Result error(Exception e){
        e.printStackTrace();
        return Result.fail(null);
    }

    /**
     * 自定义异常处理
     * @return
     */
    @ExceptionHandler(SsysException.class)
    public Result error(SsysException ssysException){
        return Result.build(null,ssysException.getCode(),ssysException.getMessage());
    }
}
