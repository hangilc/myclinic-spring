package jp.chang.myclinic.practice.componenttest;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ComponentTestBase implements ComponentTestMixin {

    protected Stage stage;
    protected Pane main;

    public ComponentTestBase(Stage stage, Pane main) {
        this.stage = stage;
        this.main = main;
    }

    public void testAll(){
        try {
            for (Method method : getClass().getMethods()) {
                if (method.isAnnotationPresent(CompTest.class)) {
                    CompTest compTest = method.getAnnotation(CompTest.class);
                    if( compTest.excludeFromBatch() ){
                        continue;
                    }
                    stage.setTitle(getClass().getSimpleName() + ":" + method.getName());
                    method.invoke(this);
                }
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public boolean testOne(String testName){
        try {
            for (Method method : getClass().getMethods()) {
                if (method.isAnnotationPresent(CompTest.class)) {
                    CompTest compTest = method.getAnnotation(CompTest.class);
                    if( testName.equals(compTest.name()) ){
                        stage.setTitle(getClass().getSimpleName() + ":" + method.getName());
                        method.invoke(this);
                        return true;
                    }
                }
            }
            return false;
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

}
