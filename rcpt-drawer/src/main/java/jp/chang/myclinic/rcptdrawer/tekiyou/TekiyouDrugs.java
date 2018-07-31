package jp.chang.myclinic.rcptdrawer.tekiyou;

import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.*;

import java.util.List;

public class TekiyouDrugs implements Tekiyou {

    //private static Logger logger = LoggerFactory.getLogger(TekiyouDrugs.class);
    private String index;
    private List<String> drugs;
    private String right;

    public TekiyouDrugs(String index, List<String> drugs, String right) {
        this.index = index;
        this.drugs = drugs;
        this.right = right;
    }

    @Override
    public void render(TekiyouContext ctx) {
        DrawerCompiler compiler = ctx.getCompiler();
        ctx.ensureSpace();
        compiler.textIn(index, ctx.getLeftColumn(), HAlign.Center, VAlign.Top);
    }
}
