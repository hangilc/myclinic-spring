package jp.chang.myclinic.hotline.javafx;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.dto.PatientDTO;
import jp.chang.myclinic.hotline.*;
import jp.chang.myclinic.hotline.lib.HotlineUtil;
import jp.chang.myclinic.hotline.lib.PeriodicFetcher;
import jp.chang.myclinic.hotline.tracker.DispatchAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class MainScene extends VBox implements DispatchAction {

    private static Logger logger = LoggerFactory.getLogger(MainScene.class);
    private static final String beepMessage = "[BEEP]";
    private VBox messageBox = new VBox(2);
    private ScrollPane messageScroll;
    private TextArea inputText = new TextArea();
    private Label errorMessage;
    private ContextMenu freqContextMenu;
    private static final String templateMarker = "{}";

    MainScene() {
        super(4);
        getStyleClass().add("main-scene");
        getChildren().addAll(
                createDisp(),
                createTextArea(),
                createCommands(),
                createErrorMessage()
        );
        messageBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                messageScroll.setVvalue(1.0);
            }
        });
    }

    private Node createDisp(){
        messageScroll = new ScrollPane(messageBox);
        messageScroll.getStyleClass().add("disp");
        messageScroll.setFitToWidth(true);
        return messageScroll;
    }

    private Node createTextArea(){
        inputText.setWrapText(true);
        inputText.getStyleClass().add("input-text");
        return inputText;
    }

    private Node createCommands(){
        HBox hbox = new HBox(4);
        messageBox.setAlignment(Pos.CENTER_LEFT);
        Button sendButton = new Button("送信");
        Button rajaButton = new Button("了解");
        Button beepButton = new Button("ビープ");
        Hyperlink freqLink = new Hyperlink("常用");
        Hyperlink patientLink = new Hyperlink("患者");
        sendButton.setOnAction(evt -> doSend());
        rajaButton.setOnAction(evt -> doRaja());
        beepButton.setOnAction(evt -> doSendBeep());
        freqContextMenu = createFreqContextMenu();
        freqLink.setOnMouseClicked(event -> doFreq(freqLink, event));
        patientLink.setOnMouseClicked(event -> doPatient(patientLink, event));
        hbox.getChildren().addAll(
                sendButton,
                rajaButton,
                beepButton,
                freqLink,
                patientLink
        );
        return hbox;
    }

    private void doPatient(Node anchor, MouseEvent event){
        Service.api.listWqueue()
                .thenAccept(qlist -> Platform.runLater(() -> {
                    Map<Integer, PatientDTO> patientMap = new LinkedHashMap<>();
                    qlist.forEach(q -> patientMap.put(q.patient.patientId, q.patient));
                    ContextMenu menu = new ContextMenu();
                    List<MenuItem> items = patientMap.values().stream().map(p -> {
                        String text = String.format("%s%s (%d) 様、", p.lastName, p.firstName, p.patientId);
                        MenuItem item = new MenuItem(text);
                        item.setOnAction(evt -> insertToInput(text));
                        return item;
                    }).collect(Collectors.toList());
                    menu.getItems().addAll(items);
                    menu.show(anchor, event.getScreenX(), event.getScreenY());
                }))
                .exceptionally(HandlerFX::exceptionally);
    }

    private ContextMenu createFreqContextMenu(){
        ContextMenu menu = new ContextMenu();
        String sender = Context.INSTANCE.getSender().getName();
        List<MenuItem> items = Freqs.INSTANCE.listFor(sender).stream().map(text -> {
            MenuItem item = new MenuItem(text);
            item.setOnAction(evt -> {
                insertFreqToInput(text);
            });
            return item;
        }).collect(Collectors.toList());
        menu.getItems().addAll(items);
        return menu;
    }

    private Node createErrorMessage(){
        Label label = new Label();
        label.setVisible(false);
        label.setManaged(false);
        label.getStyleClass().add("error");
        label.setWrapText(true);
        errorMessage = label;
        return label;
    }

    public void addHotlinePosts(List<HotlineDTO> posts, boolean initialSetup) {
        int count = 0;
        User myself = Context.INSTANCE.getSender();
        if( initialSetup ){
            messageBox.getChildren().clear();
        }
        for(HotlineDTO post: posts){
            User postSender = User.fromName(post.sender);
            User postRecipient = User.fromName(post.recipient);
            if( postSender != null && postRecipient != null ){
                if( isMyPost(postSender, postRecipient) ){
                    if( isBeepPost(post.message) ){
                        if( !initialSetup && postRecipient == Context.INSTANCE.getSender() ){
                            playBeep();
                        }
                    } else {
                        String prefix = HotlineUtil.makeHotlinePrefix(postSender.getDispName(), post.hotlineId);
                        Node label = createLabel(prefix, post.message);
                        messageBox.getChildren().add(label);
                        if( postRecipient == myself ) {
                            count += 1;
                        }
                    }
                }
           }
        }
        hideErrorMessage();
        if( !initialSetup && count > 0 ){
            playBeep();
        }
    }

    public void showErrorMessage(String message){
        errorMessage.setText(message);
        if( !errorMessage.isVisible() ){
            errorMessage.setManaged(true);
            errorMessage.setVisible(true);
        }
        fireEvent(new ResizeRequiredEvent());
    }

    private void hideErrorMessage(){
        if( errorMessage.isVisible() ){
            errorMessage.setManaged(false);
            errorMessage.setVisible(false);
            fireEvent(new ResizeRequiredEvent());
        }
    }

    private void doSend(){
        String message = inputText.getText();
        if( message.isEmpty() ){
            return;
        }
        postMessage(message);
        inputText.setText("");
    }

    private void doRaja(){
        postMessage("了解");
    }

    private void doSendBeep(){
        postMessage(beepMessage);
    }

    private boolean isMyPost(User postSender, User postRecipient){
        User sender = Context.INSTANCE.getSender();
        User recipient = Context.INSTANCE.getRecipient();
        return (postSender == sender && postRecipient == recipient) ||
                (postSender == recipient && postRecipient == sender);
    }

    private boolean isBeepPost(String message){
        return beepMessage.equals(message);
    }

    private void playBeep() {
        java.awt.Toolkit.getDefaultToolkit().beep();
    }

    private Node createLabel(String prefix, String message){
        TextFlow label = new TextFlow(new Text(prefix), new Text(message));
        label.setOnMouseClicked(event -> {
            if( event.getClickCount() == 2 ){
                EditDialog editDialog = new EditDialog(message){
                    @Override
                    protected void onEnter(EditDialog self, String text) {
                        insertToInput(text);
                        self.close();
                    }
                };
                editDialog.showAndWait();
            }
        });
        return label;
    }

    private void insertToInput(String text){
        int caretPos = inputText.getCaretPosition();
        inputText.insertText(caretPos, text);
    }

    private void insertFreqToInput(String text){
        if( text.contains(templateMarker) ){
            int caretPos = inputText.getCaretPosition();
            int offset = text.indexOf(templateMarker);
            text = text.replace(templateMarker, "");
            inputText.requestFocus();
            inputText.insertText(caretPos, text);
            inputText.positionCaret(caretPos + offset);
        } else {
            insertToInput(text);
        }
    }

    private void postMessage(String message){
        HotlineUtil.postMessge(Context.INSTANCE.getSender().getName(), Context.INSTANCE.getRecipient().getName(), message)
                .thenAccept(result -> {
                    Context.INSTANCE.getPeriodicFetcher().trigger(PeriodicFetcher.CMD_FETCH);
                })
                .exceptionally(t -> {
                    logger.error("failed to post message", t);
                    return null;
                });
    }

    private void doFreq(Node link, MouseEvent event){
        freqContextMenu.show(link, event.getScreenX(), event.getScreenY());
    }

}
