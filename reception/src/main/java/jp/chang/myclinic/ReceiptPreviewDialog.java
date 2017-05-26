package jp.chang.myclinic;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.drawer.printer.PrinterManageDialog;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class ReceiptPreviewDialog extends JDialog {

    private JButton printButton;
    private JButton cancelButton;
    private JMenuItem itemSelectPrinter = new JMenuItem("プリンター選択");
    private JMenuItem itemManagePrinter = new JMenuItem("プリンター管理");
    private java.util.List<Op> ops;

    public ReceiptPreviewDialog(Window owner, List<Op> ops){
        super(owner, "領収書プレビュー", Dialog.ModalityType.DOCUMENT_MODAL);
        this.ops = ops;
        setLayout(new MigLayout("fill", "[grow]", "[grow] []"));
        setupMenu();
        add(makeCenter(), "grow, wrap");
        add(makeSouth(), "right");
        bind();
        pack();
    }

    private void setupMenu(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menuPringSetting = new JMenu("印刷設定");
        menuBar.add(menuPringSetting);
        menuPringSetting.add(itemSelectPrinter);
        menuPringSetting.add(itemManagePrinter);
        setJMenuBar(menuBar);
    }

    private JComponent makeCenter(){
        JComponent pane = new DrawerPreviewPane(ops, 148, 105);
        pane.setPreferredSize(new Dimension(300, 200));
        pane.setOpaque((true));
        pane.setBackground(Color.WHITE);
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
        printButton.addActionListener(event -> {
            DrawerPrinter printer = new DrawerPrinter();
            printer.print(ops);
        });
        cancelButton.addActionListener(event -> {
            dispose();
        });
        itemManagePrinter.addActionListener(event -> {
            PrinterManageDialog dialog = new PrinterManageDialog(this, ReceptionConfig.INSTANCE.getSettingDir());
            dialog.setLocationByPlatform(true);
            dialog.setVisible(true);
        });
    }
}
