package jp.chang.myclinic.reception.javafx.edit_hoken;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.ShahokokuhoDTO;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.utilfx.GuiUtil;

public class EnterShahokokuhoBinder {

    public interface Callbacks {
        void onEnter(ShahokokuhoDTO shahokokuho);
        void onCancel();
    }

    //private static Logger logger = LoggerFactory.getLogger(ShahokokuhoBinder.class);
    private ShahokokuhoForm form = new ShahokokuhoForm();
    private ShahokokuhoLogic logic = new ShahokokuhoLogic();
    private VBox root = new VBox(4);
    private Callbacks callbacks = new Callbacks(){
        @Override
        public void onEnter(ShahokokuhoDTO shahokokuho) { }

        @Override
        public void onCancel() { }
    };

    public EnterShahokokuhoBinder() {
        form.bindLogic(logic);
        root.getChildren().addAll(
                form,
                createCommands()
        );
    }

    public void setCallbacks(Callbacks callbacks){
        this.callbacks = callbacks;
    }

    public Parent getPane(){
        return root;
    }

    public Node createCommands(){
        HBox row = new HBox(4);
        row.setAlignment(Pos.CENTER_RIGHT);
        Button enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(event -> doEnter());
        cancelButton.setOnAction(event -> callbacks.onCancel());
        row.getChildren().addAll(enterButton, cancelButton);
        return row;
    }

    private void doEnter(){
        ErrorMessages em = new ErrorMessages();
        ShahokokuhoDTO shaho = logic.getValue(em);
        if( em.hasError() ){
            GuiUtil.alertError(em.getMessage());
            return;
        }
        callbacks.onEnter(shaho);
    }
}
