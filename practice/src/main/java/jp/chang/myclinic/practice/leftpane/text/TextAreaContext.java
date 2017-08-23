package jp.chang.myclinic.practice.leftpane.text;

import jp.chang.myclinic.dto.TextDTO;

import java.awt.*;

interface TextAreaContext {
    void onEditorCancel(TextEditor editor);
    void onTextDeleted(TextEditor editor, int textId);
    void onTextUpdated(TextDTO enteredText, TextEditor editor);

    static TextAreaContext get(Component comp){
        while( comp != null ){
            if( comp instanceof TextAreaContext ){
                return (TextAreaContext)comp;
            }
            comp = comp.getParent();
        }
        throw new RuntimeException("cannot find TextAreaContext");
    }
}
