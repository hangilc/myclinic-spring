package jp.chang.myclinic.reception.javafx.edit_koukikourei;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jp.chang.myclinic.dto.KoukikoureiDTO;
import jp.chang.myclinic.util.logic.Converters;
import jp.chang.myclinic.util.logic.ErrorMessages;
import jp.chang.myclinic.util.logic.LogicValue;
import jp.chang.myclinic.util.logic.Validators;
import jp.chang.myclinic.utilfx.GuiUtil;

import java.util.function.Consumer;

public class EnterKoukikoureiStage extends Stage {

    //private static Logger logger = LoggerFactory.getLogger(EnterKoukikoureiStage.class);
    private Consumer<KoukikoureiDTO> enterCallback = dto -> {};
    private KoukikoureiForm form;
    private Button enterButton;

    public EnterKoukikoureiStage(int patientId) {
        setTitle("新規後期高齢保険の入力");
        Parent root = createRoot(patientId);
        root.getStylesheets().add("css/Main.css");
        root.getStyleClass().addAll("dialog-root", "enter-koukikourei-stage");
        setScene(new Scene(root));
    }

    public void setEnterCallback(Consumer<KoukikoureiDTO> callback){
        this.enterCallback = callback;
    }

    public void setInputs(KoukikoureiFormInputs inputs){
        form.setInputs(inputs);
    }

    public void simulateEnterButtonClick(){
        enterButton.fire();
    }

    private Parent createRoot(int patientId){
        VBox root = new VBox(4);
        this.form = new KoukikoureiForm();
        KoukikoureiFormLogic.EnterProc proc = KoukikoureiFormLogic.createEnterProc(patientId, form::setInputs);
        root.getChildren().add(form);
        HBox commands = new HBox(4);
        this.enterButton = new Button("入力");
        Button cancelButton = new Button("キャンセル");
        enterButton.setOnAction(evt -> {
            ErrorMessages em = new ErrorMessages();
            KoukikoureiDTO dto = proc.enter(form.getInputs(), em);
            if( em.hasError() ){
                GuiUtil.alertError(em.getMessage());
                return;
            }
            validateCheckingDigit(dto.hihokenshaBangou, em);
            if( em.hasError() ){
                if( !GuiUtil.confirm("被保険者番号の検証番号が正しくありませんが、このまま入力しますか？") ){
                    return;
                }
            }
            enterCallback.accept(dto);
        });
        cancelButton.setOnAction(evt -> close());
        commands.getChildren().addAll(enterButton, cancelButton);
        commands.setAlignment(Pos.CENTER_RIGHT);
        root.getChildren().add(commands);
        return root;
    }

    private void validateCheckingDigit(String hihokenshaBangou, ErrorMessages em){
        new LogicValue<>(hihokenshaBangou)
                .convert(Converters::stringToInteger)
                .validate(Validators::hasValidCheckingDigit)
                .verify(null, em);
    }

}
