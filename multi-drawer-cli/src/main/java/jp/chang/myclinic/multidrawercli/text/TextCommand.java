package jp.chang.myclinic.multidrawercli.text;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.multidrawer.text.TextDrawer;
import jp.chang.myclinic.multidrawercli.Command;

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
        String data = src.collect(Collectors.joining("\n"));
        TextDrawer drawer = new TextDrawer();
        return drawer.draw(data);
    }
}
