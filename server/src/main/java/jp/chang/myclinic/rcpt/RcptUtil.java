package jp.chang.myclinic.rcpt;

class RcptUtil {

	public static int touyakuKingakuToTen(double kingaku){
		if( kingaku <= 15.0 ){
			return 1;
		} else {
			return (int)Math.ceil((kingaku - 15)/10.0) + 1;
		}
	}

}