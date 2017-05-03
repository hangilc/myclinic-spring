package jp.chang.myclinic.dto;

public class RoujinDTO {
	public int roujinId;
	public int patientId;
	public int shichouson;
	public int jukyuusha;
	public int futanWari;
	public String validFrom;
	public String validUpto;

	public RoujinDTO copy(){
		RoujinDTO dst = new RoujinDTO();
		dst.roujinId = roujinId;
		dst.patientId = patientId;
		dst.shichouson = shichouson;
		dst.jukyuusha = jukyuusha;
		dst.futanWari = futanWari;
		dst.validFrom = validFrom;
		dst.validUpto = validUpto;
		return dst;
	}

	public void assign(RoujinDTO src){
		roujinId = src.roujinId;
		patientId = src.patientId;
		shichouson = src.shichouson;
		jukyuusha = src.jukyuusha;
		futanWari = src.futanWari;
		validFrom = src.validFrom;
		validUpto = src.validUpto;
	}

	@Override
	public String toString(){
		return "RoujinDTO[" +
			"roujinId=" + roujinId + "," +
			"patientId=" + patientId + "," +
			"shichouson=" + shichouson + "," +
			"jukyuusha=" + jukyuusha + "," +
			"futanWari=" + futanWari + "," +
			"validFrom=" + validFrom + "," +
			"validUpto=" + validUpto +
		"]";
	}
}