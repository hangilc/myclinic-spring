package jp.chang.myclinic.dto;

public class ConductDTO {
	public int conductId;
	public int visitId;
	public int kind;

	@Override
	public String toString(){
		return "ConductDTO[" +
		"conductId" + conductId + "," +
		"visitId" + visitId + "," +
		"kind" + kind + //"," +
		"]";
	}
}