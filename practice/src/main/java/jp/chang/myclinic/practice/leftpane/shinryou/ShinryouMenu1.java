package jp.chang.myclinic.practice.leftpane.shinryou;

import jp.chang.myclinic.practice.Link;
import jp.chang.myclinic.practice.MainExecContext;
import jp.chang.myclinic.practice.leftpane.WorkArea2;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class ShinryouMenu1 extends JPanel {

    private WorkArea2 workArea;

    ShinryouMenu1(int visitId, int currentVisitId, int tempVisitIs){
//        setLayout(new MigLayout("insets 0", "[] [] [grow]", ""));
//        Link shohouLink = new Link("[診療行為]");
//        shohouLink.setCallback(event -> doMainItem());
//        Link auxLink = new Link("[+]");
//        add(shohouLink);
//        add(auxLink);
    }

    ShinryouMenu1(int visitId, int width, MainExecContext mainExecContext){
        setLayout(new MigLayout("insets 0", "[] []", ""));
        Link shohouLink = new Link("[診療行為]");
        shohouLink.setCallback(event -> doMainItem());
        Link auxLink = new Link("[+]");
        add(shohouLink);
        add(auxLink);
    }

    void doMainItem(){
        if( workArea != null ){
            if( workArea.getComponent() instanceof AddRegularPane ){
                remove(workArea);
                workArea = null;
                repaint();
                revalidate();
            }
            return;
        }
        AddRegularPane addRegularPane = new AddRegularPane();
        workArea = new WorkArea2<AddRegularPane>("診療行為入力", addRegularPane);
        add(workArea, "newline, span, growx");
        revalidate();
    }
}
