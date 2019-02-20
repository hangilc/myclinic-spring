package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.practice.javafx.Record;
import jp.chang.myclinic.practice.javafx.RecordText;
import jp.chang.myclinic.practice.javafx.TextEnterForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

class TestText extends IntegrationTestBase {

    private Exam exam;

    TestText(Exam exam) {
        this.exam = exam;
    }

    void runAll(){
        testEnter();
    }

    private void testEnter(){
        Record record = exam.record;
        gui(record::simulateNewTextButtonClick);
        TextEnterForm textEnterForm = waitFor(record::findTextEnterForm);
        int lastTextId = record.getLastTextId();
        String text = "昨日から、のどの痛みと鼻水がある。";
        gui(() -> {
            textEnterForm.setContent(text);
            textEnterForm.simulateClickEnterButton();
        });
        int newTextId = waitFor(10, () -> {
            List<Integer> textIds = record.listTextId();
            for (Integer id : textIds) {
                if (id > lastTextId) {
                    return Optional.of(id);
                }
            }
            return Optional.empty();
        });
        RecordText newRecordText = record.findRecordText(newTextId).orElseThrow(() ->
                new RuntimeException("find record text failed.")
        );
        if (!newRecordText.getContentRep().equals(text)) {
            throw new RuntimeException("Incorrect text content.");
        }

    }

}
