package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.dto.ShinryouFullDTO;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class DeleteSomePane extends JPanel {

    interface Callback {
        default void onDelete(List<ShinryouFullDTO> shinryouList){}
        default void onCancel(){}
    }

    private ShinryouListPanel listPanel;
    private Callback callback = new Callback(){};

    DeleteSomePane(int width, List<ShinryouFullDTO> shinryouList){
        setLayout(new MigLayout("insets 0", String.format("[%dpx!]", width), ""));
        listPanel = new ShinryouListPanel(width, shinryouList);
        add(listPanel, "wrap");
        add(makeCommandBox());
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private Component makeCommandBox(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        JButton execButton = new JButton("削除実行");
        execButton.addActionListener(event -> callback.onDelete(listPanel.getChecked()));
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(event -> callback.onCancel());
        panel.add(execButton);
        panel.add(cancelButton);
        return panel;
   }
}
