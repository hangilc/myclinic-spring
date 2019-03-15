package jp.chang.myclinic.apitool.lib.gentablebase;

public interface DatabaseSpecifics {

    String projectPackage();
    default String tableBasePackage(){
        return projectPackage() + ".tablebase";
    }

}
