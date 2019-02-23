package jp.chang.myclinic.practice.testgui;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TestGroup implements TestRunnerInterface {

    private Map<String, Supplier<? extends TestRunnerInterface>> tests = new LinkedHashMap<>();

    public void addTestProc(String name, Supplier<TestProc> supplier){
        tests.put(name, supplier);
    }

    public void addTestGroup(String name, Supplier<? extends TestGroup> supplier){
        tests.put(name, supplier);
    }

    private void runAll() {
        for(String name: tests.keySet()){
            tests.get(name).get().runTest(null);
        }
    }

    @Override
    public void runTest(String test) {
        if( test == null || test.isEmpty() ){
            runAll();
            return;
        }
        String[] parts = test.split(":", 2);
        String key = parts[0];
        Supplier<? extends TestRunnerInterface> supplier = tests.getOrDefault(key, null);
        if( supplier == null ){
            System.err.printf("cannot find test: %s\n", key);
            System.exit(1);
        }
        if( parts.length <= 1 ){
            supplier.get().runTest(null);
        } else {
            supplier.get().runTest(parts[1]);
        }
    }

}
