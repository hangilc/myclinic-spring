package jp.chang.myclinic.drawer.printer.manager;

import jp.chang.myclinic.drawer.Op;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreviewDialog extends JDialog {

    private List<List<Op>> pages = Collections.emptyList();

    public PreviewDialog(Window owner, String title, double imageWidth, double imageHeight, List<Op> page{
        this(owner, title, imageWidth, imageHeight, 1);
        List<List<Op>> pages = new ArrayList<>();
        pages.add(page);
        setPages(pages);
    }

    public PreviewDialog(Window owner, String title, double imageWidth, double imageHeight, double scale){
        super(owner, title, ModalityType.DOCUMENT_MODAL);
        setLayout(new MigLayout("insets 0", "[grow]", "[grow]"));
        PreviewPane previewPane = new PreviewPane(imageWidth, imageHeight, scale);
        JScrollPane previewScroll = new JScrollPane(previewPane);
        add(previewScroll, "grow");
        pack();
    }

    public void setPages(List<List<Op>> pages){
        this.pages = pages;
    }

}
