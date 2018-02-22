package jp.chang.myclinic.practice.javafx.disease.search;

import javafx.application.Platform;
import jp.chang.myclinic.dto.DiseaseExampleDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.GuiUtil;
import jp.chang.myclinic.practice.javafx.HandlerFX;
import jp.chang.myclinic.practice.javafx.disease.add.DiseaseInput;
import jp.chang.myclinic.practice.lib.CFUtil;
import jp.chang.myclinic.practice.lib.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class ExampleSearchResult implements DiseaseSearchResultModel {

    private static Logger logger = LoggerFactory.getLogger(ExampleSearchResult.class);
    private DiseaseExampleDTO example;
    private Supplier<Result<LocalDate, List<String>>> dateSupplier;

    public ExampleSearchResult(DiseaseExampleDTO example, Supplier<Result<LocalDate, List<String>>> dateSupplier) {
        this.example = example;
        this.dateSupplier = dateSupplier;
    }

    @Override
    public String rep() {
        return example.label;
    }

    @Override
    public void onSelect(SearchBox searchBox){
        searchBox.triggerExampleSelect(example);
    }

}
