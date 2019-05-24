package jp.chang.myclinic.practice.javafx;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.HBox;

public class PatientManip extends HBox {

    private Button cashierButton;
    private Button endPatientButton;

    public PatientManip(){
        super(4);
        setAlignment(Pos.CENTER_LEFT);
        this.cashierButton = new Button("会計");
        this.endPatientButton = new Button("患者終了");
        Hyperlink searchTextLink = new Hyperlink("文章検索");
        Hyperlink referLink = new Hyperlink("紹介状作成");
        cashierButton.setOnAction(evt -> onCashier());
        endPatientButton.setOnAction(evt -> onEndPatient());
        searchTextLink.setOnAction(evt -> onSearchText());
        referLink.setOnAction(evt -> onRefer());
        getChildren().addAll(cashierButton, endPatientButton, searchTextLink, referLink);
    }

    public void simulateClickCashierButton(){
        cashierButton.fire();
    }

    public void simulateEndPatientClick(){
        endPatientButton.fire();
    }

    protected void onCashier(){

    }

    protected void onEndPatient(){

    }

    protected void onSearchText(){

    }

    protected void onRefer(){

    }

}
