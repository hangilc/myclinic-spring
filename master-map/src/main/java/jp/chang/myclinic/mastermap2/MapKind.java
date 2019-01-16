package jp.chang.myclinic.mastermap2;

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

    MapKind(char nameMapKey, char codeMapKey){
        this.nameMapKey = nameMapKey;
        this.codeMapKey = codeMapKey;
    }

    public static MapKind fromNameKey(char nameKey){
        for(MapKind mk: MapKind.values()){
            if( mk.nameMapKey == nameKey){
                return mk;
            }
        }
        return null;
    }

    public static MapKind fromCodeKey(char codeKey){
        for(MapKind mk: MapKind.values()){
            if( mk.codeMapKey == codeKey ){
                return mk;
            }
        }
        return null;
    }

}
