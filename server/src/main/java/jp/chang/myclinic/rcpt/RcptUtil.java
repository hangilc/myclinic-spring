package jp.chang.myclinic.rcpt;

public class RcptUtil {

	public static int touyakuKingakuToTen(double kingaku){
		if( kingaku <= 15.0 ){
			return 1;
		} else {
			return (int)Math.ceil((kingaku - 15)/10.0) + 1;
		}
	}

	public static int shochiKingakuToTen(double kingaku){
		if( kingaku <= 15 )
			return 0;
		else
			return (int)Math.ceil((kingaku - 15)/10) + 1;
	}

	public static int kizaiKingakuToTen(double kingaku){
		return (int)Math.round(kingaku/10.0);
	}

	public static int calcRcptAge(int bdYear, int bdMonth, int bdDay, int atYear, int atMonth){
	    int age;
		age = atYear - bdYear;
		if( atMonth < bdMonth ){
			age -= 1;
		} else if( atMonth == bdMonth ){
			if( bdDay != 1 ){
				age -= 1;
			}
		}
		return age;
	}

	public static int calcShahokokuhoFutanWariByAge(int age){
	    if( age < 3 )
	        return 2;
	    else if( age >= 70 )
	        return 2;
	    else
	        return 3;
	};

	public static int kouhiFutanWari(int futanshaBangou){
		if( futanshaBangou / 1000000 == 41 )
			return 1;
		else if( (futanshaBangou / 1000) == 80136 )
			return 1;
		else if( (futanshaBangou / 1000) == 80137 )
			return 0;
		else if( (futanshaBangou / 1000) == 81136 )
			return 1;
		else if( (futanshaBangou / 1000) == 81137 )
			return 0;
		else if( (futanshaBangou / 1000000) == 88 )
			return 0;
		else{
			System.out.println("unknown kouhi futansha: " + futanshaBangou);
			return 0;
		}
	}

	public static int calcCharge(int ten, int futanWari){
		int c = ten * futanWari;
		int r = c % 10;
		if( r < 5 )
			c -= r;
		else
			c += (10 - r);
		return c;
	}

}