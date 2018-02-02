package jp.chang.myclinic.practice.javafx;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import jp.chang.myclinic.dto.*;
import jp.chang.myclinic.util.KouhiUtil;
import jp.chang.myclinic.util.KoukikoureiUtil;
import jp.chang.myclinic.util.RoujinUtil;
import jp.chang.myclinic.util.ShahokokuhoUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HokenSelectPane extends VBox {

    private interface Hoken<T> {
        T getDTO();
        String rep();
        int getIndex();
    }

    private static abstract class HokenBase<T> {
        private T dto;

        HokenBase(T dto){
            this.dto = dto;
        }

        public T getDTO(){
            return dto;
        }
    }

    private static class Shahokokuho extends HokenBase<ShahokokuhoDTO> implements Hoken<ShahokokuhoDTO> {

        Shahokokuho(ShahokokuhoDTO dto){
            super(dto);
        }

        public String rep(){
            return ShahokokuhoUtil.rep(getDTO());
        }

        public int getIndex(){
            return getDTO().shahokokuhoId;
        }

    }

    private static class Koukikourei extends HokenBase<KoukikoureiDTO> implements Hoken<KoukikoureiDTO> {

        Koukikourei(KoukikoureiDTO dto){
            super(dto);
        }

        public String rep(){
            return KoukikoureiUtil.rep(getDTO());
        }

        public int getIndex(){
            return getDTO().koukikoureiId;
        }

    }

    private static class Roujin extends HokenBase<RoujinDTO> implements Hoken<RoujinDTO> {

        Roujin(RoujinDTO dto){
            super(dto);
        }

        public String rep(){
            return RoujinUtil.rep(getDTO());
        }

        public int getIndex(){
            return getDTO().roujinId;
        }

    }

    private static class Kouhi extends HokenBase<KouhiDTO> implements Hoken<KouhiDTO> {

        Kouhi(KouhiDTO dto){
            super(dto);
        }

        public String rep(){
            return KouhiUtil.rep(getDTO());
        }

        public int getIndex(){
            return getDTO().kouhiId;
        }

    }

    private static class HokenCheckBox<T> extends CheckBox {

        private Hoken<T> hoken;

        HokenCheckBox(Hoken<T> hoken){
            this.hoken = hoken;
            setText(hoken.rep());
        }

        Hoken<T> getHoken(){
            return hoken;
        }

        int getSelectedIndex(){
            if( isSelected() ){
                return hoken.getIndex();
            } else {
                return 0;
            }
        }

    }

    private HokenDTO current;
    private List<KouhiDTO> currentKouhiList;
    private HokenCheckBox<ShahokokuhoDTO> shahokokuhoCheck;
    private HokenCheckBox<KoukikoureiDTO> koukikoureiCheck;
    private HokenCheckBox<RoujinDTO> roujinCheck;
    private List<HokenCheckBox<KouhiDTO>> kouhiChecks = new ArrayList<>();

    public HokenSelectPane(HokenDTO available, HokenDTO current){
        super(2);
        this.current = current;
        currentKouhiList = Stream.of(current.kouhi1, current.kouhi2, current.kouhi3)
                .filter(Objects::nonNull).collect(Collectors.toList());
        {
            ShahokokuhoDTO dto = available.shahokokuho;
            if( dto != null ){
                shahokokuhoCheck = new HokenCheckBox<>(new Shahokokuho(dto));
                shahokokuhoCheck.setSelected(isCurrentShahokokuho(dto));
                getChildren().add(shahokokuhoCheck);
            }
        }
        {
            KoukikoureiDTO dto = available.koukikourei;
            if( dto != null ){
                koukikoureiCheck = new HokenCheckBox<>(new Koukikourei(dto));
                koukikoureiCheck.setSelected(isCurrentKoukikourei(dto));
                getChildren().add(koukikoureiCheck);
            }
        }
        {
            RoujinDTO dto = available.roujin;
            if( dto != null ){
                roujinCheck = new HokenCheckBox<>(new Roujin(dto));
                roujinCheck.setSelected(isCurrentRoujin(dto));
                getChildren().add(roujinCheck);
            }
        }
        Stream.of(available.kouhi1, available.kouhi2, available.kouhi3).forEach(dto -> {
            HokenCheckBox<KouhiDTO> check = new HokenCheckBox<>(new Kouhi(dto));
            check.setSelected((isCurrentKouhi(dto)));
            kouhiChecks.add(check);
            getChildren().add(check);
        });

    }

    public void storeTo(VisitDTO visit){
        visit.shahokokuhoId = shahokokuhoCheck.getSelectedIndex();
        visit.koukikoureiId = koukikoureiCheck.getSelectedIndex();
        visit.roujinId = roujinCheck.getSelectedIndex();
        List<KouhiDTO> selectedKouhi = kouhiChecks.stream()
                .filter(ch -> ch.getSelectedIndex() > 0)
                .map(ch -> ch.getHoken().getDTO())
                .collect(Collectors.toList());
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
