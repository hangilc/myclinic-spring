package jp.chang.myclinic.apitool.lib.frontend;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrontendMethodTxProc extends FrontendMethodBase {

    FrontendMethodTxProc(MethodDeclaration backendMethod) {
        super(backendMethod);
    }

    @Override
    public MethodDeclaration createFrontendBackendMethod() {
        return createFrontendBackendMethod("txProc");
    }
}
