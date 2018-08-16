package jp.chang.myclinic.mastermap.next;

import java.util.HashMap;
import java.util.Map;

public enum MapKind {
    Iyakuhin('y', 'Y'),
    Shinryou('s', 'S'),
    Kizai('k', 'K'),
    Disease('d', 'D'),
    DiseaseAdj('a', 'A');

    private char nameMapKey;
    private char codeMapKey;
    private static Map<Character, MapKind> nameKindMap = new HashMap<>();
    static {
        for(MapKind k: MapKind.values()){
            nameKindMap.put(k.nameMapKey, k);
        }
    }
    private static Map<Character, MapKind> codeKindMap = new HashMap<>();
    static {
        for(MapKind k: MapKind.values()){
            codeKindMap.put(k.codeMapKey, k);
        }
    }

    MapKind(char nameMapKey, char codeMapKey){
        this.nameMapKey = nameMapKey;
        this.codeMapKey = codeMapKey;
    }

    public char getNameMapKey() {
        return nameMapKey;
    }

    public char getCodeMapKey() {
        return codeMapKey;
    }

    public static MapKind fromNameKey(char nameKey){
       return nameKindMap.get(nameKey);
    }

    public static MapKind fromCodeKey(char codeKey){
        return codeKindMap.get(codeKey);
    }

}
