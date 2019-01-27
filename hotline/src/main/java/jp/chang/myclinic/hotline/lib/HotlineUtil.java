package jp.chang.myclinic.hotline.lib;

import jp.chang.myclinic.dto.HotlineDTO;
import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.util.DateTimeUtil;
import jp.chang.myclinic.util.kanjidate.KanjiDateRepBuilder;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

public class HotlineUtil {

    public static String makeHotlinePrefix(String sender, int hotlineId){
        return String.format("%s(%03d)> ", sender, hotlineId % 1000);
    }

    public static HotlineDTO createHotlineDTO(String sender, String recipient, String message){
        HotlineDTO hotline = new HotlineDTO();
        hotline.sender = sender;
        hotline.recipient = recipient;
        hotline.message = message;
        hotline.postedAt = new KanjiDateRepBuilder(LocalDateTime.now()).sqldate().build();
        return hotline;
    }

    public static CompletableFuture<Integer>  postMessge(String sender, String recipient, String message){
        HotlineDTO hotline = createHotlineDTO(sender, recipient, message);
        return Service.api.enterHotline(hotline);
    }

}
