package jp.chang.myclinic.server.rcpt;

import jp.chang.myclinic.dto.*;

public class FutanWari {
	public static int calcFutanWari(HokenDTO hoken, int rcptAge){
		int futanWari = 10;
		 if( hoken.shahokokuho != null ){
		 	futanWari = RcptUtil.calcShahokokuhoFutanWariByAge(rcptAge);
		 	if( hoken.shahokokuho.kourei > 0 ){
		 		futanWari = hoken.shahokokuho.kourei;
		 	}
		 }
		 if( hoken.koukikourei != null ){
		 	futanWari = hoken.koukikourei.futanWari;
		 }
		 if( hoken.roujin != null ){
		 	futanWari = hoken.roujin.futanWari;
		 }
		 for(KouhiDTO kouhi: new KouhiDTO[]{ hoken.kouhi1, hoken.kouhi2, hoken.kouhi3 }) {
			 if (kouhi == null) {
				 continue;
			 }
			 int kouhiFutanWari = RcptUtil.kouhiFutanWari(kouhi.futansha);
			 if (kouhiFutanWari < futanWari) {
				 futanWari = kouhiFutanWari;
			 }
		 }
		return futanWari;
	}
}