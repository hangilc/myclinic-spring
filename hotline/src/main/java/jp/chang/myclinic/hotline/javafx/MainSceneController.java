package jp.chang.myclinic.hotline.javafx;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.hotline.Context;
import jp.chang.myclinic.hotline.User;
import jp.chang.myclinic.hotline.lib.HotlineUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MainSceneController {
    private static Logger logger = LoggerFactory.getLogger(MainSceneController.class);
    public VBox messageBox;
    public TextArea inputText;

    public void onSubmit(Event event){
        String message = inputText.getText();
        if( message.isEmpty() ){
            return;
        }
        postMessage(message);
    }

    public void onRaja(ActionEvent actionEvent) {
        postMessage("了解");
    }

    public void onBeep(ActionEvent actionEvent) {
        System.out.println("on beep");
    }

    public void addHotlinePosts(List<HotlineDTO> posts){
        posts.forEach(post -> {
            User postSender = User.fromName(post.sender);
            if( postSender == null ){
                return;
            }
            if( postSender == Context.INSTANCE.getSender() || postSender == Context.INSTANCE.getRecipient() ){
                String text = HotlineUtil.makeHotlineText(postSender.getDispName(), post.hotlineId, post.message);
                Label label = new Label(text);
                label.setAlignment(Pos.TOP_LEFT);
                label.setWrapText(true);
                label.setMinHeight(Control.USE_PREF_SIZE);
                messageBox.getChildren().add(label);
            }
        });
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
