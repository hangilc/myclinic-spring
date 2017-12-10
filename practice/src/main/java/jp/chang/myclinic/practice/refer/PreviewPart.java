package jp.chang.myclinic.practice.refer;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.drawer.previewkit.PreviewPane;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.List;

class PreviewPart extends JPanel {

    private PreviewPane previewPane = new PreviewPane(PaperSize.A4.getWidth(), PaperSize.A4.getHeight(), 0.6);

    PreviewPart(){
        setLayout(new MigLayout("insets 0", "[grow]", "[grow]"));
        add(previewPane, "wrap");
    }

    void render(List<Op> ops){
        previewPane.setOps(ops);
    }
}
