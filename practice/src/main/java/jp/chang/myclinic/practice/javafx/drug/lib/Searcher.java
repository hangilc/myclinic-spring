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

public class Searcher {

    private static Logger logger = LoggerFactory.getLogger(Searcher.class);

    private Searcher() {
    }

    private static class MasterItem implements SearchResultItem {
        private IyakuhinMasterDTO master;
        private Consumer<IyakuhinMasterDTO> onSelectHandler;

        MasterItem(IyakuhinMasterDTO master, Consumer<IyakuhinMasterDTO> onSelectHandler){
            this.master = master;
            this.onSelectHandler = onSelectHandler;
        }

        @Override
        public String getRep() {
            return master.name;
        }

        @Override
        public void onSelect() {
            onSelectHandler.accept(master);
        }
    }

    public static CompletableFuture<List<SearchResultItem>> searchMaster(String text, LocalDate at,
                                                                         Consumer<IyakuhinMasterDTO> onSelectHandler) {
        return Service.api.searchIyakuhinMaster(text, at.toString())
                .thenApply(result -> result.stream().map(m -> new MasterItem(m, onSelectHandler)).collect(Collectors.toList()));
    }

    private static class ExampleItem implements SearchResultItem {
        private PrescExampleFullDTO exampleFull;
        private Consumer<PrescExampleFullDTO> onSelectHandler;

        ExampleItem(PrescExampleFullDTO exampleFull, Consumer<PrescExampleFullDTO> onSelectHandler) {
            this.exampleFull = exampleFull;
            this.onSelectHandler = onSelectHandler;
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

        @Override
        public void onSelect() {
            onSelectHandler.accept(exampleFull);
        }
    }

    public static CompletableFuture<List<SearchResultItem>> searchExample(String text,
                                                                          Consumer<PrescExampleFullDTO> onSelectHandler){
        return Service.api.searchPrescExample(text)
                .thenApply(result -> result.stream().map(e -> new ExampleItem(e, onSelectHandler)).collect(Collectors.toList()));
    }

    public static CompletableFuture<List<SearchResultItem>> listAllExamples(Consumer<PrescExampleFullDTO> onSelectHandler){
        return Service.api.listAllPrescExample()
                .thenApply(result -> result.stream().map(e -> new ExampleItem(e, onSelectHandler)).collect(Collectors.toList()));
    }

    private static class DrugItem implements SearchResultItem {
        private DrugFullDTO drugFull;
        private Consumer<DrugFullDTO> onSelectHandler;

        public DrugItem(DrugFullDTO drugFull, Consumer<DrugFullDTO> onSelectHandler) {
            this.drugFull = drugFull;
            this.onSelectHandler = onSelectHandler;
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

        @Override
        public void onSelect() {
            onSelectHandler.accept(drugFull);
        }
    }

    public static CompletableFuture<List<SearchResultItem>> searchDrug(String text, int patientId,
                                                                       Consumer<DrugFullDTO> onSelectHandler){
        return Service.api.searchPrevDrug(text, patientId)
                .thenApply(result -> result.stream().map(d -> new DrugItem(d, onSelectHandler)).collect(Collectors.toList()));
    }

}

