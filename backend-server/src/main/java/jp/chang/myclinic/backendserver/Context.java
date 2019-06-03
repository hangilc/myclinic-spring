package jp.chang.myclinic.backendserver;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.logdto.practicelog.PracticeLogDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class Context {

    private Context() {

    }

    public static ObjectMapper mapper = new ObjectMapper();
    public static Function<LocalDate, List<PracticeLogDTO>> listAllPracticeLog;
}
