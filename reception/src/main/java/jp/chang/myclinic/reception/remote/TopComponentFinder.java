package jp.chang.myclinic.reception.remote;

import javafx.stage.Window;
import jp.chang.myclinic.reception.Main;

class TopComponentFinder implements ComponentFinder {


    @Override
    public Object findComponent(String selector) {
        if(selector.equals("MainPane")){
            return Main.appMainPane;
        } else {
            for(Window w: javafx.stage.Window.getWindows()) {
                if( w instanceof NameProvider ){
                    NameProvider np = (NameProvider)w;
                    if (np.getNameProviderName().equals(selector)) {
                        return w;
                    }
                }
            }
            return null;
        }
    }
}
