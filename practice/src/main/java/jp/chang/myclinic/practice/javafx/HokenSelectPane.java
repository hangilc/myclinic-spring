package jp.chang.myclinic.practice.javafx;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.KouhiUtil;
import jp.chang.myclinic.util.KoukikoureiUtil;
import jp.chang.myclinic.util.RoujinUtil;
import jp.chang.myclinic.util.ShahokokuhoUtil;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HokenSelectPane extends VBox {

    private HokenDTO current;
    private List<KouhiDTO> currentKouhiList;
    private CheckBox shahokokuhoCheck;
    private CheckBox koukikoureiCheck;
    private CheckBox roujinCheck;
    private CheckBox kouhi1Check;
    private CheckBox kouhi2Check;
    private CheckBox kouhi3Check;

    public HokenSelectPane(HokenDTO available, HokenDTO current){
        super(2);
        this.current = current;
        currentKouhiList = Stream.of(current.kouhi1, current.kouhi2, current.kouhi3)
                .filter(Objects::nonNull).collect(Collectors.toList());
        if( available.shahokokuho != null ){
            String text = ShahokokuhoUtil.rep(available.shahokokuho);
            CheckBox check = new CheckBox(text);
            check.setSelected(isCurrentShahokokuho(available.shahokokuho));
            getChildren().add(check);
            shahokokuhoCheck = check;
        }
        if( available.koukikourei != null ){
            String text = KoukikoureiUtil.rep(available.koukikourei);
            CheckBox check = new CheckBox(text);
            check.setSelected((isCurrentKoukikourei(available.koukikourei)));
            getChildren().add(check);
            koukikoureiCheck = check;
        }
        if( available.roujin != null ){
            String text = RoujinUtil.rep(available.roujin);
            CheckBox check = new CheckBox(text);
            check.setSelected((isCurrentRoujin(available.roujin)));
            getChildren().add(check);
            roujinCheck = check;
        }
        if( available.kouhi1 != null ){
            String text = KouhiUtil.rep(available.kouhi1);
            CheckBox check = new CheckBox(text);
            check.setSelected((isCurrentKouhi(available.kouhi1)));
            getChildren().add(check);
            kouhi1Check = check;
        }
        if( available.kouhi2 != null ){
            String text = KouhiUtil.rep(available.kouhi2);
            CheckBox check = new CheckBox(text);
            check.setSelected((isCurrentKouhi(available.kouhi2)));
            getChildren().add(check);
            kouhi2Check = check;
        }
        if( available.kouhi3 != null ){
            String text = KouhiUtil.rep(available.kouhi3);
            CheckBox check = new CheckBox(text);
            check.setSelected((isCurrentKouhi(available.kouhi3)));
            getChildren().add(check);
            kouhi3Check = check;
        }
    }

    private boolean isCurrentShahokokuho(ShahokokuhoDTO shahokokuho){
        return current.shahokokuho != null && shahokokuho != null &&
                current.shahokokuho.shahokokuhoId == shahokokuho.shahokokuhoId;
    }

    private boolean isCurrentKoukikourei(KoukikoureiDTO koukikourei){
        return current.koukikourei != null && koukikourei != null &&
                current.koukikourei.koukikoureiId == koukikourei.koukikoureiId;
    }

    private boolean isCurrentRoujin(RoujinDTO roujin){
        return current.roujin != null && roujin != null &&
                current.roujin.roujinId == roujin.roujinId;
    }

    private boolean isCurrentKouhi(KouhiDTO kouhi){
        int kouhiId = kouhi.kouhiId;
        for(KouhiDTO curr: currentKouhiList){
            if( curr.kouhiId == kouhiId ){
                return true;
            }
        }
        return false;
    }

}
