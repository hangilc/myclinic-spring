package jp.chang.myclinic.backend.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface BackendAsyncClientOption {

    boolean convertLocalDateTime() default false;
    String composeResult() default "";

}