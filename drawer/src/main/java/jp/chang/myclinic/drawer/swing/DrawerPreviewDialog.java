package jp.chang.myclinic.drawer.swing;

import jp.chang.myclinic.drawer.Op;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by hangil on 2017/06/14.
 */
public class DrawerPreviewDialog extends JDialog {

    private DrawerPreviewPane previewPane = new DrawerPreviewPane();
    private JPanel northPanel;
    private JButton printButton = new JButton("印刷");

    public DrawerPreviewDialog(Window owner, String title, boolean modal){
        super(owner, title, modal ? ModalityType.DOCUMENT_MODAL : ModalityType.MODELESS);
        setLayout(new MigLayout("", "", ""));
        northPanel = makeNorth();
        add(northPanel, "dock north");
        add(previewPane);
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
        repaint();
        revalidate();
    }

    private JPanel makeNorth(){
        JPanel panel = new JPanel(new MigLayout("", "", ""));
        panel.add(printButton);
        return panel;
    }

}
