package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.practice.javafx.Record;
import jp.chang.myclinic.practice.javafx.shinryou.AddRegularForm;

class TestShinryou extends IntegrationTestBase {

    private Exam exam;

    TestShinryou(Exam exam) {
        this.exam = exam;
    }

    void enterForSimpleExam(){
        Record record = exam.record;
        gui(record::simulateAddRegularShinryouClick);
        AddRegularForm form = waitFor(record::findAddRegularForm);
        String[] items = new String[]{
                "初診", "処方料",  "内服調剤", "調基", "薬剤情報提供"
        };
        IncrementWaiter recordShinryouWaiter =
                new IncrementWaiter(() -> record.listShinryou().size())
                .setRequiredIncrement(items.length);
        gui(() -> {
            for(String item: items){
                form.simulateSelectItem(item);
            }
            form.simulateClickEnterButton();
        });
        recordShinryouWaiter.waitForIncrement(10);
    }

}
