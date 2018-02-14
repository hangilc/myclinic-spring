package jp.chang.myclinic.practice.javafx.parts;

import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.dto.ConductShinryouDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ConductShinryouEdit extends DispGrid implements FormEditPart<ConductShinryouDTO> {

    private static Logger logger = LoggerFactory.getLogger(ConductShinryouEdit.class);

    private int shinryoucode = 0;
    private Text name = new Text("");

    public ConductShinryouEdit() {
        addRow("名称：", new TextFlow(name));
    }

    @Override
    public void setModel(ConductShinryouDTO model) {
        this.shinryoucode = model.shinryoucode;
        name.setText(model.name);
    }

    @Override
    public List<String> stuffInto(ConductShinryouDTO model) {
        return null;
    }

    @Override
    public Node asNode() {
        return this;
    }
}
