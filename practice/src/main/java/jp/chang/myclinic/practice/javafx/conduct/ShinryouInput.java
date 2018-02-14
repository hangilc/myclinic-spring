package jp.chang.myclinic.practice.javafx.conduct;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import jp.chang.myclinic.practice.javafx.parts.DispGrid;
import jp.chang.myclinic.practice.lib.conduct.ConductShinryouInputInterface;

public class ShinryouInput extends DispGrid implements ConductShinryouInputInterface {

    private int shinryoucode = 0;
    private Text name = new Text("");

    public ShinryouInput(){
        addRow("名称：", new TextFlow(name));
    }

    @Override
    public int getShinryoucode() {
        return shinryoucode;
    }

    @Override
    public void setShinryoucode(int shinryoucode) {
        this.shinryoucode = shinryoucode;
    }

    @Override
    public void setName(String name) {
        this.name.setText(name);
    }
}
