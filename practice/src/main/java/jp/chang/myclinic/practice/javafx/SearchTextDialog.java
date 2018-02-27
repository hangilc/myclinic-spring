package jp.chang.myclinic.practice.javafx;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.TextVisitPageDTO;
import jp.chang.myclinic.practice.Service;
import jp.chang.myclinic.practice.javafx.parts.SimplePageNav;
import jp.chang.myclinic.practice.javafx.parts.searchbox.BasicSearchTextInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.concurrent.CompletableFuture;

public class SearchTextDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(SearchTextDialog.class);
    private int patientId;
    private String searchText;
    BasicSearchTextInput textInput;
    SimplePageNav pageNav;

    public SearchTextDialog(int patientId) {
        this.patientId = patientId;
        setTitle("文章検索");
        VBox root = new VBox(4);
        root.getStyleClass().add("search-text-dialog");
        root.getStylesheets().addAll(
                "css/Practice.css"
        );
        textInput.setOnSearchCallback(text -> {
            searchText = text;
            initSearch();
        });
        root.getChildren().addAll(
                createTextInput(),
                createNav()
        );
        setScene(new Scene(root));
    }

    private Node createTextInput(){
        textInput = new BasicSearchTextInput();
        return textInput;
    }

    private void initSearch(){
        page = 0;
        search(page);
    }

    private CompletableFuture<TextVisitPageDTO> search(int page){
        if( searchText == null || searchText.isEmpty() ){
            TextVisitPageDTO dto = new TextVisitPageDTO();
            dto.page = page;
            dto.textVisits = Collections.emptyList();
            dto.totalPages = 0;
            return CompletableFuture.completedFuture(dto);
        } else {
            return Service.api.searchTextByPage(patientId, searchText, page);
        }
    }

}
