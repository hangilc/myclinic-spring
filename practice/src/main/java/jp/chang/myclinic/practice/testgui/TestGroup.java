package jp.chang.myclinic.practice.testgui;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TestGroup implements TestRunnerInterface {

    private Map<String, TestRunnerInterface> tests = new LinkedHashMap<>();

    public void addTestProc(String name, TestProc proc){
        tests.put(name, proc);
    }

    public void addTestGroup(String name, TestGroup group){
        tests.put(name, group);
    }

    private void runAll() {
        for(String name: tests.keySet()){
            tests.get(name).runTest(null);
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
        TestRunnerInterface runner = tests.getOrDefault(key, null);
        if( runner == null ){
            System.err.printf("cannot find test: %s\n", key);
            System.exit(1);
        }
        if( parts.length <= 1 ){
            runner.runTest(null);
        } else {
            runner.runTest(parts[1]);
        }
    }

}
