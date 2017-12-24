package jp.chang.myclinic.hotline.lib;

import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.hotline.Service;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public class HotlineUtil {

    public static String makeHotlineText(String sender, int hotlineId, String message){
        return String.format("%s(%03d)> %s", sender, hotlineId % 1000, message);
    }

    public static CompletableFuture<Integer>  postMessge(String sender, String recipient, String message){
        HotlineDTO hotline = new HotlineDTO();
        hotline.sender = sender;
        hotline.recipient = recipient;
        hotline.message = message;
        hotline.postedAt = LocalDate.now().toString();
        return Service.api.enterHotline(hotline);
    }

}
