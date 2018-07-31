package jp.chang.myclinic.rcptdrawer.tekiyou;

import jp.chang.myclinic.drawer.DrawerCompiler.*;

public class TekiyouShoukiTitle implements Tekiyou {

    //private static Logger logger = LoggerFactory.getLogger(TekiyouShoukiTitle.class);

    @Override
    public void render(TekiyouContext ctx) {
        ctx.shiftDown(2);
        ctx.getCompiler().textIn("【症状詳記】", ctx.getCurrentBox(), HAlign.Left, VAlign.Top);
        ctx.shrink();
    }
}
