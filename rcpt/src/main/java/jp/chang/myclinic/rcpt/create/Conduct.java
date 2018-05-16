package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Conduct {

    @JsonProperty("ラベル")
    String label;
    @JsonProperty("種類")
    String kind;
    @JacksonXmlElementWrapper(localName="診療", useWrapping=false)
    @JsonProperty("診療")
    List<ConductShinryou> shinryouList;
    @JacksonXmlElementWrapper(localName="薬剤", useWrapping=false)
    @JsonProperty("薬剤")
    List<ConductDrug> drugs;
    @JacksonXmlElementWrapper(localName="器材", useWrapping=false)
    @JsonProperty("器材")
    List<ConductKizai> kizaiList;

    Conduct() {

    }

}
