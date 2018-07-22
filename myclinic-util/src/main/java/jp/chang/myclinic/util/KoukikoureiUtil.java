package jp.chang.myclinic.util;

import jp.chang.myclinic.dto.KoukikoureiDTO;

public class KoukikoureiUtil {

	public static String rep(KoukikoureiDTO koukikoureiDTO){
		return rep(koukikoureiDTO.futanWari);
	}

	public static String rep(int futanWari){
		return "後期高齢" + futanWari + "割";
	}

}
