package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class Drug {

    @JacksonXmlElementWrapper(localName="内服", useWrapping=false)
    @JsonProperty("内服")
    List<Naifuku> naifukuList;
    @JacksonXmlElementWrapper(localName="頓服", useWrapping=false)
    @JsonProperty("頓服")
    List<Tonpuku> tonpukuList;
    @JacksonXmlElementWrapper(localName="外用", useWrapping=false)
    @JsonProperty("外用")
    List<Gaiyou> gaiyouList;

    Drug() {

    }

    @Override
    public String toString() {
        return "Drug{" +
                "naifukuList=" + naifukuList +
                ", tonpukuList=" + tonpukuList +
                ", gaiyouList=" + gaiyouList +
                '}';
    }
}
