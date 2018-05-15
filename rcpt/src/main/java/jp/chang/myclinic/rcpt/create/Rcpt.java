package jp.chang.myclinic.rcpt.create;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@JacksonXmlRootElement(localName = "レセプト")
@JsonIgnoreProperties(ignoreUnknown = true)
class Rcpt {

    private static Logger logger = LoggerFactory.getLogger(Rcpt.class);

    @JsonProperty("元号")
    String gengou;
    @JsonProperty("年")
    int nen;
    @JsonProperty("月")
    int month;
    @JsonProperty("都道府県番号")
    int todoufukenBangou;
    @JsonProperty("医療機関コード")
    String kikancode;
    @JsonProperty("医療機関住所")
    String clinicAddress;
    @JsonProperty("医療機関電話")
    String clinicPhone;
    @JsonProperty("医療機関名称")
    String clinicName;
    @JacksonXmlElementWrapper(localName = "請求", useWrapping = false)
    @JsonProperty("請求")
    List<Seikyuu> seikyuuList;

    Rcpt() {

    }

}
