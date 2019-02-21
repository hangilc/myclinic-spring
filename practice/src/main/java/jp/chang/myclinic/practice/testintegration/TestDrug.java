package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.practice.javafx.Record;
import jp.chang.myclinic.practice.javafx.drug.DrugEnterForm;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugSearchResultItem;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugSearcher;

import java.util.Optional;

class TestDrug extends IntegrationTestBase {

    private Exam exam;

    TestDrug(Exam exam) {
        this.exam = exam;
    }

    void enterNaifuku(){
        Record record = exam.record;
        gui(record::simulateNewDrugButtonClick);
        DrugEnterForm drugEnterForm = waitFor(record::findDrugEnterForm);
        IncrementWaiter searchResultWaiter = new IncrementWaiter(drugEnterForm::getSearchResultSerialId);
        gui(() -> {
            drugEnterForm.simulateSetSearchText("カロナール");
            drugEnterForm.simulateClickSearchButton();
        });
        searchResultWaiter.waitForIncrement(10);
        int calonalNaifuku = 553;
        DrugSearchResultItem item = drugEnterForm.getSearchResultItems().stream()
                .filter(result -> filterPrescExampleById(result, calonalNaifuku))
                .findFirst().orElseThrow(() -> new RuntimeException("cannot search example drug"));
        gui(() -> drugEnterForm.simulateSelectSearchResultItem(item));
        waitFor(() -> {
            DrugSearchResultItem currentItem = drugEnterForm.getCurrentInputItem();
            return Optional.ofNullable(currentItem == item ? true : null);
        });
        IncrementWaiter recordDrugWaiter = new IncrementWaiter(() ->
            record.listDrug().size());
        gui(drugEnterForm::simulateClickEnterButton);
        recordDrugWaiter.waitForIncrement(10);
    }

    private boolean filterPrescExampleById(DrugSearchResultItem result, int prescExampleId){
        if( result instanceof DrugSearcher.ExampleItem){
            DrugSearcher.ExampleItem exampleItem = (DrugSearcher.ExampleItem)result;
            if( exampleItem.getExample().prescExample.prescExampleId == prescExampleId ){
                return true;
            }
        }
        return false;
    }

}
