package jp.chang.myclinic.apitool.lib.frontend;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import jp.chang.myclinic.apitool.lib.DtoClassList;
import jp.chang.myclinic.apitool.lib.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class FrontendMethods {

    private static Helper helper = Helper.getInstance();

    public static FrontendMethod createFrontendMethod(MethodDeclaration backendMethod) {
        String methodName = backendMethod.getNameAsString();
        if (methodName.startsWith("enter")) {
            Helper.AutoIncInfo autoIncInfo = helper.scanAutoInc(backendMethod.getParameters());
            if (autoIncInfo.autoIncClass != null) {
                if (!backendMethod.getType().isVoidType()) {
                    System.err.println("Non-void return type in auto-inc insert method: " +
                            backendMethod);
                    System.exit(1);
                }
                return new FrontendMethodAutoInc(backendMethod, autoIncInfo.param,
                        autoIncInfo.autoIncClass, autoIncInfo.fieldNames);
            } else {
                if (backendMethod.getType().isVoidType()) {
                    return new FrontendMethodTxProc(backendMethod);
                } else {
                    return new FrontendMethodTx(backendMethod);
                }
            }
        } else if (methodName.startsWith("get") || methodName.startsWith("list") ||
                methodName.startsWith("search") || methodName.startsWith("find") ||
                methodName.startsWith("count") || methodName.startsWith("resolve") ||
                methodName.startsWith("batchResolve")) {
            return new FrontendMethodQuery(backendMethod);
        } else {
            if (backendMethod.getType().isVoidType()) {
                return new FrontendMethodTxProc(backendMethod);
            } else {
                return new FrontendMethodTx(backendMethod);
            }
        }
    }

}
