package jp.chang.myclinic.support.houkatsukensa;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;

public class HoukatsuKensaFile implements HoukatsuKensaService{

    private HoukatsuKensa houkatsuKensa;

    public HoukatsuKensaFile(Path dataPath) {
        try {
            this.houkatsuKensa = HoukatsuKensa.load(dataPath);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public HoukatsuKensa.Revision getRevision(LocalDate at) {
        return houkatsuKensa.findRevision(at);
    }

    public static void main(String[] args){
        HoukatsuKensaService service = new HoukatsuKensaFile(Paths.get("config/houkatsu-kensa.xml"));
        HoukatsuKensa.Revision rev = service.getRevision(LocalDate.now());
        System.out.println(rev);
    }
}
