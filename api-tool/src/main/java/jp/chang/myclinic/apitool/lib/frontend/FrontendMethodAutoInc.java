package jp.chang.myclinic.apitool.lib.frontend;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

public class FrontendMethodAutoInc implements FrontendMethod {

    FrontendMethodAutoInc(MethodDeclaration backendMethod, Class<?> dtoClass, Field autoIncField) {

    }

}
