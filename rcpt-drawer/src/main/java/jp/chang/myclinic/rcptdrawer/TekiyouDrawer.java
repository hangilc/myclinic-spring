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
            if( isNotNull(tekiyouLine.index) ){
                drawIndex(tekiyouLine.index);
            }
            if( isNotNull(tekiyouLine.tankaTimes) ){
                drawRight(tekiyouLine.tankaTimes);
            }
            drawBody(tekiyouLine.body, tekiyouLine.opts);
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

    private void drawBody(String body, Set<TekiyouLineOpt> opts){
        if( bodyColumn.getHeight() < 3 ){
            newPage();
        }
        if( body == null ){
            body = "";
        }
        HAlign halign = HAlign.Left;
        if( opts.contains(AlignRight) ){
            halign = HAlign.Right;
        }
        if( opts.contains(GroupBegin) && !opts.contains(GroupEnd) ){
            openRightBracket(bodyColumn.getTop() + 0.1);
        }
        List<String> lines = compiler.breakLine(body, bodyColumn.getWidth());
        for(String line: lines){
            if( bodyColumn.getHeight() < 3 ){
                if( rightBracket != null ) {
                    rightBracket.onPageEnding();
                }
                newPage();
                if( rightBracket != null ) {
                    rightBracket.onPageStarting();
                }
            }
            compiler.textIn(line, bodyColumn, halign, VAlign.Top);
            bodyColumn = bodyColumn.shrinkHeight(3, VertAnchor.Bottom);
        }
        if( opts.contains(GroupEnd) && !opts.contains(GroupBegin) ){
            closeRightBracket(bodyColumn.getTop() - 1.4 );
        }
        indexColumn = indexColumn.setTop(bodyColumn.getTop());
        rightColumn = rightColumn.setTop(bodyColumn.getTop());
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
        newPageProc.run();
        indexColumn = indexColumnSave;
        bodyColumn = bodyColumnSave;
        rightColumn = rightColumnSave;
    }

}
