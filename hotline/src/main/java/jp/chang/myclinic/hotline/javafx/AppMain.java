package jp.chang.myclinic.hotline.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.chang.myclinic.hotline.Context;
import jp.chang.myclinic.hotline.Service;
import jp.chang.myclinic.hotline.User;

import java.util.List;

public class AppMain extends Application {

    private User sender;
    private User recipient;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        List<String> args = getParameters().getUnnamed();
        if( args.size() != 3 ){
            System.out.println("Usage: server-url sender recipient");
            System.out.println("ssender/receipient should be one of practice, pharmacy, or reception");
            System.exit(1);
        }
        String serverUrl = args.get(0);
        if (!serverUrl.endsWith("/")) {
            serverUrl = serverUrl + "/";
        }
        Service.setServerUrl(serverUrl);
        Context.INSTANCE = new Context(sender, recipient);
    }

    @Override
    public void start(Stage primaryStage)  {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MainScene.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Hotline");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception ex){
            System.out.println(ex);
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private static User resolveUser(String arg){
        for(User user: User.values()){
            if( user.getName().equalsIgnoreCase(arg) ){
                return user;
            }
        }
        throw new RuntimeException("invalid user name: " + arg);
    }

}
