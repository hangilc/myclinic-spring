package jp.chang.myclinic.practice.rightpane.disease.browsepane;

import jp.chang.myclinic.practice.Link;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;

class NavPart extends JPanel {

    interface Callback {
        default void gotoPage(int page, Runnable uiCallback){}
    }

    private int current;
    private int total;
    private JLabel infoLabel = new JLabel();
    private Callback callback = new Callback(){};

    NavPart(int currentValue, int totalValue){
        this.current = currentValue;
        this.total = totalValue;
        setLayout(new MigLayout("insets 0", "", ""));
        Link firstLink = new Link("最初");
        firstLink.setCallback(evt -> {
            if( current > 0 ){
                current = 0;
                callback.gotoPage(0, this::updateInfoLabel);
                updateInfoLabel();
            }
        });
        Link prevLink = new Link("前へ");
        prevLink.setCallback(evt -> {
            if( current > 0 ){
                current -= 1;
                callback.gotoPage(current, this::updateInfoLabel);
            }
        });
        Link nextLink = new Link("次へ");
        nextLink.setCallback(evt -> {
            if( current < (total - 1) ){
                current += 1;
                callback.gotoPage(current, this::updateInfoLabel);
                updateInfoLabel();
            }
        });
        Link lastLink = new Link("最後");
        lastLink.setCallback(evt -> {
            if( current < (total - 1) ){
                current = total - 1;
                callback.gotoPage(current, this::updateInfoLabel);
                updateInfoLabel();
            }
        });
        updateInfoLabel();
        add(firstLink);
        add(prevLink);
        add(nextLink);
        add(lastLink);
        add(infoLabel);
    }

    void setCallback(Callback callback){
        this.callback = callback;
    }

    private void updateInfoLabel(){
        if( total > 1 ) {
            infoLabel.setText(String.format("[%d/%d]", current + 1, total));
        }
    }

}
