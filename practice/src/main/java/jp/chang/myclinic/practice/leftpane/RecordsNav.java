package jp.chang.myclinic.practice.leftpane;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class RecordsNav extends JPanel {

    public interface Callback {
        void onTrigger(int page);
    }

    private int page;
    private int totalPages;
    private Callback callback;
    JLabel label;

    public RecordsNav(){
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

    public void set(int page, int totalPages){
        this.page = page;
        this.totalPages = totalPages;
        label.setText(makeLabelText());
    }

    private String makeLabelText(){
        return String.format("[%d/%d]", page+1, totalPages);
    }

    private void doClick(int newPage){
        if( newPage != page && newPage >= 0 && newPage < totalPages ){
            //callback.onTrigger(newPage);
        }
    }
}
