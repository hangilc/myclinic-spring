package jp.chang.myclinic.rcptdrawer;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.rcptdrawer.drawerpreview.DrawerCanvas;
import jp.chang.myclinic.utilfx.GuiUtil;

import java.util.*;
import java.util.stream.Collectors;

class MainRoot extends HBox {

    //private static Logger logger = LoggerFactory.getLogger(MainRoot.class);
    private DrawerCanvas drawerCanvas = new DrawerCanvas(PaperSize.A4, 1.3);
    private List<PageTag> pageTags;
    private int currentPageIndex;
    private Label totalLabel = new Label("");
    private Label patientsLabel = new Label("");
    private Label pageLabel = new Label("");

    MainRoot() {
        super(4);
        getStylesheets().add("Main.css");
        getStyleClass().add("app-root");
        Node previewNode = createPreview();
        getChildren().addAll(
                previewNode,
                createRightColumn()
        );
        HBox.setHgrow(previewNode, Priority.ALWAYS);
    }

    public void setRcptPages(List<PageTag> pageTags){
        this.pageTags = pageTags;
        this.currentPageIndex = 0;
        setTotalLabel(pageTags.size());
        setPatientsLabel(countPatients(pageTags));
        updatePreview();
    }

    private int countPatients(List<PageTag> pageTags){
        Set<Integer> patientIds = new HashSet<Integer>();
        pageTags.forEach(tag -> patientIds.add(tag.patientId));
        return patientIds.size();
    }

    private void setPatientsLabel(int totalPatients){
        String text = String.format("患者 %d名", totalPatients);
        patientsLabel.setText(text);
    }

    private void setTotalLabel(int totalPages){
        String text = String.format("合計 %d枚", totalPages);
        totalLabel.setText(text);
    }

    private void updatePreview(){
        PageTag current = pageTags.get(currentPageIndex);
        if( current != null ){
            drawerCanvas.setOps(current.ops);
            pageLabel.setText(String.format("頁: %d", currentPageIndex + 1));
        } else {
            drawerCanvas.setOps(Collections.emptyList());
            pageLabel.setText("");
        }
    }

    private Node createPreview(){
        ScrollPane scrollPane = new ScrollPane(drawerCanvas);
        scrollPane.setPrefWidth(DrawerCanvas.mmToPixel(PaperSize.A4.getWidth() * 0.7));
        scrollPane.setPrefHeight(DrawerCanvas.mmToPixel(PaperSize.A4.getHeight() * 0.7));
        return scrollPane;
    }

    private Node createRightColumn(){
        VBox vbox = new VBox(4);
        Button nextButton = new Button("次へ");
        Button prevButton = new Button("前へ");
        Button gotoPatientButton = new Button("患者へ");
        Button printOneButton = new Button("１枚印刷");
        Button printRangeButton = new Button("範囲印刷");
        Button printAllButton = new Button("全部印刷");
        nextButton.setOnAction(evt -> gotoNextPage());
        prevButton.setOnAction(evt -> gotoPrevPage());
        gotoPatientButton.setOnAction(evt -> doGotoPatient());
        printOneButton.setOnAction(evt -> printOnePage());
        printRangeButton.setOnAction(evt -> doPrintRange());
        printAllButton.setOnAction(evt -> printAll());
        vbox.setFillWidth(true);
        nextButton.setMaxWidth(Double.MAX_VALUE);
        prevButton.setMaxWidth(Double.MAX_VALUE);
        gotoPatientButton.setMaxWidth(Double.MAX_VALUE);
        vbox.getChildren().addAll(
                totalLabel,
                patientsLabel,
                pageLabel,
                nextButton,
                prevButton,
                gotoPatientButton,
                printOneButton,
                printRangeButton,
                printAllButton
        );
        return vbox;
    }

    private void gotoNextPage(){
        gotoPage(currentPageIndex + 1);
    }

    private void gotoPrevPage(){
        gotoPage(currentPageIndex - 1);
    }

    private void gotoPage(int rcptPage){
        if( rcptPage >= 0 && rcptPage < pageTags.size() ){
            currentPageIndex = rcptPage;
            updatePreview();
        }
    }

    private void printOnePage(){
        int index = currentPageIndex;
        if( index >= 0 && index < pageTags.size() ){
            PageTag pageTag = pageTags.get(index);
            DrawerPrinter drawerPrinter = new DrawerPrinter();
            drawerPrinter.print(pageTag.ops);
        }
    }

    private void printAll(){
        List<List<Op>> pageOps = pageTags.stream().map(pageTag -> pageTag.ops)
                .collect(Collectors.toList());
        Collections.reverse(pageOps);
        DrawerPrinter drawerPrinter = new DrawerPrinter();
        drawerPrinter.printPages(pageOps);
    }

    private void gotoPatient(int patientId){
        for(int i=0;i<pageTags.size();i++){
            PageTag pageTag = pageTags.get(i);
            if( pageTag.patientId == patientId ){
                currentPageIndex = i;
                updatePreview();
                return;
            }
        }
        GuiUtil.alertError(String.format("患者番号 %d がみつかりませんでした。", patientId));
    }

    private void doGotoPatient(){
        Optional<String> input = GuiUtil.askForString("患者番号", "");
        input.ifPresent(s -> {
            try {
                int patientId = Integer.parseInt(s);
                gotoPatient(patientId);
            } catch(NumberFormatException ex){
                GuiUtil.alertError("患者番号の入力が不適切です。");
            }
        });
    }

    private void printRange(int fromPage, int uptoPage){
        fromPage -= 1;
        uptoPage -= 1;
        if( fromPage < 0 ){
            fromPage = 0;
        }
        if( uptoPage >= pageTags.size() ){
            uptoPage = pageTags.size() - 1;
        }
        List<List<Op>> pageOps = pageTags.subList(fromPage, uptoPage+1).stream()
                .map(pageTag -> pageTag.ops).collect(Collectors.toList());
        Collections.reverse(pageOps);
        DrawerPrinter drawerPrinter = new DrawerPrinter();
        drawerPrinter.printPages(pageOps);
    }

    private void doPrintRange(){
        String example = String.format("%d - %d", 1, pageTags.size());
        Optional<String> input = GuiUtil.askForString("範囲を入力してください（例：1-120）", example);
        input.ifPresent(s -> {
            String[] parts = s.split("-");
            if( parts.length != 2 ){
                GuiUtil.alertError("入力が不適切です。");
                return;
            }
            String fromPage = parts[0].trim();
            String uptoPage = parts[1].trim();
            try {
                printRange(Integer.parseInt(fromPage), Integer.parseInt(uptoPage));
            } catch(NumberFormatException ex){
                GuiUtil.alertError("数字の入力が不適切です。");
            }
        });
    }

}
