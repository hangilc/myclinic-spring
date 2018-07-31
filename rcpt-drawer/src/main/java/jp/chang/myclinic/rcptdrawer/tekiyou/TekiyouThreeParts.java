package jp.chang.myclinic.rcptdrawer.tekiyou;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;

import java.util.List;

public class TekiyouThreeParts implements Tekiyou {

    //private static Logger logger = LoggerFactory.getLogger(TekiyouThreeParts.class);
    private String index;
    private String middle;
    private String right;

    public TekiyouThreeParts(String index, String middle, String right) {
        this.index = index;
        this.middle = middle;
        this.right = right;
    }

    public TekiyouThreeParts(String middle){
        this("", middle, "");
    }

    @Override
    public void render(TekiyouContext ctx) {
        DrawerCompiler compiler = ctx.getCompiler();
        ctx.ensureSpace();
        if( !index.isEmpty() ) {
            compiler.textIn(index, ctx.getLeftColumn(), HAlign.Center, VAlign.Top);
        }
        if( !right.isEmpty() ) {
            compiler.textIn(right, ctx.getRightColumn(), HAlign.Right, VAlign.Top);
        }
        if( !middle.isEmpty() ) {
            double leadingSave = ctx.getLineLeading();
            ctx.setLineLeading(0);
            Box middleBox = ctx.getMiddleColumn();
            List<String> lines = compiler.breakLine(middle, middleBox.getWidth());
            for(String line: lines){
                ctx.ensureSpace();
                middleBox = ctx.getMiddleColumn();
                compiler.textIn(line, middleBox, HAlign.Left, VAlign.Top);
                ctx.shrink();
            }
            ctx.setLineLeading(leadingSave);
        } else {
            ctx.shrink();
        }
    }

}
