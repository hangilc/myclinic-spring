package jp.chang.myclinic.pharma.javafx.lib;

import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class HighlightText {

    private static Logger logger = LoggerFactory.getLogger(HighlightText.class);

    private HighlightText() { }

    private TextFlow highlight(String text, String hilight){
        TextFlow textFlow = new TextFlow();
        int lastEnd = 0;
        while(true){
            int start = text.indexOf(hilight, lastEnd);
            if( start < 0 ){
                break;
            }
            if( start > lastEnd ){
                textFlow.getChildren().add(new Text(text.substring(lastEnd, start)));
            }
            Text hilightText = new Text(text.substring(start, start + hilight.length()));
            hilightText.getStyleClass().add("hilight");
            textFlow.getChildren().add(hilightText);
            lastEnd = start + hilight.length();
        }
        if( lastEnd <text.length() ){
            textFlow.getChildren().add(new Text(text.substring(lastEnd, text.length())));
        }
        return textFlow;
    }

}
