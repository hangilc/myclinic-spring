package jp.chang.myclinic.practice.rightpane.todaysvisits;

import jp.chang.myclinic.dto.VisitPatientDTO;
import jp.chang.myclinic.practice.MainContext;
import jp.chang.myclinic.practice.Service;

import javax.swing.*;
import java.awt.*;

public class TodaysVisits extends JButton {

    private SearchResult searchResult;
    private JScrollPane resultScroll;

    public TodaysVisits(){
        super("本日の診察");
        TodaysVisits self = this;
        searchResult = new SearchResult();
        searchResult.setCallback(new SearchResult.Callback() {
            @Override
            public void onSelect(VisitPatientDTO selected) {
                MainContext.get(self).startBrowse(selected.patient, () -> {});
            }
        });
        resultScroll = new JScrollPane(searchResult);
        resultScroll.setVisible(false);
        addActionListener(evt -> {
            if( resultScroll.isVisible() ){
                resultScroll.setVisible(false);
                revalidate();
                repaint();
            } else {
                doOpen();
            }
        });
    }

    public Component getWorkArea(){
        return resultScroll;
    }

    private void doOpen(){
        Service.api.listTodaysVisits()
                .thenAccept(list -> EventQueue.invokeLater(() ->{
                    searchResult.setListData(list.toArray(new VisitPatientDTO[]{}));
                    resultScroll.setVisible(true);
                    revalidate();
                    repaint();
                }))
                .exceptionally(t -> {
                    t.printStackTrace();
                    EventQueue.invokeLater(() -> {
                        alert(t.toString());
                    });
                    return null;
                });
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
