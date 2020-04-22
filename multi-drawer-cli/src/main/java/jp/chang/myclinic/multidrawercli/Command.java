package jp.chang.myclinic.multidrawercli;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.multidrawer.DataDrawer;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface Command {

    String getDescription();
    void initialize(List<String> args);
    List<List<Op>> render(Stream<String> src) throws Exception;

    default String getSourceAsString(Stream<String> src){
        return src.collect(Collectors.joining("\n"));
    }

}
