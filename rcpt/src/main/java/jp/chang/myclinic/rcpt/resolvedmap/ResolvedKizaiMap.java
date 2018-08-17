package jp.chang.myclinic.rcpt.resolvedmap;

import java.time.LocalDate;

public class ResolvedKizaiMap {
	
	public int 半切;
	public int 大角;
	public int 四ツ切;

	public ResolvedKizaiMap(){}

	public ResolvedKizaiMap(Resolver resolver, LocalDate at){
		this.半切 = resolver.resolve("半切", at);
		this.大角 = resolver.resolve("大角", at);
		this.四ツ切 = resolver.resolve("四ツ切", at);
	}
}
