package jp.chang.myclinic.practice.javafx.disease;

import jp.chang.myclinic.practice.javafx.GuiUtil;
import jp.chang.myclinic.practice.javafx.HandlerFX;
import jp.chang.myclinic.practice.javafx.parts.SearchBoxOld;
import jp.chang.myclinic.practice.lib.Result;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class DiseaseSearchBox extends SearchBoxOld<DiseaseSearchResult> {

    private DiseaseSearchInputBox inputBox;

    public DiseaseSearchBox(Supplier<Result<LocalDate, List<String>>> dateSupplier) {
        setSearcher(t -> {
            Result<LocalDate,List<String>> result = dateSupplier.get();
            if( result.hasValue() ){
                return inputBox.getSearcher().search(t, result.getValue().toString());
            } else {
                GuiUtil.alertError("開始日の設定が不適切です。");
                return CompletableFuture.completedFuture(Collections.emptyList());
            }

        });
        setConverter(DiseaseSearchResult::rep);
    }

    @Override
    protected InputBox createInputBox() {
        inputBox = new DiseaseSearchInputBox();
        inputBox.setOnTextCallback(t -> {
            inputBox.getSearcher().search(t, at)
                    .thenAccept(System.out::println)
                    .exceptionally(HandlerFX::exceptionally);
        });
        return inputBox;
    }
}
