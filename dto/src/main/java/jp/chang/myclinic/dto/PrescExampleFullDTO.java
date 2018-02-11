package jp.chang.myclinic.dto;

public class PrescExampleFullDTO {
    public PrescExampleDTO prescExample;
    public IyakuhinMasterDTO master;

    public static PrescExampleFullDTO copy(PrescExampleFullDTO src){
        PrescExampleFullDTO dst = new PrescExampleFullDTO();
        dst.prescExample = src.prescExample;
        dst.master = src.master;
        return dst;
    }
}
