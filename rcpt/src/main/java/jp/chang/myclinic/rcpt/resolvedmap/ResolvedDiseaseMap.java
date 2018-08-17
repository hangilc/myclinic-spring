package jp.chang.myclinic.rcpt.resolvedmap;

import java.time.LocalDate;

public class ResolvedDiseaseMap {
	
	public int 急性上気道炎;
	public int アレルギー性鼻炎;
	public int 糖尿病;
	public int 低血糖発作;
	public int 前立腺癌;

	public ResolvedDiseaseMap(){}

	public ResolvedDiseaseMap(Resolver resolver, LocalDate at){
		this.急性上気道炎 = resolver.resolve("急性上気道炎", at);
		this.アレルギー性鼻炎 = resolver.resolve("アレルギー性鼻炎", at);
		this.糖尿病 = resolver.resolve("糖尿病", at);
		this.低血糖発作 = resolver.resolve("低血糖発作", at);
		this.前立腺癌 = resolver.resolve("前立腺癌", at);
	}
}
