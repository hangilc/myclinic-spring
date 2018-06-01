package jp.chang.myclinic.rcptdrawer;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;
import jp.chang.myclinic.drawer.Box.VertAnchor;
import static jp.chang.myclinic.rcptdrawer.TekiyouLineOpt.*;

import java.util.List;
import java.util.Set;

class TekiyouDrawer {

    private DrawerCompiler compiler;
    private Box indexColumnSave;
    private Box bodyColumnSave;
    private Box rightColumnSave;
    private Box indexColumn;
    private Box bodyColumn;
    private Box rightColumn;
    private Runnable newPageProc;
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
        if( opts.contains())
        List<String> lines = compiler.breakLine(body, bodyColumn.getWidth());
        for(String line: lines){
            if( bodyColumn.getHeight() < 3 ){
                newPage();
            }
            compiler.textIn(line, bodyColumn, halign, VAlign.Top);
            bodyColumn = bodyColumn.shrinkHeight(3, VertAnchor.Bottom);
        }
        indexColumn = indexColumn.setTop(bodyColumn.getTop());
        rightColumn = rightColumn.setTop(bodyColumn.getTop());
    }

    private void newPage(){
        newPageProc.run();
        indexColumn = indexColumnSave;
        bodyColumn = bodyColumnSave;
        rightColumn = rightColumnSave;
    }

}
