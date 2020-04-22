package jp.chang.myclinic.multidrawercli.seal8x3;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.multidrawer.seal8x3.Seal8x3Data;
import jp.chang.myclinic.multidrawer.seal8x3.Seal8x3Drawer;
import jp.chang.myclinic.multidrawercli.Command;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Seal8x3Command implements Command {

    @Override
    public String getDescription() {
        return "prints 8 x 3 label seal (A4)";
    }

    @Override
    public void initialize(List<String> args) {

    }

    @Override
    public List<List<Op>> render(Stream<String> src) throws Exception {
        String json = getSourceAsString(src);
        ObjectMapper mapper = new ObjectMapper();
        Seal8x3Data data = mapper.readValue(json, new TypeReference<Seal8x3Data>() {});
        Seal8x3Drawer drawer = new Seal8x3Drawer();
        return drawer.draw(data);
    }
}
