package jp.chang.myclinic.practice.leftpane;

import jp.chang.myclinic.dto.VisitFull2PageDTO;
import jp.chang.myclinic.practice.Service;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

class RecordsNav extends JPanel {

    public interface Callback {
        void onTrigger(int page);
    }

    private int patientId;
    private int page;
    private int totalPages;
    private Callback callback;
    private JLabel label;

    RecordsNav(){
        setLayout(new MigLayout("insets 0", "", ""));
        JButton toFirstButton = new JButton("最初へ");
        toFirstButton.addActionListener(event -> doClick(0));
        JButton toPrevButton = new JButton("前へ");
        toPrevButton.addActionListener(event -> doClick(this.page - 1));
        JButton toNextButton = new JButton("次へ");
        toNextButton.addActionListener(event -> doClick(this.page + 1));
        JButton toLastButton = new JButton("最後へ");
        toLastButton.addActionListener(event -> doClick(this.totalPages - 1));
        label = new JLabel(makeLabelText());
        add(toFirstButton);
        add(toPrevButton);
        add(toNextButton);
        add(toLastButton);
        add(label);
    }

    void start(int patientId, int totalPages){
        this.patientId = patientId;
        this.page = 0;
        this.totalPages = totalPages;
        label.setText(makeLabelText());
    }

    void clear(){
        this.patientId = 0;
    }

    void set(int page, int totalPages){
        this.page = page;
        this.totalPages = totalPages;
        label.setText(makeLabelText());
    }

    void changePage(VisitFull2PageDTO visitFullPage, int page){
        // to be overridden
    }

    private String makeLabelText(){
        return String.format("[%d/%d]", page+1, totalPages);
    }

    private void doClick(int newPage){
        if( patientId > 0 && newPage != page && newPage >= 0 && newPage < totalPages ){
            Service.api.listVisitFull2(patientId, newPage)
                    .thenAccept(visits -> changePage(visits, newPage))
                    .exceptionally(t -> {
                        t.printStackTrace();
                        EventQueue.invokeLater(() -> {
                            alert(t.toString());
                        });
                        return null;
                    });
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
