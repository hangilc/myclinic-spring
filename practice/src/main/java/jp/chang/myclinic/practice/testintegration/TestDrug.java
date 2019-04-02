package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.dto.DrugFullDTO;
import jp.chang.myclinic.practice.javafx.Record;
import jp.chang.myclinic.practice.javafx.RecordDrug;
import jp.chang.myclinic.practice.javafx.drug.DrugEnterForm;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugSearchResultItem;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugSearcher;
import jp.chang.myclinic.util.DrugUtil;

import java.util.Optional;

class TestDrug extends IntegrationTestBase {

    void enterNaifuku(Record record){
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
        RecordDrug enteredRecordDrug = getLast(record.listDrug());
        DrugFullDTO enteredDrug = Context.getInstance().getFrontend().getDrugFull(enteredRecordDrug.getDrugId()).join();
        String rep = DrugUtil.drugRep(enteredDrug);
        confirm(enteredRecordDrug.isDisplaying() && enteredRecordDrug.getDisplayingText().contains(rep));
        gui(drugEnterForm::simulateClickCloseButton);
        waitForNot(record::findDrugEnterForm);
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
