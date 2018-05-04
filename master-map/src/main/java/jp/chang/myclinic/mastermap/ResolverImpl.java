package jp.chang.myclinic.mastermap;

import java.time.LocalDate;
import java.util.Optional;

class ResolverImpl implements Resolver {

    private NameMap nameMap;
    private CodeMap codeMap;

    ResolverImpl(NameMap nameMap, CodeMap codeMap) {
        this.nameMap = nameMap;
        this.codeMap = codeMap;
    }

    @Override
    public int resolve(String name, LocalDate at){
        Optional<Integer> optCode = nameMap.get(name);
        if( optCode.isPresent() ){
            int code = optCode.get();
            return codeMap.resolve(code, at);
        } else {
            throw new RuntimeException("Cannot find " + name + " in name map.");
        }
    }

}
