package jp.chang.myclinic.rcptdrawer;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;

import java.util.ArrayList;
import java.util.List;

class RightBracket {

    private DrawerCompiler compiler;
    private Box column;
    private List<Runnable> procs = new ArrayList<>();

    RightBracket(DrawerCompiler compiler, Box column) {
        this.compiler = compiler;
        this.column = column;
    }

    double getColumnWidth(){
        return column.getWidth();
    }

    void open(double y){
        procs.add(() -> compiler.line(column.getLeft(), y, column.getRight(), y));
    }

    void onPageEnding(){
        render();
        compiler.lineTo(column.getRight(), column.getBottom());
    }

    void onPageStarting(){
        procs.add(() -> compiler.moveTo(column.getRight(), column.getTop()));
    }

    void close(double y){
        procs.add(() -> compiler.lineTo(column.getRight(), y));
        procs.add(() -> compiler.lineTo(column.getLeft(), y));
    }

    void render(){
        procs.forEach(Runnable::run);
        procs = new ArrayList<>();
    }

}
