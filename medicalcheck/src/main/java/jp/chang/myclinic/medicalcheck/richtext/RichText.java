package jp.chang.myclinic.medicalcheck.richtext;

import jp.chang.myclinic.drawer.render.Renderable;
import jp.chang.myclinic.drawer.render.StringRenderer;
import jp.chang.myclinic.drawer.render.SuperScriptRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RichText {

    private static Logger logger = LoggerFactory.getLogger(RichText.class);
    private static Pattern superscriptPattern = Pattern.compile("\\^\\{(\\d+)}");
    private String superscriptFont;

    public RichText(String superscriptFont){
        this.superscriptFont = superscriptFont;
    }

    public List<Renderable> parse(String src){
        Matcher matcher = superscriptPattern.matcher(src);
        List<Renderable> result = new ArrayList<>();
        int lastEnd = 0;
        while( matcher.find() ){
            int start = matcher.start();
            result.add(new StringRenderer(src.substring(lastEnd, start)));
            String superText = matcher.group(1);
            result.add(new SuperScriptRenderer(superText, superscriptFont));
            lastEnd = matcher.end();
        }
        if( lastEnd < src.length() ){
            result.add(new StringRenderer(src.substring(lastEnd, src.length())));
        }
        return result;
    }

}
