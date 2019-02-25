package jp.chang.myclinic.practice.testgui;

public interface TestRunnerInterface {

    void runTest(String test);
    default boolean skipInBatch(){ return false; }

}
