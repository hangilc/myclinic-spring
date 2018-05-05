package jp.chang.myclinic.mastermap.generated;

import java.time.LocalDate;
import jp.chang.myclinic.mastermap.Resolver;

public class ResolvedDiseaseAdjMap {
	
	public int 疑い;

	public ResolvedDiseaseAdjMap(){}

	public ResolvedDiseaseAdjMap(Resolver resolver, LocalDate at){
		this.疑い = resolver.resolve("疑い", at);
	}
}
