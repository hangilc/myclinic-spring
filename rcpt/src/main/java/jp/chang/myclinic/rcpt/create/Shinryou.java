package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonIgnoreProperties(ignoreUnknown = true)
class Shinryou {

    private static Logger logger = LoggerFactory.getLogger(Shinryou.class);

    @JsonProperty("診療コード")
    int shinryoucode;

    Shinryou() {

    }

}
