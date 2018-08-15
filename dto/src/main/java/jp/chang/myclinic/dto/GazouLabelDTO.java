package jp.chang.myclinic.dto;

public class GazouLabelDTO {
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