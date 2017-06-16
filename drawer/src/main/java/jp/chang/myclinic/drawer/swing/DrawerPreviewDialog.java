package jp.chang.myclinic.drawer.swing;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.AuxSetting;
import jp.chang.myclinic.drawer.printer.DrawerPrinter;
import jp.chang.myclinic.drawer.printer.manage.PrinterSetting;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class DrawerPreviewDialog extends JDialog {

    private DrawerPreviewPane previewPane = new DrawerPreviewPane();
    private JPanel northPanel;
    private JButton printButton = new JButton("印刷");
    private JLabel settingLabel = new JLabel("");
    private List<Op> ops = Collections.emptyList();
    private String settingName;

    public DrawerPreviewDialog(Window owner, String title, boolean modal){
        super(owner, title, modal ? ModalityType.DOCUMENT_MODAL : ModalityType.MODELESS);
        setLayout(new MigLayout("", "", ""));
        northPanel = makeNorth();
        add(northPanel, "dock north");
        add(previewPane);
        bind();
        pack();
    }

    public void setImageSize(double imageWidth, double imageHeight){
        previewPane.setImageSize(imageWidth, imageHeight);
    }

    public void setPreviewPaneSize(int width, int height){
        previewPane.setPreferredSize(new Dimension(width, height));
        previewPane.repaint();
        previewPane.revalidate();
        pack();
    }

    public void render(List<Op> ops){
        previewPane.setOps(ops);
        this.ops = ops;
        repaint();
        revalidate();
    }

    public void setPrinterSetting(String settingName){
        this.settingName = settingName;
        String settingDisp = settingName;
        if( settingName == null || settingName.isEmpty() ){
            settingDisp = "(プリンター未選択)";
        }
        settingLabel.setText(settingDisp);
        repaint();
        revalidate();
        pack();
    }

    private JPanel makeNorth(){
        JPanel panel = new JPanel(new MigLayout("", "", ""));
        panel.add(printButton);
        panel.add(settingLabel);
        return panel;
    }

    private void bind(){
        printButton.addActionListener(event -> doPrint());
    }

    private void doPrint(){
        DrawerPrinter drawerPrinter = new DrawerPrinter();
        byte[] devmode = null, devnames = null;
        AuxSetting auxSetting = null;
        try {
            if (settingName != null && PrinterSetting.INSTANCE.nameExists(settingName)) {
                devmode = PrinterSetting.INSTANCE.readDevmode(settingName);
                devnames = PrinterSetting.INSTANCE.readDevnames(settingName);
                auxSetting = PrinterSetting.INSTANCE.readAuxSetting(settingName);
            }
            if (devmode == null) {
                DrawerPrinter.DialogResult result = drawerPrinter.printDialog(null, null);
                if (result.ok) {
                    devmode = result.devmodeData;
                    devnames = result.devnamesData;
                } else {
                    return;
                }
            }
            drawerPrinter.print(ops, devmode, devnames);
            dispose();
        } catch(IOException ex){
            ex.printStackTrace();
            alert(ex.toString());
        }
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message.toString());
    }

}
