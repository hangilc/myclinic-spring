package jp.chang.myclinic;

import jp.chang.myclinic.drawer.Op;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ReceiptPreviewDialog extends JDialog {

    private JButton printButton;
    private JButton cancelButton;
    private java.util.List<Op> ops;

    public ReceiptPreviewDialog(Window owner, List<Op> ops){
        super(owner, "領収書プレビュー", Dialog.ModalityType.DOCUMENT_MODAL);
        this.ops = ops;
        setLayout(new MigLayout("fill", "[grow]", "[grow] []"));
        add(makeCenter(), "wrap");
        add(makeSouth(), "right");
        bind();
        pack();
    }

    private JComponent makeCenter(){
        JComponent pane = new DrawerPreviewPane(ops);
        pane.setPreferredSize(new Dimension(300, 200));
        return pane;
    }

    private JComponent makeSouth(){
        JPanel panel = new JPanel(new MigLayout("insets 0", "", ""));
        printButton = new JButton("印刷");
        panel.add(printButton, "sizegroup btn");
        cancelButton = new JButton("キャンセル");
        panel.add(cancelButton, "sizegroup btn");
        return panel;
    }

    private void bind(){
        cancelButton.addActionListener(event -> {
            dispose();
        });
    }
}
