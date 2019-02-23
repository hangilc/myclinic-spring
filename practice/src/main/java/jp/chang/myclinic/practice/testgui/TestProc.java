package jp.chang.myclinic.practice.testgui;

public interface TestProc extends TestRunnerInterface {

    void runProc();

    @Override
    default void runTest(String test) {
        if( test == null || test.isEmpty() ){
            runProc();
        } else {
            throw new RuntimeException("no subtest: " + test);
        }
    }

}
