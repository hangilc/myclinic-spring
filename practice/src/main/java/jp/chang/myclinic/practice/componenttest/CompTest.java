package jp.chang.myclinic.practice.componenttest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CompTest {
    String name() default "";
    boolean excludeFromBatch() default false;
}
