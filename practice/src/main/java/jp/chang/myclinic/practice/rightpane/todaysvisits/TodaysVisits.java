package jp.chang.myclinic.practice.rightpane.todaysvisits;

import javax.swing.*;
import java.awt.*;

public class TodaysVisits extends JButton {

    private SearchResult searchResult;

    public TodaysVisits(){
        super("本日の診察");
        searchResult = new SearchResult();
        searchResult.setVisible(false);
    }

    public Component getWorkArea(){
        return searchResult;
    }
}
