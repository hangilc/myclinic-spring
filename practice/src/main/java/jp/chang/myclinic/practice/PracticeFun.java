package jp.chang.myclinic.practice;

import javafx.application.Platform;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.WqueueFullDTO;
import jp.chang.myclinic.utilfx.GuiUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PracticeFun {

    //private static Logger logger = LoggerFactory.getLogger(PracticeFun.class);

    private PracticeFun() {

    }

    public static CompletableFuture<List<WqueueFullDTO>> listWqueue(){
        return Service.api.listWqueueFullForExam();
    }

}
