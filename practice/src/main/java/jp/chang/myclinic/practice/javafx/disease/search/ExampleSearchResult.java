package jp.chang.myclinic.practice.javafx.disease.search;

import jp.chang.myclinic.dto.DiseaseExampleDTO;
import jp.chang.myclinic.practice.lib.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
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
        return example.label != null ? example.label : example.byoumei;
    }

    @Override
    public void onSelect(SearchBox searchBox){
        searchBox.triggerExampleSelect(example);
    }

}
