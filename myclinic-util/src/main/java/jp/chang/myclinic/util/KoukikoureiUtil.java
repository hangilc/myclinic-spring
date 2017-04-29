package jp.chang.myclinic.util;

import jp.chang.myclinic.dto.KoukikoureiDTO;

public class KoukikoureiUtil {

	public static String rep(KoukikoureiDTO koukikoureiDTO){
		return "後期高齢" + koukikoureiDTO.futanWari + "割";
	}

}
