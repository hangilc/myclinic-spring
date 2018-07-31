package jp.chang.myclinic.rcptdrawer.tekiyou;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;

public class TekiyouContext {

    //private static Logger logger = LoggerFactory.getLogger(TekiyouContext.class);

    private DrawerCompiler compiler;
    private Box tekiyouBox;
    private Runnable newPageProc;
    private Box currentBox;
    private double leftColumnWidth = 10;
    private double rightColumnWidth = 10;
    private double lineLeading = 1;

    public TekiyouContext(DrawerCompiler compiler, Box tekiyouBox, Runnable newPageProc){
        this.compiler = compiler;
        this.tekiyouBox = tekiyouBox;
        this.newPageProc = newPageProc;
        initCurrentBox();
    }

    private void initCurrentBox(){
        this.currentBox = tekiyouBox.copy().inset(0, 1);
    }

    public boolean hasSpace(){
        return hasSpace(1);
    }

    public boolean hasSpace(int n){
        double required = compiler.getCurrentFontSize() * n;
        if( n > 1 ){
            required -= lineLeading * (n-1);
        }
        return currentBox.getHeight() >= required;
    }

    public DrawerCompiler getCompiler() {
        return compiler;
    }

    public Box getCurrentBox() {
        return currentBox;
    }

    public Box getLeftColumn(){
        return getCurrentBox().setWidth(leftColumnWidth, Box.HorizAnchor.Left);
    }

    public Box getRightColumn(){
        return getCurrentBox().setWidth(rightColumnWidth, Box.HorizAnchor.Right);
    }

    public Box getMiddleColumn(){
        return getCurrentBox().shrinkWidth(leftColumnWidth + 1, Box.HorizAnchor.Right)
                .shrinkWidth(rightColumnWidth + 1, Box.HorizAnchor.Left);
    }

    public void shrink(){
        currentBox = currentBox.shrinkHeight(compiler.getCurrentFontSize() + lineLeading, Box.VertAnchor.Bottom);
    }

    public void newPage(){
        newPageProc.run();
        initCurrentBox();
    }

    public void ensureSpace(){
        if( !hasSpace() ){
            newPage();
        }
    }

    public double getLineLeading() {
        return lineLeading;
    }

    public void setLineLeading(double lineLeading) {
        this.lineLeading = lineLeading;
    }
}
