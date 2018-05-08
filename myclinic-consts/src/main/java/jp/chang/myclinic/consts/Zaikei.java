package jp.chang.myclinic.consts;

public enum Zaikei {

    Naifuku('1'),
    Other('3'),
    Chuusha('4'),
    Gaiyou('6'),
    ShikaYakuzai('8'),
    ShikaTokutei('9');

    private char code;

    Zaikei(char code){
        this.code = code;
    }

    public char getCode(){
        return code;
    }

    public static Zaikei fromCode(char code){
        for(Zaikei zaikei: values()){
            if( zaikei.code == code ){
                return zaikei;
            }
        }
        return null;
    }
}
