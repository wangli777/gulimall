package com.wangli.common.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @author WangLi
 * @date 2021/6/6 19:56
 * @description 自定义校验注解，校验字段值必须为0或1
 * @Constraint(validatedBy = {}) 可以指定如何进行校验
 */
@Documented
@Constraint(validatedBy = {OneOrZeroConstraintValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OneOrZero {
    String message() default "必须提交指定的值";
//    String message() default "{com.wangli.common.valid.OneOrZero.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int[] values() default {};
}
