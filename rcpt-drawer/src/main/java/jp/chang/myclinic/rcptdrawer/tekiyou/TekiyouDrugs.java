package jp.chang.myclinic.rcptdrawer.tekiyou;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;

import java.util.List;

public class TekiyouDrugs implements Tekiyou {

    //private static Logger logger = LoggerFactory.getLogger(TekiyouDrugs.class);
    private String index;
    private List<String> drugs;
    private String right;
    private double bracketColumnWidth = 3;
    private double bracketWidth = 1.5;
    private double bracketLeftMargin = (bracketColumnWidth - bracketWidth) / 2.0;

    public TekiyouDrugs(String index, List<String> drugs, String right) {
        this.index = index;
        this.drugs = drugs;
        this.right = right;
    }

    @Override
    public void render(TekiyouContext ctx) {
        DrawerCompiler compiler = ctx.getCompiler();
        Box middleBox = ctx.getMiddleColumn();
        double bracketLeft = middleBox.getRight() - bracketColumnWidth + bracketLeftMargin;
        double bracketRight = bracketLeft + bracketWidth;
        double bracketTop = middleBox.getTop() + compiler.getCurrentFontSize() * 0.3;
        double middleWidth = middleBox.getWidth();
        double drugWidth = middleWidth - bracketColumnWidth;
        ctx.ensureSpace();
        compiler.textIn(index, ctx.getLeftColumn(), HAlign.Center, VAlign.Top);
        compiler.textIn(right, ctx.getRightColumn(), HAlign.Right, VAlign.Top);
        boolean needBracket = drugs.size() > 1;
        boolean bracketStartDrawn = false;
        if( drugs.size() > 0 ){
            double leadingSave = ctx.getLineLeading();
            for(String drug: drugs){
                List<String> lines = compiler.breakLine(drug, drugWidth);
                for(String line: lines){
                    if( !ctx.hasSpace() ){
                        double bracketBottom = ctx.getMiddleColumn().getTop();
                        if( bracketStartDrawn ){
                            compiler.moveTo(bracketRight, bracketTop);
                            compiler.lineTo(bracketRight, bracketBottom);
                        } else {
                            compiler.moveTo(bracketLeft, bracketTop);
                            compiler.lineTo(bracketRight, bracketTop);
                            compiler.lineTo(bracketRight, bracketBottom);
                            bracketStartDrawn = true;
                        }
                        ctx.newPage();
                        bracketTop = ctx.getMiddleColumn().getTop();
                    }
                    compiler.textIn(line, ctx.getMiddleColumn(), HAlign.Left, VAlign.Top);
                    ctx.shrink();
                }
            }
            ctx.setLineLeading(leadingSave);
            if( needBracket ){
                double bracketBottom = ctx.getMiddleColumn().getTop() - compiler.getCurrentFontSize() * 0.7
                        - ctx.getLineLeading();
                if( !bracketStartDrawn ){
                    compiler.moveTo(bracketLeft, bracketTop);
                    compiler.lineTo(bracketRight, bracketTop);
                    compiler.lineTo(bracketRight, bracketBottom);
                    compiler.lineTo(bracketLeft, bracketBottom);
                } else {
                    compiler.moveTo(bracketRight, bracketTop);
                    compiler.lineTo(bracketRight, bracketBottom);
                    compiler.lineTo(bracketLeft, bracketBottom);
                }
            }
        } else {
            ctx.shrink();
        }
    }

}
