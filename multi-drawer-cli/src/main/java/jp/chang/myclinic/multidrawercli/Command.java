package jp.chang.myclinic.multidrawercli;

import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.multidrawer.DataDrawer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Stream;

public interface Command {

    String getDescription();
    void initialize(List<String> args);
    List<List<Op>> render(Stream<String> src) throws Exception;
}
