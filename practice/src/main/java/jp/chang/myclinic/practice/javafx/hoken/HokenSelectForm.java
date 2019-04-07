package jp.chang.myclinic.practice.javafx.hoken;

import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.HokenDTO;
import jp.chang.myclinic.dto.VisitDTO;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.utilfx.HandlerFX;

import java.util.function.Consumer;

public class HokenSelectForm extends VBox {

    private final int visitId;
    private HokenSelectPane selectPane;
    private Consumer<HokenDTO> onEnteredHandler = v -> {};
    private Runnable onCancelHandler = () -> {};
    private Hyperlink enterlink = new Hyperlink("入力");
    private Hyperlink cancellink = new Hyperlink("キャンセル");

    public HokenSelectForm(int visitId, HokenDTO available, HokenDTO current){
        super(4);
        this.visitId = visitId;
        getStyleClass().add("form");
        Label title = new Label("保険選択");
        title.getStyleClass().add("title");
        title.setMaxWidth(Double.MAX_VALUE);
        selectPane = new HokenSelectPane(available, current);
        getChildren().addAll(
                title,
                selectPane,
                createButtons()
        );
    }

    public void setOnEnteredHandler(Consumer<HokenDTO> handler){
        this.onEnteredHandler = handler;
    }

    public void setOnCancelHandler(Runnable handler){
        this.onCancelHandler = handler;
    }

    public void simulateEnterButtonClick(){
        enterlink.fire();
    }

    public void simulateShahokokuhoSelect(boolean select){
        selectPane.simulateShahokokuhoSelect(select);
    }

    public void simulateKoukikoureiSelect(boolean select){
        selectPane.simulateKoukikoureiSelect(select);
    }

    public void simulateKouhiSelect(int kouhiId, boolean select){
        selectPane.simulateKouhiSelect(kouhiId, select);
    }

    public void simulateCancelButtonClick(){
        cancellink.fire();
    }

    private Node createButtons(){
        HBox hbox = new HBox(4);
        enterlink.setOnAction(event -> {
                VisitDTO visit = new VisitDTO();
                visit.visitId = visitId;
                selectPane.storeTo(visit);
                Frontend frontend = Context.frontend;
                Context.frontend.updateHoken(visit)
                        .thenCompose(v -> frontend.getHoken(visitId))
                        .thenAccept(newHoken -> onEnteredHandler.accept(newHoken))
                        .exceptionally(HandlerFX::exceptionally);
        });
        cancellink.setOnAction(event -> onCancelHandler.run());
        hbox.getChildren().addAll(enterlink, cancellink);
        return hbox;
    }

}
