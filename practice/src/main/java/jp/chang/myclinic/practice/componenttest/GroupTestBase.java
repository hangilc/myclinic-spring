package jp.chang.myclinic.practice.componenttest;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class GroupTestBase implements TestInterface{

    protected abstract List<TestInterface> getTests();
    protected Stage stage;
    protected StackPane main;

    public GroupTestBase(Stage stage, StackPane main){
        this.stage = stage;
        this.main = main;
    }

    @Override
    public void testAll() {
        getTests().forEach(TestInterface::testAll);
    }

    @Override
    public boolean testOne(String className, String methodName) {
        for(TestInterface t: getTests()){
            if( t.testOne(className, methodName) ){
                return true;
            }
        }
        return false;
    }
}
