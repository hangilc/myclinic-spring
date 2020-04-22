package jp.chang.myclinic.multidrawercli.text;

import jp.chang.myclinic.drawer.Box;
import jp.chang.myclinic.drawer.DrawerCompiler;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.drawer.PaperSize;
import jp.chang.myclinic.multidrawercli.Command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TextCommand implements Command {

    @Override
    public String getDescription() {
        return "draws plain text";
    }

    @Override
    public void initialize(List<String> args) {

    }

    @Override
    public List<List<Op>> render(Stream<String> src) throws Exception {
        List<String> lines = src.collect(Collectors.toList());
        PaperSize paperSize = PaperSize.A4;
        Box box = new Box(paperSize);
        List<List<Op>> pages = new ArrayList<>();
        DrawerCompiler c = new DrawerCompiler();
        c.createFont("default", "MS Gothic", 8);
        c.setFont("default");
        c.tryMultilineText(lines, box, DrawerCompiler.HAlign.Left, DrawerCompiler.VAlign.Top, 1);
        pages.add(c.getOps());
        return pages;
    }
}
