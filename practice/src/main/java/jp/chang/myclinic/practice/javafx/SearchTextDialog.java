package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.dto.TextVisitDTO;
import jp.chang.myclinic.dto.TextVisitPageDTO;
import jp.chang.myclinic.dto.VisitPatientDTO;
import jp.chang.myclinic.practice.javafx.parts.PageNav;
import jp.chang.myclinic.practice.javafx.parts.SimplePageNav;
import jp.chang.myclinic.practice.javafx.parts.searchbox.BasicSearchTextInput;
import jp.chang.myclinic.practice.lib.PracticeLib;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.kanjidate.KanjiDateRepBuilder;
import jp.chang.myclinic.utilfx.HandlerFX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SearchTextDialog extends Stage {

    private static Logger logger = LoggerFactory.getLogger(SearchTextDialog.class);
    private int patientId;
    private String searchText;
    private BasicSearchTextInput textInput;
    private PageNav pageNav;
    private VBox listBox;
    private ScrollPane listScroll;

    public SearchTextDialog(int patientId) {
        this.patientId = patientId;
        setTitle("文章検索");
        VBox root = new VBox(4);
        root.getStyleClass().add("search-text-dialog");
        root.getStylesheets().addAll(
                "css/Practice.css"
        );
        root.getChildren().addAll(
                createTextInput(),
                createNav(),
                createList()
        );
        setScene(new Scene(root));
    }

    private Node createTextInput(){
        textInput = new BasicSearchTextInput();
        textInput.setOnSearchCallback(text -> {
            searchText = text;
            initSearch();
        });
        return textInput;
    }

    private Node createNav(){
        pageNav = new PageNav();
        pageNav.setRequestHandler((page, cb) -> {
            search(page)
                    .thenAccept(result -> Platform.runLater(() -> {
                        setSearchRsult(result.textVisits);
                        cb.run();
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        });
        return new SimplePageNav(pageNav);
    }

    private Node createList(){
        listBox = new VBox(4);
        ScrollPane sp = new ScrollPane(listBox);
        sp.getStyleClass().add("result-scroll");
        sp.setFitToWidth(true);
        listScroll = sp;
        return sp;
    }

    private void setSearchRsult(List<TextVisitDTO> result){
        listBox.getChildren().clear();
        result.forEach(item -> {
            VBox box = new VBox(4);
            box.setStyle("-fx-padding: 4px");
            String titleText = new KanjiDateRepBuilder(DateTimeUtil.parseSqlDateTime(item.visit.visitedAt))
                    .format1().str(" ").format6().build();
//            String titleText = DateTimeUtil.sqlDateTimeToKanji(item.visit.visitedAt,
//                    DateTimeUtil.kanjiFormatter1, DateTimeUtil.kanjiFormatter6);
            Label title = new Label(titleText);
            title.setMaxWidth(Double.MAX_VALUE);
            title.getStyleClass().add("title");
            Text content = new Text(item.text.content);
            box.getChildren().addAll(
                    title,
                    new TextFlow(content)
            );
            listBox.getChildren().add(box);
        });
        listScroll.setVvalue(0);
    }

    private void initSearch(){
        search(0)
                .thenAccept(result -> Platform.runLater(() -> {
                    setSearchRsult(result.textVisits);
                    pageNav.start(0, result.totalPages);
                }))
                .exceptionally(HandlerFX::exceptionally);
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
