package jp.chang.myclinic.practice.javafx.conduct;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.consts.ConductKind;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.frontend.Frontend;
import jp.chang.myclinic.practice.Context;
import jp.chang.myclinic.utilfx.HandlerFX;
import jp.chang.myclinic.practice.javafx.events.ConductEnteredEvent;
import jp.chang.myclinic.practice.lib.PracticeAPI;
import jp.chang.myclinic.practice.lib.PracticeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class ConductMenu extends VBox {

    private static Logger logger = LoggerFactory.getLogger(ConductMenu.class);

    private int visitId;
    private String at;
    private StackPane workarea = new StackPane();

    public ConductMenu(int visitId, String at) {
        super(4);
        this.visitId = visitId;
        this.at = at;
        getChildren().addAll(
            createLink()
        );
    }

    private Node createLink(){
        Hyperlink link = new Hyperlink("[処置]");
        link.setOnMouseClicked(this::onClick);
        return link;
    }

    private void onClick(MouseEvent event){
        if( !isWorkareaShown() ) {
            ContextMenu contextMenu = new ContextMenu();
            {
                MenuItem item = new MenuItem("Ｘ線検査追加");
                item.setOnAction(evt -> doEnterXp());
                contextMenu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("注射追加");
                item.setOnAction(evt -> doEnterInjection());
                contextMenu.getItems().add(item);
            }
            {
                MenuItem item = new MenuItem("全部コピー");
                item.setOnAction(evt -> doCopyAll());
                contextMenu.getItems().add(item);
            }
            contextMenu.show(this, event.getScreenX(), event.getScreenY());
        }
    }

    private void fireConductEntered(ConductFullDTO entered){
        fireEvent(new ConductEnteredEvent(entered));
    }

    private CompletableFuture<ConductFullDTO> enterXp(int visitId, String label, String film){ // returns conductId
        class Store {
            private LocalDate at;
            private int shinryoucode1;
            private int shinryoucode2;
            private int kizaicode;
        }
        Store store = new Store();
        Frontend frontend = Context.getInstance().getFrontend();
        return frontend.getVisit(visitId)
                .thenCompose(visit -> {
                    store.at = LocalDateTime.parse(visit.visitedAt).toLocalDate();
                    return frontend.resolveShinryouMasterByKey("単純撮影", store.at);
                })
                .thenCompose(m -> {
                    store.shinryoucode1 = m.shinryoucode;
                    return frontend.resolveShinryouMasterByKey("単純撮影診断", store.at);
                })
                .thenCompose(m -> {
                    store.shinryoucode2 = m.shinryoucode;
                    return frontend.resolveKizaiMasterByKey(film, store.at);
                })
                .thenCompose(m -> {
                    store.kizaicode = m.kizaicode;
                    ConductEnterRequestDTO req = new ConductEnterRequestDTO();
                    req.visitId = visitId;
                    req.kind = ConductKind.Gazou.getCode();
                    req.gazouLabel = label;
                    req.shinryouList = Stream.of(store.shinryoucode1, store.shinryoucode2)
                            .map(shinryoucode -> {
                                ConductShinryouDTO shinryou = new ConductShinryouDTO();
                                shinryou.shinryoucode = shinryoucode;
                                return shinryou;
                            })
                            .collect(toList());
                    req.kizaiList = Stream.of(store.kizaicode)
                            .map(kizaicode -> {
                                ConductKizaiDTO kizai = new ConductKizaiDTO();
                                kizai.kizaicode = kizaicode;
                                kizai.amount = 1.0;
                                return kizai;
                            })
                            .collect(toList());
                    return frontend.enterConductFull(req);
                });
    }

    private void doEnterXp(){
        if( PracticeUtil.confirmCurrentVisitAction(visitId, "Ｘ線検査を入力しますか？") ) {
            EnterXpForm form = new EnterXpForm() {
                @Override
                protected void onEnter(EnterXpForm form, String label, String film) {
                    enterXp(visitId, label, film)
                            .thenAccept(entered -> Platform.runLater(() -> {
                                fireConductEntered(entered);
                                hideWorkarea();
                            }))
                            .exceptionally(HandlerFX::exceptionally);
                }

                @Override
                protected void onCancel(EnterXpForm form) {
                    hideWorkarea();
                }
            };
            showWorkarea(form);
        }
    }

    private void doEnterInjection(){
        if( PracticeUtil.confirmCurrentVisitAction(visitId, "処置注射を入力しますか？") ){
            EnterInjectionForm form = new EnterInjectionForm(at){
                @Override
                protected void onEnter(EnterInjectionForm form, ConductKind kind, ConductDrugDTO drug) {
                    PracticeAPI.enterInjection(visitId, kind, drug)
                            .thenAccept(entered -> Platform.runLater(() -> {
                                fireConductEntered(entered);
                                hideWorkarea();
                            }))
                            .exceptionally(HandlerFX::exceptionally);
                }

                @Override
                protected void onCancel(EnterInjectionForm form) {
                    hideWorkarea();
                }
            };
            showWorkarea(form);
        }
    }

    private void doCopyAll(){
        int targetVisitId = PracticeUtil.findCopyTarget(visitId);
        if( targetVisitId > 0 ){
            Context.getInstance().getFrontend().copyAllConducts(targetVisitId, visitId)
                    .thenCompose(Context.getInstance().getFrontend()::listConductFullByIds)
                    .thenAccept(entered -> Platform.runLater(() -> {
                        entered.forEach(this::fireConductEntered);
                    }))
                    .exceptionally(HandlerFX::exceptionally);
        }
    }

    private void showWorkarea(Node content){
        workarea.getChildren().clear();
        workarea.getChildren().add(content);
        getChildren().add(workarea);
    }

    private void hideWorkarea(){
        getChildren().remove(workarea);
    }

    private boolean isWorkareaShown(){
        return getChildren().contains(workarea);
    }

}
