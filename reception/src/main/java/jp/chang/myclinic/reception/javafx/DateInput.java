package jp.chang.myclinic.reception.javafx;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import jp.chang.myclinic.consts.Gengou;
import jp.chang.myclinic.reception.lib.DateUtil;
import jp.chang.myclinic.reception.lib.Result;

import java.io.IOException;
import java.time.LocalDate;

public class DateInput extends HBox {

    @FXML
    private ChoiceBox<Gengou> gengouChoice;
    @FXML
    private TextField nenInput;
    @FXML
    private TextField monthInput;
    @FXML
    private TextField dayInput;

    public DateInput() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/DateInput.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        loader.load();
    }

    @FXML
    private void initialize(){
        gengouChoice.setConverter(new StringConverter<Gengou>(){

            @Override
            public String toString(Gengou gengou) {
                return gengou.getKanji();
            }

            @Override
            public Gengou fromString(String string) {
                return Gengou.fromKanji(string);
            }
        });
        setGengouItems(Gengou.Heisei, Gengou.Shouwa, Gengou.Taishou, Gengou.Meiji);
        selectGengou(Gengou.Shouwa);
    }

    public void setGengouItems(Gengou... gengouItems){
        gengouChoice.getItems().clear();
        gengouChoice.getItems().addAll(gengouItems);
    }

    public void selectGengou(Gengou gengou){
        gengouChoice.getSelectionModel().select(gengou);
    }

    public Result<LocalDate> getValue(){
        return DateUtil.convertToLocalDate(gengouChoice.getValue(), nenInput.getText(), monthInput.getText(),
                dayInput.getText());
    }

}
