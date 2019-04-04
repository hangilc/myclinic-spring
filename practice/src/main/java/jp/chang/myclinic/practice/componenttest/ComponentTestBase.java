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
                    stage.setTitle(getClass().getSimpleName() + ":" + method.getName());
                    method.invoke(this);
                }
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void confirm(boolean cond){
        if( !cond ){
            throw new RuntimeException("confirmation failuer");
        }
    }


}
