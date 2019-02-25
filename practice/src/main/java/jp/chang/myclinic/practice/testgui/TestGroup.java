package jp.chang.myclinic.practice.testgui;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TestGroup implements TestRunnerInterface {

    private Map<String, Supplier<TestRunnerInterface>> tests = new LinkedHashMap<>();

    protected void addTestProc(String name, Runnable proc){
        tests.put(name, () -> (TestProc) proc::run);
    }

    protected void addTestProcSingleOnly(String name, Runnable proc){
        tests.put(name, () -> new TestProc(){
            @Override
            public void runProc() {
                proc.run();
            }

            @Override
            public boolean skipInBatch() {
                return true;
            }
        });
    }

    void addTestGroup(String name, Supplier<TestGroup> supplier){
        tests.put(name, supplier::get);
    }

    private void runAll() {
        for(String name: tests.keySet()){
            TestRunnerInterface runner = tests.get(name).get();
            if( !runner.skipInBatch() ){
                runner.runTest(null);
            }
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
        Supplier<TestRunnerInterface> runner = tests.getOrDefault(key, null);
        if( runner == null ){
            System.err.printf("cannot find test: %s\n", key);
            System.exit(1);
        }
        if( parts.length <= 1 ){
            runner.get().runTest(null);
        } else {
            runner.get().runTest(parts[1]);
        }
    }

}
