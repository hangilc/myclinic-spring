package jp.chang.myclinic.rcpt.check;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.rcpt.resolvedmap.*;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class Listener extends RunListener {

    public static LocalDate at = LocalDate.of(2018, 3, 1);
    public static ResolvedMap resolvedMap;
    public static ResolvedShinryouMap shinryouMap;
    {
        resolvedMap = createMockResolvedMaps();
        shinryouMap = resolvedMap.shinryouMap;
    }
    public static ObjectMapper objectMapper;

    @Override
    public void testRunStarted(Description description) throws Exception {
        System.out.println("Test Started...");
        objectMapper = new ObjectMapper();
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        System.out.println("Test Ended...");
    }

    private static ResolvedMap createMockResolvedMaps(){
        ResolvedMap resolvedMap = new ResolvedMap();
        ResolvedShinryouMap shinryouMap = new ResolvedShinryouMap();
        shinryouMap.resolveAt(at, createMockResolver()).join();
        resolvedMap.shinryouMap = shinryouMap;
        ResolvedKizaiMap kizaiMap = new ResolvedKizaiMap();
        kizaiMap.resolveAt(at, createMockResolver()).join();
        resolvedMap.kizaiMap = kizaiMap;
        ResolvedDiseaseMap diseaseMap = new ResolvedDiseaseMap();
        diseaseMap.resolveAt(at, createMockResolver()).join();
        resolvedMap.diseaseMap = diseaseMap;
        ResolvedDiseaseAdjMap adjMap = new ResolvedDiseaseAdjMap();
        adjMap.resolveAt(at, createMockResolver()).join();
        resolvedMap.diseaseAdjMap = adjMap;
        return resolvedMap;
    }

    private static ResolvedMapBase.Resolver createMockResolver(){
        return new ResolvedMapBase.Resolver(){

            @Override
            public CompletableFuture<Map<String, Integer>> resolve(LocalDate at, List<List<String>> args) {
                int serial = 1;
                Map<String, Integer> map = new HashMap<>();
                for(List<String> arg: args){
                    map.put(arg.get(0), serial++);
                }
                return CompletableFuture.completedFuture(map);
            }

        };
    }

}
