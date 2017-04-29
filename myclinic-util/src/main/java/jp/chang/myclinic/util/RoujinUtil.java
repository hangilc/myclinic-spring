package jp.chang.myclinic.util;

import jp.chang.myclinic.dto.RoujinDTO;

public class RoujinUtil {

	public static String rep(RoujinDTO roujinDTO){
		return "老人" + roujinDTO.futanWari + "割";
	}

}
