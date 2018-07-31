package jp.chang.myclinic.rcptdrawer.tekiyou;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.*;

import java.util.List;

public class TekiyouByoumei implements Tekiyou {

    //private static Logger logger = LoggerFactory.getLogger(TekiyouByoumei.class);
    private String byoumei;
    private String date;

    public TekiyouByoumei(String byoumei, String date) {
        this.byoumei = byoumei;
        this.date = date;
    }

    @Override
    public void render(TekiyouContext context) {
        Box middleBox = context.getMiddleColumn();
        DrawerCompiler compiler = context.getCompiler();
        List<String> lines = compiler.breakLine(byoumei, middleBox.getWidth());
        double leadingSave = context.getLineLeading();
        context.setLineLeading(0);
        for(String line: lines){
            context.ensureSpace();
            Box box = context.getMiddleColumn();
            compiler.textIn(line, box, HAlign.Left, VAlign.Top);
            context.shrink();
        }
        context.ensureSpace();
        compiler.textIn(date, context.getMiddleColumn(), HAlign.Right, VAlign.Top);
        context.shrink();
        context.setLineLeading(leadingSave);
    }
}
