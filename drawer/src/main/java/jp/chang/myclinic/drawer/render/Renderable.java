package jp.chang.myclinic.drawer.render;

import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.DrawerCompiler.VAlign;

public interface Renderable {
    void render(DrawerCompiler compiler, double x, double y, VAlign valign); // returns horizontal advancement
    double calcWidth(DrawerCompiler compiler);
}
