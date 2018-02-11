package jp.chang.myclinic.practice.javafx;

import javafx.application.Platform;
import jp.chang.myclinic.practice.lib.PracticeFun;

public class FunJavaFX {

    public static PracticeFun INSTANCE = new PracticeFun(Platform::runLater);

}
