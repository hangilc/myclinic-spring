package jp.chang.myclinic.hotline.wrappedtext;

import javax.swing.*;
import java.awt.*;

public class Strut extends JComponent {

    public interface Callback {
        void run(int width);
    }

    private Callback callback;

    public Strut(Callback callback){
        this.callback = callback;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        EventQueue.invokeLater(() -> {
            getParent().remove(this);
            callback.run(width);
        });
    }
}
