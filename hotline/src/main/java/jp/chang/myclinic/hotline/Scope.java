package jp.chang.myclinic.hotline;

import java.util.function.Consumer;

public class Scope {

    //private static Logger logger = LoggerFactory.getLogger(Scope.class);
    private boolean beepEnabled = true;
    private Consumer<String> showErrorHandler = err -> {};
    private Runnable hideErrorHandler = () -> {};
    private Runnable resizeStageHandler = () -> {};

    public Scope() {

    }

    public boolean isBeepEnabled() {
        return beepEnabled;
    }

    public void setBeepEnabled(boolean beepEnabled) {
        this.beepEnabled = beepEnabled;
    }

    public void setShowErrorHandler(Consumer<String> showErrorHandler) {
        this.showErrorHandler = showErrorHandler;
    }

    public void showError(String message){
        showErrorHandler.accept(message);
    }

    public void setHideErrorHandler(Runnable hideErrorHandler) {
        this.hideErrorHandler = hideErrorHandler;
    }

    public void hideError(){
        hideErrorHandler.run();
    }

    public void setResizeStageHandler(Runnable resizeStageHandler) {
        this.resizeStageHandler = resizeStageHandler;
    }

    public void resizeStage(){
        resizeStageHandler.run();
    }

}
