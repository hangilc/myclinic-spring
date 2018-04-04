package jp.chang.myclinic.drawer.render;

import jp.chang.myclinic.drawer.DrawerCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import static jp.chang.myclinic.drawer.DrawerCompiler.VAlign;

public class StringRenderer implements Renderable {

    private static Logger logger = LoggerFactory.getLogger(StringRenderer.class);
    private String string;

    public StringRenderer(String string) {
        this.string = string;
    }

    @Override
    public void render(DrawerCompiler compiler, double x, double y, VAlign valign) {
        compiler.textAt(string, x, y, HAlign.Left, valign);
    }

    @Override
    public double calcWidth(DrawerCompiler compiler) {
        return compiler.calcTextWidth(string);
    }
}
