package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonMerge;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.OptBoolean;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class Drug {

    @JacksonXmlElementWrapper(localName = "内服", useWrapping = false)
    @JsonProperty("内服")
    @JsonMerge(OptBoolean.TRUE)
    List<Naifuku> naifukuList = new ArrayList<>();
    @JacksonXmlElementWrapper(localName = "頓服", useWrapping = false)
    @JsonProperty("頓服")
    @JsonMerge(OptBoolean.TRUE)
    List<Tonpuku> tonpukuList = new ArrayList<>();
    @JacksonXmlElementWrapper(localName = "外用", useWrapping = false)
    @JsonProperty("外用")
    @JsonMerge(OptBoolean.TRUE)
    List<Gaiyou> gaiyouList = new ArrayList<>();

    @Override
    public String toString() {
        return "Drug{" +
                "naifukuList=" + naifukuList +
                ", tonpukuList=" + tonpukuList +
                ", gaiyouList=" + gaiyouList +
                '}';
    }
}
