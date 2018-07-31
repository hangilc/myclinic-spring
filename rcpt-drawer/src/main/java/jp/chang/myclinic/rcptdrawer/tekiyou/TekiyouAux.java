package jp.chang.myclinic.rcptdrawer.tekiyou;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.*;

import java.util.List;

public class TekiyouAux implements Tekiyou{

    //private static Logger logger = LoggerFactory.getLogger(TekiyouAux.class);
    private String text;
    private double leftMargin;

    public TekiyouAux(String text) {
        this.text = text;
    }

    public void setLeftMargin(double leftMargin) {
        this.leftMargin = leftMargin;
    }

    @Override
    public void render(TekiyouContext ctx) {
        DrawerCompiler compiler = ctx.getCompiler();
        Box middleBox = ctx.getMiddleColumn();
        List<String> lines = compiler.breakLine(text, middleBox.getWidth());
        for(String line: lines){
            ctx.ensureSpace();
            Box box = ctx.getMiddleColumn();
            box = box.shrinkWidth(leftMargin, Box.HorizAnchor.Right);
            compiler.textIn(line, box, HAlign.Left, VAlign.Top);
            ctx.shrink();
        }
    }
}
