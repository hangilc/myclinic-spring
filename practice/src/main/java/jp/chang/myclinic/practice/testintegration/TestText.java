package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.practice.javafx.Record;
import jp.chang.myclinic.practice.javafx.RecordText;
import jp.chang.myclinic.practice.javafx.TextEnterForm;
import jp.chang.myclinic.practice.javafx.text.TextDisp;

class TestText extends IntegrationTestBase {

    void runAll() {
        Exam exam = new TestSelectForExam().selectWithNewPatientWithHoken();
        RecordText recordText = enterText(exam.record);
        modifyText(recordText);
    }

    void modifyText(RecordText recordText) {

    }

    RecordText enterText(Record record) {
        return enterText(record, "昨日から、のどの痛みと鼻水がある。");
    }

    RecordText enterText(Record record, String content) {
        gui(record::simulateNewTextButtonClick);
        TextEnterForm textEnterForm = waitFor(record::findTextEnterForm);
        IncrementWaiter textRecordWaiter = new IncrementWaiter(record::getLastTextId);
        gui(() -> {
            textEnterForm.setContent(content);
            textEnterForm.simulateClickEnterButton();
        });
        int newTextId = textRecordWaiter.waitForIncrement();
        RecordText newRecordText = record.findRecordText(newTextId).orElseThrow(() ->
                new RuntimeException("find record text failed.")
        );
        TextDisp textDisp = newRecordText.findTextDisp().orElseThrow(() ->
                new RuntimeException("cannot find record disp"));
        if (!textDisp.getRep().equals(content)) {
            throw new RuntimeException("Incorrect text content.");
        }
        return newRecordText;
    }

}
