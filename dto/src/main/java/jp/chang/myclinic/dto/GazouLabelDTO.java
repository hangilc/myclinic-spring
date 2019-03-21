package jp.chang.myclinic.dto;

import jp.chang.myclinic.dto.annotation.Primary;

public class GazouLabelDTO {
	@Primary
	public int conductId;
	public String label;

	public static GazouLabelDTO copy(GazouLabelDTO src){
		GazouLabelDTO dst = new GazouLabelDTO();
		dst.conductId = src.conductId;
		dst.label = src.label;
		return dst;
	}

	@Override
	public String toString(){
		return "GazouLabelDTO[" +
			"conductId=" + conductId + "," +
			"label=" + label +
		"]";
	}
}