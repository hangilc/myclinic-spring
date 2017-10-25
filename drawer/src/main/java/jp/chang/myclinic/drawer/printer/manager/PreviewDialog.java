package jp.chang.myclinic.drawer.printer.manager;

import jp.chang.myclinic.drawer.Op;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreviewDialog extends JDialog {

    private PreviewPane previewPane = new PreviewPane(14.8, 21.0);
    private List<List<Op>> pages = new ArrayList<>();

    public PreviewDialog(Window owner, String title){
        super(owner, title, ModalityType.DOCUMENT_MODAL);
        setLayout(new MigLayout("insets 0", "[grow]", "[grow]"));
        JScrollPane previewScroll = new JScrollPane(previewPane);
        add(previewScroll, "grow");
    }

    public PreviewDialog setPageSize(double width, double height){
        previewPane.setPageSize(width, height);
        return this;
    }

    public PreviewDialog setScale(double scale){
        previewPane.setScale(scale);
        return this;
    }

    public PreviewDialog setPages(List<List<Op>> pages){
        this.pages = pages;
        if( pages.size() > 0 ){
            List<Op> ops = pages.get(0);
            previewPane.setOps(ops);
        }
        return this;
    }

    public PreviewDialog setPage(List<Op> ops){
        List<List<Op>> pages = new ArrayList<>();
        pages.add(ops);
        return setPages(pages);
    }

}
