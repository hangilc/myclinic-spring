package jp.chang.myclinic.practice;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Link extends JLabel {

    private static Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    private Runnable callback;

    public Link(String text){
        this(text, null);
    }

    public Link(String text, Runnable callback){
        super(text);
        this.callback = callback;
        setForeground(Color.BLUE);
        setCursor(handCursor);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                invokeCallback();
            }
        });
    }

    private void invokeCallback(){
        if( callback != null ){
            callback.run();
        }
    }

    public void setCallback(Runnable callback){
        this.callback = callback;
    }
}
