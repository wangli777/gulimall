package com.wangli.common.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author WangLi
 * @date 2021/6/6 20:05
 * @description 自定义校验器，
 */
public class OneOrZeroConstraintValidator implements ConstraintValidator<OneOrZero, Integer> {
    private Set<Integer> set = new HashSet<>();

    /**
     * 初始化方法
     */
    @Override
    public void initialize(OneOrZero constraintAnnotation) {
        //获取注解上的值0,1
        int[] values = constraintAnnotation.values();
        for (int value : values) {
            set.add(value);
        }
    }

    /**
     * @param integer                    需要校验的值
     * @param constraintValidatorContext 校验的上下文信息
     * @return
     */
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
//        校验传入的值是否符合要求
        return set.contains(integer);
    }
}
