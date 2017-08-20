package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.practice.MainExecContext;

import javax.swing.*;

public class LeftPaneWrapper extends JPanel {

    private MainExecContext mainExecContext;

    public LeftPaneWrapper(MainExecContext mainExecContext){
        this.mainExecContext = mainExecContext;
    }

    public void reset(){

    }

    public void start(){
        System.out.println("START");
    }
}
