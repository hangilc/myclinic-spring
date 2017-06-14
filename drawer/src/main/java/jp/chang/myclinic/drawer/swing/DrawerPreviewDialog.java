package jp.chang.myclinic.drawer.swing;

import jp.chang.myclinic.drawer.Op;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by hangil on 2017/06/14.
 */
public class DrawerPreviewDialog extends JDialog {

    private DrawerPreviewPane previewPane = new DrawerPreviewPane();

    public DrawerPreviewDialog(Window owner, String title, boolean modal){
        super(owner, title, modal ? ModalityType.DOCUMENT_MODAL : ModalityType.MODELESS);
        add(previewPane);
    }

    public void setImageSize(double imageWidth, double imageHeight){
        previewPane.setImageSize(imageWidth, imageHeight);
    }

    public void setPreviewPaneSize(int width, int height){
        previewPane.setPreferredSize(new Dimension(width, height));
        pack();
    }

    public void render(List<Op> ops){
        previewPane.setOps(ops);
        repaint();
        revalidate();
    }

}
