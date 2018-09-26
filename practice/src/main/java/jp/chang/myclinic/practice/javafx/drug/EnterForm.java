package jp.chang.myclinic.practice.javafx.drug;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugEnterInput;
import jp.chang.myclinic.practice.javafx.drug.lib.DrugSearchMode;
import jp.chang.myclinic.practice.javafx.drug.lib.SearchModeChooser;
import jp.chang.myclinic.practice.javafx.drug.lib.SearchTextInput;
import jp.chang.myclinic.practice.javafx.drug.lib.SearchResult;

public class EnterForm extends VBox {

    //private static Logger logger = LoggerFactory.getLogger(EnterForm.class);
    private DrugEnterInput input = new DrugEnterInput();
    private SearchModeChooser searchModeChooser = new SearchModeChooser(
            DrugSearchMode.Master, DrugSearchMode.Example, DrugSearchMode.Previous
    );

    public EnterForm(VisitDTO visit){
        super(4);
        getStyleClass().add("drug-form");
        getStyleClass().add("form");
        SearchTextInput searchTextInput = new SearchTextInput();
        searchTextInput.setHandler(this::doSearch);
        searchModeChooser.setValue(DrugSearchMode.Example);
        HBox searchModeBox = new HBox(4);
        searchModeBox.getChildren().addAll(searchModeChooser.getButtons());
        SearchResult searchResult = new SearchResult();
        getChildren().addAll(
                createTitle("新規処方の入力"),
                input,
                createCommands(),
                searchTextInput,
                searchModeBox,
                searchResult
        );
    }

    private void doSearch(String text){

    }

    private Node createTitle(String text) {
        Label title = new Label(text);
        title.setMaxWidth(Double.MAX_VALUE);
        title.getStyleClass().add("title");
        return title;
    }

    protected void onClose(){

    }

    private Node createCommands() {
        HBox hbox = new HBox(4);
        hbox.setAlignment(Pos.CENTER_LEFT);
        hbox.getStyleClass().add("commands");
        Button enterButton = new Button("入力");
        Button closeButton = new Button("閉じる");
        Hyperlink clearLink = new Hyperlink("クリア");
        enterButton.setOnAction(evt -> doEnter());
        closeButton.setOnAction(evt -> onClose());
        clearLink.setOnAction(evt -> doClear());
        hbox.getChildren().addAll(
                enterButton,
                closeButton,
                clearLink
        );
        return hbox;
    }

    private void doEnter(){

    }

    private void doClear(){

    }


//    private CheckBox daysFixedCheck = new CheckBox("固定");
//
//    public EnterForm(VisitDTO visit) {
//        super(visit, "新規処方の入力");
//        daysFixedCheck.setSelected(true);
//        getInput().addToDaysRow(daysFixedCheck);
//    }
//
//    @Override
//    Set<Input.SetOption> getSetOptions() {
//        Set<Input.SetOption> opts = new HashSet<Input.SetOption>();
//        if (daysFixedCheck.isSelected()) {
//            opts.add(Input.SetOption.FixedDays);
//        }
//        return opts;
//    }
//
//    @Override
//    void doEnter() {
//        DrugDTO drug = getInput().createDrug(getVisitId(), 0);
//        if( drug.drugId != 0 ){
//            throw new RuntimeException("drugId is not null.");
//        }
//        Service.api.enterDrug(drug)
//                .thenCompose(Service.api::getDrugFull)
//                .thenAccept(enteredDrug -> Platform.runLater(() -> {
//                    EnterForm.this.fireEvent(new DrugEnteredEvent(enteredDrug, null));
//                    doClearInput();
//                    getSearchInput().clear();
//                    getSearchResult().clear();
//                }))
//                .exceptionally(HandlerFX::exceptionally);
//    }

}
