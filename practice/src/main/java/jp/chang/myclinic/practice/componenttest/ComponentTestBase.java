package jp.chang.myclinic.practice.componenttest;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jp.chang.myclinic.mockdata.MockData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ComponentTestBase implements TestInterface, ComponentTestMixin {

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
                    invokeTest(method);
                }
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public boolean testOne(String className, String methodName){
        if( getClass().getSimpleName().equals(className) ) {
            try {
                for (Method method : getClass().getMethods()) {
                    if (method.isAnnotationPresent(CompTest.class)) {
                        if (method.getName().equals(methodName)) {
                            invokeTest(method);
                            return true;
                        }
                    }
                }
                return false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return false;
        }
    }

    private void invokeTest(Method method) throws InvocationTargetException, IllegalAccessException {
        String title = getClass().getSimpleName() + ":" + method.getName();
        gui(() -> {
            stage.setTitle(title);
        });
        method.invoke(this);
        System.out.println(title);
    }

}
