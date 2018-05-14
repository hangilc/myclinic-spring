package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class Visit {

    //private static Logger logger = LoggerFactory.getLogger(Visit.class);

    @JsonProperty("受診日")
    String visitedAt;
    @JacksonXmlElementWrapper(localName="診療", useWrapping=false)
    @JsonProperty("診療")
    List<Shinryou> shinryouList;

    Visit() {

    }

}
