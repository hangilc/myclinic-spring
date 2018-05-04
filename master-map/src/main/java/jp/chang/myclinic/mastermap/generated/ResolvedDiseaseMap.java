package jp.chang.myclinic.mastermap.generated;

import java.time.LocalDate;
import jp.chang.myclinic.mastermap.Resolver;

public class ResolvedDiseaseMap {
	
	public int 急性上気道炎;
	public int アレルギー性鼻炎;
	public int 糖尿病;

	public ResolvedDiseaseMap(Resolver resolver, LocalDate at){
		this.急性上気道炎 = resolver.resolve("急性上気道炎", at);
		this.アレルギー性鼻炎 = resolver.resolve("アレルギー性鼻炎", at);
		this.糖尿病 = resolver.resolve("糖尿病", at);
	}
}
