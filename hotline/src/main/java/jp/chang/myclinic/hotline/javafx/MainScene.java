package jp.chang.myclinic.hotline.javafx;

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
import jp.chang.myclinic.hotline.Context;
import jp.chang.myclinic.hotline.Freqs;
import jp.chang.myclinic.hotline.ResizeRequiredEvent;
import jp.chang.myclinic.hotline.User;
import jp.chang.myclinic.hotline.lib.HotlineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

class MainScene extends VBox {

    private static Logger logger = LoggerFactory.getLogger(MainScene.class);
    private static final String beepMessage = "[BEEP]";
    private VBox messageBox = new VBox(2);
    private ScrollPane messageScroll;
    private TextArea inputText = new TextArea();
    private Label errorMessage;
    private ContextMenu freqContextMenu;

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
        sendButton.setOnAction(evt -> doSend());
        rajaButton.setOnAction(evt -> doRaja());
        beepButton.setOnAction(evt -> doSendBeep());
        freqContextMenu = createFreqContextMenu();
        freqLink.setOnMouseClicked(event -> doFreq(freqLink, event));
        hbox.getChildren().addAll(
                sendButton,
                rajaButton,
                beepButton,
                freqLink
        );
        return hbox;
    }

    private ContextMenu createFreqContextMenu(){
        ContextMenu menu = new ContextMenu();
        String sender = Context.INSTANCE.getSender().getName();
        List<MenuItem> items = Freqs.INSTANCE.listFor(sender).stream().map(text -> {
            MenuItem item = new MenuItem(text);
            item.setOnAction(evt -> {
                insertToInput(text);
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
        posts.forEach(post -> {
            User postSender = User.fromName(post.sender);
            User postRecipient = User.fromName(post.recipient);
            if( postSender == null || postRecipient == null ){
                return;
            }
            if( isMyPost(postSender, postRecipient) ){
                if( isBeepPost(post.message) ){
                    if( !initialSetup && postRecipient == Context.INSTANCE.getSender() ){
                        System.out.println("beep");
                        playBeep();
                    }
                    return;
                }
                String prefix = HotlineUtil.makeHotlinePrefix(postSender.getDispName(), post.hotlineId);
                Node label = createLabel(prefix, post.message);
                messageBox.getChildren().add(label);
            }
        });
        hideErrorMessage();
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

    private void postMessage(String message){
        HotlineUtil.postMessge(Context.INSTANCE.getSender().getName(), Context.INSTANCE.getRecipient().getName(), message)
                .thenAccept(result -> {
                    Context.INSTANCE.getPeriodicFetcher().trigger();
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
