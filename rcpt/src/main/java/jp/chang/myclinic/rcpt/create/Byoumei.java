package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
class Byoumei {

    private static Logger logger = LoggerFactory.getLogger(Byoumei.class);

    @JsonProperty("名称")
    String name;
    @JsonProperty("診療開始日")
    String startDate;
    @JsonProperty("転帰")
    String tenki;
    @JsonProperty("診療終了日")
    String endDate;

    Byoumei() {

    }

}
