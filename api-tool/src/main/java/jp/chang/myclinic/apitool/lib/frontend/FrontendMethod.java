package jp.chang.myclinic.apitool.lib.frontend;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.Type;

public interface FrontendMethod {

    MethodDeclaration createFrontendMethod();
    MethodDeclaration createFrontendBackendMethod();
    MethodDeclaration createFrontendAdapterMethod();
    MethodDeclaration createFrontendProxyMethod();

}
