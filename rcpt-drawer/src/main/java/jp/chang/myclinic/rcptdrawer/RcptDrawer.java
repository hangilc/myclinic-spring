package jp.chang.myclinic.rcptdrawer;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;
import jp.chang.myclinic.drawer.Op;

import java.util.ArrayList;
import java.util.List;

class RcptDrawer {

    private DrawerCompiler compiler = new DrawerCompiler();
    private List<List<Op>> pages = new ArrayList<>();

    RcptDrawer() {
        setupFonts();
        compiler.setFont("Gothic3");
        compiler.textInJustified("診療報酬明細書", new Box(22, 20, 46, 23), VAlign.Bottom);
    }

    List<List<Op>> getPages(){
        pages.add(compiler.getOps());
        return pages;
    }

    private void setupFonts(){
        compiler.createFont("Gothic3", "MS Gothic", 3);
        compiler.createFont("Gothic2.2", "MS Gothic", 2.2);
    }

}
