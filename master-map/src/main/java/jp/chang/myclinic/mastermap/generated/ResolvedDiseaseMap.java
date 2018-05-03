package jp.chang.myclinic.mastermap.generated;

import jp.chang.myclinic.mastermap.Resolver;

public class ResolvedDiseaseMap {
	
	
	public static int 急性上気道炎;
	
	public static int アレルギー性鼻炎;
	
	public static int 糖尿病;
	

	public void init(Resolver resolver){
		
		this.急性上気道炎 = resolver.resolve("急性上気道炎");
		
		this.アレルギー性鼻炎 = resolver.resolve("アレルギー性鼻炎");
		
		this.糖尿病 = resolver.resolve("糖尿病");
		
	}
}
