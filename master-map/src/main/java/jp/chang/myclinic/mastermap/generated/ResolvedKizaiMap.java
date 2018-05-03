package jp.chang.myclinic.mastermap.generated;

import jp.chang.myclinic.mastermap.Resolver;

public class ResolvedKizaiMap {
	
	
	public static int 半切;
	
	public static int 大角;
	
	public static int 四ツ切;
	

	public void init(Resolver resolver){
		
		this.半切 = resolver.resolve("半切");
		
		this.大角 = resolver.resolve("大角");
		
		this.四ツ切 = resolver.resolve("四ツ切");
		
	}
}
