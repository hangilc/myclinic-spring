package jp.chang.myclinic.mastermap.next;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;

public class MasterMapManager {

    private NameMapRegistry nameMapRegistry;
    private CodeMapRegistry codeMapRegsitry;

    private MasterMapManager(NameMapRegistry nameMapRegistry, CodeMapRegistry codeMapRegistry) {
        this.nameMapRegistry = nameMapRegistry;
        this.codeMapRegsitry = codeMapRegistry;
    }

    public static MasterMapManager load(String nameMapConfigPath, String codeMapConfigPath) throws IOException {
        NameMapRegistry nameMapRegistry = NameMapRegistry.load(Paths.get(nameMapConfigPath));
        CodeMapRegistry codeMapRegistry = CodeMapRegistry.load(Paths.get(codeMapConfigPath));
        return new MasterMapManager(nameMapRegistry, codeMapRegistry);
    }

    public int resolve(MapKind mapKind, String name, LocalDate at){
        Integer code = nameMapRegistry.getNameMap(mapKind).get(name);
        if( code == null ){
            throw new RuntimeException("Cannot find code for " + name + ".");
        }
        return codeMapRegsitry.getCodeMap(mapKind).resolve(code, at);
    }

}
