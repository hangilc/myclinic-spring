package jp.chang.myclinic.practice.javafx.drug.lib;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.consts.DrugCategory;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.DrugUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DrugSearcher {

    private static Logger logger = LoggerFactory.getLogger(DrugSearcher.class);

    private DrugSearcher() {
    }

    public static class MasterItem implements DrugSearchResultItem {
        private IyakuhinMasterDTO master;
        private Consumer<IyakuhinMasterDTO> onSelectHandler;

        MasterItem(IyakuhinMasterDTO master, Consumer<IyakuhinMasterDTO> onSelectHandler){
            this.master = master;
            this.onSelectHandler = onSelectHandler;
        }

        IyakuhinMasterDTO getMaster(){
            return master;
        }

        @Override
        public String getRep() {
            return master.name;
        }

    }

    public static CompletableFuture<List<DrugSearchResultItem>> searchMaster(String text, LocalDate at,
                                                                             Consumer<IyakuhinMasterDTO> onSelectHandler) {
        return Service.api.searchIyakuhinMaster(text, at.toString())
                .thenApply(result -> result.stream().map(m -> new MasterItem(m, onSelectHandler)).collect(Collectors.toList()));
    }

    public static class ExampleItem implements DrugSearchResultItem {
        private PrescExampleFullDTO exampleFull;
        private Consumer<PrescExampleFullDTO> onSelectHandler;

        ExampleItem(PrescExampleFullDTO exampleFull, Consumer<PrescExampleFullDTO> onSelectHandler) {
            this.exampleFull = exampleFull;
            this.onSelectHandler = onSelectHandler;
        }

        public PrescExampleFullDTO getExample(){
            return exampleFull;
        }

        @Override
        public String getRep() {
            IyakuhinMasterDTO master = exampleFull.master;
            PrescExampleDTO example = exampleFull.prescExample;
            DrugCategory category = DrugCategory.fromCode(example.category);
            if( category == null ){
                logger.error("Invalid category: " + example.category);
                category = DrugCategory.Naifuku;
            }
            double amount;
            try {
                amount = Double.parseDouble(example.amount);
            } catch(NumberFormatException ex) {
                logger.error("Invalid amount: " + example.amount);
                amount = 0;
            }
            return DrugUtil.drugRep(category, master.name, amount, master.unit, example.usage, example.days);
        }

    }

    public static CompletableFuture<List<DrugSearchResultItem>> searchExample(String text,
                                                                              Consumer<PrescExampleFullDTO> onSelectHandler){
        return Service.api.searchPrescExample(text)
                .thenApply(result -> result.stream().map(e -> new ExampleItem(e, onSelectHandler)).collect(Collectors.toList()));
    }

    public static CompletableFuture<List<DrugSearchResultItem>> listAllExamples(Consumer<PrescExampleFullDTO> onSelectHandler){
        return Service.api.listAllPrescExample()
                .thenApply(result -> result.stream().map(e -> new ExampleItem(e, onSelectHandler)).collect(Collectors.toList()));
    }

    public static class DrugItem implements DrugSearchResultItem {
        private DrugFullDTO drugFull;
        private Consumer<DrugFullDTO> onSelectHandler;

        DrugItem(DrugFullDTO drugFull, Consumer<DrugFullDTO> onSelectHandler) {
            this.drugFull = drugFull;
            this.onSelectHandler = onSelectHandler;
        }

        DrugFullDTO getDrug(){
            return drugFull;
        }

        @Override
        public String getRep() {
            IyakuhinMasterDTO master = drugFull.master;
            DrugDTO drug = drugFull.drug;
            DrugCategory category = DrugCategory.fromCode(drug.category);
            if( category == null ){
                logger.error("Invalid category: " + drug.category);
                category = DrugCategory.Naifuku;
            }
            return DrugUtil.drugRep(category, master.name, drug.amount, master.unit, drug.usage, drug.days);
        }

    }

    static CompletableFuture<List<DrugSearchResultItem>> searchDrug(String text, int patientId,
                                                                           Consumer<DrugFullDTO> onSelectHandler){
        return Service.api.searchPrevDrug(text, patientId)
                .thenApply(result -> result.stream().map(d -> new DrugItem(d, onSelectHandler)).collect(Collectors.toList()));
    }

}

