package jp.chang.myclinic.rcpt.resolvedmap;

import java.time.LocalDate;

public class ResolvedDiseaseAdjMap {
	
	public int 疑い;

	public ResolvedDiseaseAdjMap(){}

	public ResolvedDiseaseAdjMap(Resolver resolver, LocalDate at){
		this.疑い = resolver.resolve("疑い", at);
	}
}
