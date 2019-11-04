package jp.chang.myclinic.apitool;

import com.github.javaparser.ast.body.MethodDeclaration;
import jp.chang.myclinic.apitool.lib.PracticeLogKindList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine.*;

@Command(name = "practice-log-xml")
class PracticeLogXml implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(PracticeLogXml.class);

    @Override
    public void run() {
        for (Class<?> cls : PracticeLogKindList.kindMap.keySet()) {
            String kind = PracticeLogKindList.kindMap.get(cls);
            System.out.println(kind);
        }
    }
}
