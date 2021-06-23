package com.wangli.gulimall.product.exception;

import com.wangli.common.exception.BizCodeEnum;
import com.wangli.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WangLi
 * @date 2021/6/6 18:45
 * @description
 */
//@ControllerAdvice
//@ResponseBody
@Slf4j
@RestControllerAdvice(basePackages = "com.wangli.gulimall.product.app")
public class GulimallExceptionControllerAdvice {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handlValidException(MethodArgumentNotValidException e) {
//        log.error("数据校验出现问题{},异常类型:{}", e.getMessage(), e.getClass());
        Map<String, String> map = new HashMap<>();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getFieldErrors().forEach(fieldError -> {
            String field = fieldError.getField();
            String defaultMessage = fieldError.getDefaultMessage();
            map.put(field, defaultMessage);
        });
        return R.error(BizCodeEnum.VALID_EXCEPTION.getCode(), BizCodeEnum.VALID_EXCEPTION.getMsg()).put("data", map);
    }

    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable) {
        log.error("handleException error:{}", throwable.getMessage(), throwable);
        return R.error(BizCodeEnum.UNKONW_EXCEPTION.getCode(), BizCodeEnum.UNKONW_EXCEPTION.getMsg());
    }
}
