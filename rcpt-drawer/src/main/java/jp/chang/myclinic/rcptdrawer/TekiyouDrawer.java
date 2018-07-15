package jp.chang.myclinic.rcptdrawer;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.Box.VertAnchor;
import jp.chang.myclinic.drawer.Box.HorizAnchor;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;

import java.util.List;
import java.util.Set;

import static jp.chang.myclinic.rcptdrawer.TekiyouLineOpt.*;

class TekiyouDrawer {

    private DrawerCompiler compiler;
    private Box indexColumnSave;
    private Box bodyColumnSave;
    private Box rightColumnSave;
    private Box indexColumn;
    private Box bodyColumn;
    private Box rightColumn;
    private Runnable newPageProc;
    private Box rightBracketColumn;
    private RightBracket rightBracket;
    private static int leftColumnWidth = 10;
    private static int rightColumnWidth = 10;

    TekiyouDrawer(DrawerCompiler compiler, Box tekiyouBox, Runnable newPageProc) {
        this.compiler = compiler;
        Box[] cc = tekiyouBox.splitToColumns(
                leftColumnWidth,
                tekiyouBox.getWidth() - rightColumnWidth
        );
        indexColumnSave = indexColumn = cc[0].inset(0, 1);
        bodyColumnSave = bodyColumn = cc[1].inset(1, 1);
        rightColumnSave = rightColumn = cc[2].inset(0, 1);
        rightBracketColumn = bodyColumn.setWidth(1.5, HorizAnchor.Right);
        this.newPageProc = newPageProc;
    }

    void draw(List<TekiyouLine> tekiyouLines){
        compiler.setFont("Gothic3");
        for(TekiyouLine tekiyouLine: tekiyouLines){
            double leftMargin = tekiyouLine.leftMargin;
            List<String> lines = compiler.breakLine(tekiyouLine.body, bodyColumn.getWidth() - leftMargin);
            for(int i=0;i<lines.size();i++){
                String line = lines.get(i);
                boolean isLast = i == (lines.size() - 1);
                if( i == 0 ){
                    drawOneLine(tekiyouLine.index, line, tekiyouLine.tankaTimes, tekiyouLine.opts, true, isLast,
                            leftMargin);
                } else {
                    drawOneLine(null, line, null, tekiyouLine.opts, false, isLast, leftMargin);
                }
            }
            bodyColumn = bodyColumn.shrinkHeight(1, VertAnchor.Bottom);
            indexColumn = indexColumn.setTop(bodyColumn.getTop());
            rightColumn = rightColumn.setTop(bodyColumn.getTop());
        }
    }

    private boolean isNotNull(String s){
        return s != null && !s.isEmpty();
    }

    private void drawIndex(String index){
        compiler.textIn(index, indexColumn, HAlign.Center, VAlign.Top);
    }

    private void drawRight(String right){
        compiler.textIn(right, rightColumn, HAlign.Right, VAlign.Top);
    }

    private void drawOneLine(String index, String body, String right, Set<TekiyouLineOpt> opts,
                             boolean isLeadLine, boolean isLastLine, double leftMargin){
        if( bodyColumn.getHeight() < 3 ){
            newPage();
        }
        if( isNotNull(index) ){
            drawIndex(index);
        }
        if( isNotNull(right) ){
            drawRight(right);
        }
        HAlign halign = HAlign.Left;
        if( opts.contains(AlignRight) ){
            halign = HAlign.Right;
        }
        if( isLeadLine && opts.contains(GroupBegin) && !opts.contains(GroupEnd) ){
            openRightBracket(bodyColumn.getTop() + 0.1);
        }
        Box actualBodyColumn;
        if( leftMargin != 0 ){
            actualBodyColumn = bodyColumn.shrinkWidth(leftMargin, HorizAnchor.Right);
        } else {
            actualBodyColumn = bodyColumn;
        }
        compiler.textIn(body, actualBodyColumn, halign, VAlign.Top);
        bodyColumn = bodyColumn.shrinkHeight(3, VertAnchor.Bottom);
        indexColumn = indexColumn.setTop(bodyColumn.getTop());
        rightColumn = rightColumn.setTop(bodyColumn.getTop());
        if( isLastLine && opts.contains(GroupEnd) && !opts.contains(GroupBegin) ){
            closeRightBracket(bodyColumn.getTop() - 1.4 );
        }
    }

    private void openRightBracket(double y){
        rightBracket = new RightBracket(compiler, rightBracketColumn);
        rightBracket.open(y);
        bodyColumn = bodyColumn.shrinkWidth(rightBracket.getColumnWidth()+1,
                HorizAnchor.Left);
    }

    private void closeRightBracket(double y){
        rightBracket.close(y);
        rightBracket.render();
        rightBracket = null;
        bodyColumn.setWidth(bodyColumnSave.getWidth(), HorizAnchor.Left);
    }

    private void newPage(){
        if( rightBracket != null ){
            rightBracket.onPageEnding();
        }
        newPageProc.run();
        indexColumn = indexColumnSave;
        bodyColumn = bodyColumnSave;
        rightColumn = rightColumnSave;
        if( rightBracket != null ){
            rightBracket.onPageStarting();
        }
    }

}
