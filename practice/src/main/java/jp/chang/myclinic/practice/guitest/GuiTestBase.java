package jp.chang.myclinic.practice.guitest;

import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GuiTestBase implements TestInterface, GuiTestMixin {

    protected Stage stage;
    protected StackPane main;

    public GuiTestBase(Stage stage, StackPane main) {
        this.stage = stage;
        this.main = main;
    }

    public void testAll(){
        try {
            for (Method method : getClass().getMethods()) {
                if (method.isAnnotationPresent(GuiTest.class)) {
                    invokeTest(method);
                }
            }
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public boolean testOne(String className, String methodName){
        if( getClass().getSimpleName().equals(className) ) {
            if( methodName == null || methodName.isEmpty() ){
                testAll();
                return true;
            }
            try {
                for (Method method : getClass().getMethods()) {
                    if (method.isAnnotationPresent(GuiTest.class)) {
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
