package jp.chang.myclinic.hotline.javafx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.hotline.Context;
import jp.chang.myclinic.hotline.User;
import jp.chang.myclinic.hotline.lib.HotlineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

public class MainSceneController {
    private static Logger logger = LoggerFactory.getLogger(MainSceneController.class);
    public VBox messageBox;
    public ScrollPane messageScroll;
    public TextArea inputText;
    private static String beepMessage = "[BEEP]";

    public void initialize(){
        messageBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                messageScroll.setVvalue(1.0);
            }
        });
    }

    public void onSubmit(Event event){
        String message = inputText.getText();
        if( message.isEmpty() ){
            return;
        }
        postMessage(message);
        inputText.setText("");
    }

    public void onRaja(ActionEvent actionEvent) {
        postMessage("了解");
    }

    public void onBeep(ActionEvent actionEvent) {
        postMessage(beepMessage);
    }

    private void playBeep(){
        java.awt.Toolkit.getDefaultToolkit().beep();
//        File wavFile = new File("C:\\Windows\\Media\\notify.wav");
//        try {
//            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);
//            AudioFormat audioFormat = audioInputStream.getFormat();
//            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
//            Clip clip = (Clip)AudioSystem.getLine(info);
//            clip.open(audioInputStream);
//            clip.setFramePosition(0);
//            clip.start();
//            //clip.stop();
//        } catch(Exception ex){
//            logger.error("cannot play sound", ex);
//        }
    }

    public void addHotlinePosts(List<HotlineDTO> posts, boolean initialSetup){
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

    private boolean isMyPost(User postSender, User postRecipient){
        User sender = Context.INSTANCE.getSender();
        User recipient = Context.INSTANCE.getRecipient();
        return (postSender == sender && postRecipient == recipient) ||
                (postSender == recipient && postRecipient == sender);
    }

    private boolean isBeepPost(String message){
        return beepMessage.equals(message);
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
