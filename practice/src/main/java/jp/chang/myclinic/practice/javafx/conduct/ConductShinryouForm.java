package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import jp.chang.myclinic.dto.ShinryouMasterDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.HandlerFX;
import jp.chang.myclinic.practice.javafx.parts.SearchBox;
import jp.chang.myclinic.practice.javafx.parts.WorkForm;

import java.util.List;
import java.util.function.Consumer;

public class ConductShinryouForm extends WorkForm {

    private String at;

    public ConductShinryouForm(String at){
        super("診療行為追加");
        this.at = at;
        ShinryouInput shinryouInput = new ShinryouInput();
        SearchBox<ShinryouMasterDTO> searchBox = new SearchBox<ShinryouMasterDTO>(){
            @Override
            protected String convert(ShinryouMasterDTO result) {
                return result.name;
            }

            @Override
            protected void search(String text, Consumer<List<ShinryouMasterDTO>> cb) {
                Service.api.searchShinryouMaster(text, at)
                        .thenAccept(list -> Platform.runLater(() -> cb.accept(list)))
                        .exceptionally(HandlerFX::exceptionally);
            }

            @Override
            protected void onSelect(ShinryouMasterDTO selection) {
                shinryouInput.setMaster(selection);
            }
        };
        getChildren().addAll(
                shinryouInput,
                searchBox
        );
    }
}
