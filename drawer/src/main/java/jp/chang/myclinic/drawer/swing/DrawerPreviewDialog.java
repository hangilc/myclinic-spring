package jp.chang.myclinic.drawer.swing;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.printer.manage.PrinterSetting;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DrawerPreviewDialog extends JDialog {

    private DrawerPreviewPane previewPane = new DrawerPreviewPane();
    private JPanel northPanel;
    private JButton printButton = new JButton("印刷");
    private JLabel settingLabel = new JLabel("");
    private JLabel leftNavButton = new JLabel("<html><font color=\"#0000ff\">&lt;</font></html>");
    private JLabel navStateLabel = new JLabel("");
    private JLabel rightNavButton = new JLabel("<html><font color=\"#0000ff\">&gt;</font></html>");
    private List<List<Op>> pages = new ArrayList<>();
    private int pageIndex;
    private String settingName;

    public DrawerPreviewDialog(Window owner, String title, boolean modal){
        super(owner, title, modal ? ModalityType.DOCUMENT_MODAL : ModalityType.MODELESS);
        setLayout(new MigLayout("fill, insets 0 5 5 5", "", ""));
        northPanel = makeNorth();
        add(northPanel, "dock north");
        {
            JScrollPane sp = new JScrollPane(previewPane);
            add(sp, "grow");
        }
        bind();
        pack();
    }

    public void setImageSize(double imageWidth, double imageHeight){
        previewPane.setImageSize(imageWidth, imageHeight);
        previewPane.repaint();
        previewPane.revalidate();
        pack();
    }

    public void setPreviewPaneSize(int width, int height){
        previewPane.setPreferredSize(new Dimension(width, height));
        previewPane.repaint();
        previewPane.revalidate();
        pack();
    }

    public void render(List<Op> ops){
        pages.clear();
        pages.add(ops);
        doRender(ops);
    }

    private void doRender(List<Op> ops){
        previewPane.setOps(ops);
        previewPane.repaint();
        previewPane.revalidate();
    }

    public void renderPages(List<List<Op>> pages){
        this.pages = pages;
        pageIndex = 0;
        doRender(pages.size() > 0 ? pages.get(0) : Collections.emptyList());
        addNav();
        pack();
    }

    public void changePages(List<List<Op>> pages){
        this.pages = pages;
        pageIndex = 0;
        doRender(pages.size() > 0 ? pages.get(0) : Collections.emptyList());
        navStateLabel.setText(getNavLabelString());
    }

    public void addComponent(JComponent component){
        northPanel.add(component);
    }

    private void addNav(){
        leftNavButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        rightNavButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        navStateLabel.setText(getNavLabelString());
        northPanel.add(leftNavButton);
        northPanel.add(navStateLabel);
        northPanel.add(rightNavButton);
        northPanel.revalidate();
        bindNav();
    }

    private void bindNav(){
        leftNavButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                doNavLeft();
                pack();
            }
        });
        rightNavButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                doNavRight();
                pack();
            }
        });
    }

    private void doNavLeft(){
        int newPage = pageIndex - 1;
        if( newPage >= 0 && newPage < pages.size() ){
            pageIndex = newPage;
            doRender(pages.get(pageIndex));
            navStateLabel.setText(getNavLabelString());
        }
    }

    private void doNavRight(){
        int newPage = pageIndex + 1;
        if( newPage < pages.size() ){
            pageIndex = newPage;
            doRender(pages.get(pageIndex));
            navStateLabel.setText(getNavLabelString());
        }
    }

    private String getNavLabelString(){
        if( pages.size() == 0 ){
            return "0/0";
        }
        return String.format("%d/%d", pageIndex+1, pages.size());
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
        PrinterSetting.INSTANCE.printPages(pages, settingName);
        dispose();
        /*
        DrawerPrinter drawerPrinter = new DrawerPrinter();
        byte[] devmode = null, devnames = null;
        AuxSetting auxSetting = null;
        try {
            if (settingName != null && PrinterSetting.INSTANCE.nameExists(settingName)) {
                devmode = PrinterSetting.INSTANCE.readDevmode(settingName);
                devnames = PrinterSetting.INSTANCE.readDevnames(settingName);
                auxSetting = PrinterSetting.INSTANCE.readAuxSetting(settingName);
                // TODO: use auxSetting
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
            drawerPrinter.printPages(pages, devmode, devnames);
            dispose();
        } catch(IOException ex){
            ex.printStackTrace();
            alert(ex.toString());
        }
        */
    }

    private void alert(String message){
        JOptionPane.showMessageDialog(this, message);
    }

}
