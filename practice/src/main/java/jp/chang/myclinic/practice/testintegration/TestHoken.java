package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.practice.javafx.RecordHoken;
import jp.chang.myclinic.util.ShahokokuhoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class TestHoken extends IntegrationTestBase {

    private Exam exam;

    TestHoken(Exam exam) {
        this.exam = exam;
    }

    void confirmHokenShahokokuho(ShahokokuhoDTO shahokokuho){
        RecordHoken recordHoken = exam.record.findRecordHoken().orElseThrow(() ->
                new RuntimeException("cannot find hoken recoord"));
        String rep = ShahokokuhoUtil.rep(shahokokuho);
        confirm(recordHoken.getDispText().equals(rep));
    }

}
