package jp.chang.myclinic.dto;

public class GazouLabelDTO {
	public int conductId;
	public String label;

	@Override
	public String toString(){
		return "GazouLabelDTO[" +
			"conductId=" + conductId + "," +
			"label=" + label +
		"]";
	}
}