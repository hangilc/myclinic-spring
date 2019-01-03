package jp.chang.myclinic.management;

import jp.chang.myclinic.util.dto_logic.HokenLib;

public class AppendVerificationDigit {

    public static void main(String[] args){
        int i = Integer.parseInt(args[0]);
        int d = HokenLib.calcCheckingDigit(i);
        System.out.printf("%d%d\n", i, d);
    }

}
