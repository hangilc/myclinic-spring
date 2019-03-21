package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.AutoInc;
import jp.chang.myclinic.dto.annotation.Primary;

public class HotlineDTO {
    @Primary
    @AutoInc
    public int hotlineId;
    public String message;
    public String sender;
    public String recipient;
    public String postedAt;
}
