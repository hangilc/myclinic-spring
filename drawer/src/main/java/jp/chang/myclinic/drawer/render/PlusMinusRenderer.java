package jp.chang.myclinic.drawer.render;

import jp.chang.myclinic.drawer.DrawerCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static jp.chang.myclinic.drawer.DrawerCompiler.HAlign;
import static jp.chang.myclinic.drawer.DrawerCompiler.VAlign;

public class PlusMinusRenderer implements Renderable {

    private static Logger logger = LoggerFactory.getLogger(PlusMinusRenderer.class);
    private String plus = "＋";
    private String minus = "－";

    @Override
    public double render(DrawerCompiler compiler, double x, double y, VAlign valign) {
        compiler.textAt(plus, x, y, HAlign.Left, valign);
        compiler.textAt(minus, x, y + compiler.getCurrentFontSize()* 0.46, HAlign.Left, valign);
        return x + calcWidth(compiler);
    }

    @Override
    public double calcWidth(DrawerCompiler compiler) {
        return List.of(plus, minus).stream().mapToDouble(compiler::calcTextWidth).max().orElse(0);
    }
}
