package jp.chang.myclinic.mastermap.generated;

import jp.chang.myclinic.mastermap.Resolver;

import java.time.LocalDate;

public class ResolvedDiseaseAdjMap {
	
	public int 疑い;

	public ResolvedDiseaseAdjMap(Resolver resolver, LocalDate at){
		this.疑い = resolver.resolve("疑い", at);
	}
}
