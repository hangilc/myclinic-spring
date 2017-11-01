package jp.chang.myclinic.drawer.lib;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

public class Link extends JLabel {

    private static Cursor handCursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    private Consumer<MouseEvent> callback;

    public Link(String text){
        this(text, null);
    }

    public Link(String text, Consumer<MouseEvent> callback){
        super(text);
        this.callback = callback;
        setForeground(Color.BLUE);
        setCursor(handCursor);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e) {
                invokeCallback(e);
            }
        });
    }

    private void invokeCallback(MouseEvent e){
        if( callback != null ){
            callback.accept(e);
        }
    }

    public void setCallback(Consumer<MouseEvent> callback){
        this.callback = callback;
    }
}
