package jp.chang.myclinic.mastermap2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface MasterNameMap {
    public String[] candidates() default "";
}
