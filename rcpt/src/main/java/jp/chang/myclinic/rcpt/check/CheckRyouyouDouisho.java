package jp.chang.myclinic.rcpt.check;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.dto.ShinryouAttrDTO;
import jp.chang.myclinic.rcpt.resolvedmap.ResolvedShinryouMap;

import java.io.IOException;

class CheckRyouyouDouisho extends CheckBase {

    //private static Logger logger = LoggerFactory.getLogger(CheckRyouyouDouisho.class);

    CheckRyouyouDouisho(Scope scope) {
        super(scope);
    }

    void check(){
        ResolvedShinryouMap sm = getShinryouMaster();
        forEachShinryouInVisits(shinryou -> {
            if( shinryou.shinryou.shinryoucode == sm.療養費同意書交付料 ){
                try {
                    ShinryouAttrDTO attr = Service.api.findShinryouAttrCall(shinryou.shinryou.shinryouId).execute().body();
                    if( attr == null ){
                        String msg = "療養費同意書交付料に病名の摘要がありません。";
                        error(msg);
                    }
                } catch (IOException e) {
                    System.err.println("Failed access service.");
                }
            }
        });
    }

}
