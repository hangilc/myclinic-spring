package jp.chang.myclinic.hotline.javafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.hotline.Context;
import jp.chang.myclinic.hotline.User;
import jp.chang.myclinic.hotline.lib.HotlineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

class MainScene extends VBox {

    private static Logger logger = LoggerFactory.getLogger(MainScene.class);
    private static final String beepMessage = "[BEEP]";
    private VBox messageBox = new VBox(2);
    private ScrollPane messageScroll;
    private TextArea inputText = new TextArea();

    MainScene() {
        super(4);
        getStyleClass().add("main-scene");
        getChildren().addAll(
                createDisp(),
                createTextArea(),
                createCommands()
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
        Button sendButton = new Button("送信");
        Button rajaButton = new Button("了解");
        Button beepButton = new Button("ビープ");
        sendButton.setOnAction(evt -> doSend());
        rajaButton.setOnAction(evt -> doRaja());
        beepButton.setOnAction(evt -> doSendBeep());
        hbox.getChildren().addAll(
                sendButton,
                rajaButton,
                beepButton
        );
        return hbox;
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
                String text = HotlineUtil.makeHotlineText(postSender.getDispName(), post.hotlineId, post.message);
                Node label = createLabel(text);
                messageBox.getChildren().add(label);
            }
        });
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

    private Node createLabel(String text){
        Label label = new Label(text);
        label.setAlignment(Pos.TOP_LEFT);
        label.setWrapText(true);
        label.setMinHeight(USE_PREF_SIZE);
        label.setOnMouseClicked(event -> {
            if( event.getClickCount() == 2 ){
                EditDialog editDialog = new EditDialog(text);
                editDialog.showAndWait();
            }
        });
        return label;
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

}
