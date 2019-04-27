package jp.chang.myclinic.apitool.lib.frontend;

import com.github.javaparser.ast.body.MethodDeclaration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FrontendMethodTx extends FrontendMethodBase {

    FrontendMethodTx(MethodDeclaration backendMetho) {
        super(backendMetho);
    }

    @Override
    public MethodDeclaration createFrontendBackendMethod() {
        return createFrontendBackendMethod("tx");
    }
}
