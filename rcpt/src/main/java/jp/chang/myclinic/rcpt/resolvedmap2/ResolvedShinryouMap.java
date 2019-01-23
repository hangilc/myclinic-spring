package jp.chang.myclinic.rcpt.resolvedmap2;

import jp.chang.myclinic.client.Service;
import jp.chang.myclinic.mastermap2.MasterNameMap;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ResolvedShinryouMap {

    @MasterNameMap(candidates = {"単純撮影（アナログ撮影）", "単純撮影（撮影）"})
    public int 単純撮影;
    @MasterNameMap(candidates = "単純撮影（イ）の写真診断")
    public int 単純撮影診断;
    @MasterNameMap(candidates = {"皮下、筋肉内注射", "皮内、皮下及び筋肉内注射"})
    public int 皮下筋注;
    @MasterNameMap(candidates = {"静脈内注射"})
    public int 静注;
    @MasterNameMap(candidates = {"初診", "初診料"})
    public int 初診;
    @MasterNameMap(candidates = {"再診（診療所）", "再診", "再診料"})
    public int 再診;
    @MasterNameMap(candidates = {"同日再診", "同日再診料"})
    public int 同日再診;
    public int 外来管理加算;
    public int 往診;
    public int 尿一般;
    @MasterNameMap(candidates = {"潜血（便）"})
    public int 便潜血;
    @MasterNameMap(candidates = {"尿・糞便等検査判断料"})
    public int 尿便検査判断料;
    @MasterNameMap(candidates = {"血液学的検査判断料"})
    public int 血液検査判断料;
    @MasterNameMap(candidates = {"生化学的検査（１）判断料"})
    public int 生化Ⅰ判断料;
    @MasterNameMap(candidates = {"生化学的検査（２）判断料"})
    public int 生化Ⅱ判断料;
    @MasterNameMap(candidates = {"免疫学的検査判断料"})
    public int 免疫検査判断料;
    @MasterNameMap(candidates = {""})
    public int 微生物検査判断料;
    @MasterNameMap(candidates = {""})
    public int 病理判断料;
    @MasterNameMap(candidates = {""})
    public int 静脈採血;
    @MasterNameMap(candidates = {""})
    public int 内服調剤;
    @MasterNameMap(candidates = {""})
    public int 外用調剤;
    @MasterNameMap(candidates = {""})
    public int 心電図;
    @MasterNameMap(candidates = {""})
    public int 処方料;
    @MasterNameMap(candidates = {""})
    public int 処方料７;
    @MasterNameMap(candidates = {""})
    public int 処方せん料;
    @MasterNameMap(candidates = {""})
    public int 処方せん料７;
    @MasterNameMap(candidates = {""})
    public int 調基;
    @MasterNameMap(candidates = {""})
    public int 特定疾患処方;
    @MasterNameMap(candidates = {""})
    public int 特定疾患処方管理加算処方せん料;
    @MasterNameMap(candidates = {""})
    public int 長期処方;
    @MasterNameMap(candidates = {""})
    public int 長期投薬加算処方せん料;
    @MasterNameMap(candidates = {""})
    public int 特定疾患管理;
    @MasterNameMap(candidates = {""})
    public int 薬剤情報提供;
    @MasterNameMap(candidates = {""})
    public int 手帳記載加算;
    @MasterNameMap(candidates = {""})
    public int 診療情報提供料１;
    @MasterNameMap(candidates = {""})
    public int 訪問看護指示料;
    @MasterNameMap(candidates = {""})
    public int 療養費同意書交付料;
    @MasterNameMap(candidates = {""})
    public int 向精神薬;
    @MasterNameMap(candidates = {""})
    public int 外来後発加算１;
    @MasterNameMap(candidates = {""})
    public int 骨塩定量ＭＤ法;
    @MasterNameMap(candidates = {""})
    public int 血算;
    @MasterNameMap(candidates = {""})
    public int 末梢血液像;
    @MasterNameMap(candidates = {""})
    public int ＨｂＡ１ｃ;
    @MasterNameMap(candidates = {""})
    public int ＰＴ;
    @MasterNameMap(candidates = {""})
    public int ＧＯＴ;
    @MasterNameMap(candidates = {""})
    public int ＧＰＴ;
    @MasterNameMap(candidates = {""})
    public int γＧＴＰ;
    @MasterNameMap(candidates = {""})
    public int ＣＰＫ;
    @MasterNameMap(candidates = {""})
    public int クレアチニン;
    @MasterNameMap(candidates = {""})
    public int 尿酸;
    @MasterNameMap(candidates = {""})
    public int ＬＤＬコレステロール;
    @MasterNameMap(candidates = {""})
    public int ＨＤＬコレステロール;
    @MasterNameMap(candidates = {""})
    public int ＴＧ;
    @MasterNameMap(candidates = {""})
    public int グルコース;
    @MasterNameMap(candidates = {""})
    public int カリウム;
    @MasterNameMap(candidates = {""})
    public int ＴＳＨ;
    @MasterNameMap(candidates = {""})
    public int ＦＴ４;
    @MasterNameMap(candidates = {""})
    public int ＦＴ３;
    @MasterNameMap(candidates = {""})
    public int ＰＳＡ;
    @MasterNameMap(candidates = {""})
    public int 蛋白定量尿;
    @MasterNameMap(candidates = {""})
    public int クレアチニン尿;
    @MasterNameMap(candidates = {""})
    public int ＣＲＰ;
    @MasterNameMap(candidates = {""})
    public int 非特異的ＩｇＥ;
    public Map<String, Integer> nameMap;

    public CompletableFuture<Void> resolveAt(LocalDate at) {
        List<List<String>> args = new ArrayList<>();
        for (Field fld : this.getClass().getFields()) {
            if (fld.getType() == Integer.TYPE) {
                List<String> arg = new ArrayList<>();
                arg.add(fld.getName());
                if (fld.isAnnotationPresent(MasterNameMap.class)) {
                    MasterNameMap annot = fld.getAnnotation(MasterNameMap.class);
                    arg.addAll(Arrays.asList(annot.candidates()));
                }
                args.add(arg);
            }
        }
        return Service.api.batchResolveShinryouNames(at, args)
                .thenAccept(map -> {
                    this.nameMap = map;
                    try {
                        for (Field fld : this.getClass().getFields()) {
                            if (fld.getType() == Integer.TYPE) {
                                String name = fld.getName();
                                if (map.containsKey(name)) {
                                    fld.setInt(this, map.get(name));
                                }
                            }
                        }
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    @Override
    public String toString() {
        return "ResolvedShinryouMap{" +
                "単純撮影=" + 単純撮影 +
                ", 単純撮影診断=" + 単純撮影診断 +
                ", 皮下筋注=" + 皮下筋注 +
                ", 静注=" + 静注 +
                ", 初診=" + 初診 +
                ", 再診=" + 再診 +
                ", 同日再診=" + 同日再診 +
                ", 外来管理加算=" + 外来管理加算 +
                ", 往診=" + 往診 +
                ", 尿一般=" + 尿一般 +
                ", 便潜血=" + 便潜血 +
                ", 尿便検査判断料=" + 尿便検査判断料 +
                ", 血液検査判断料=" + 血液検査判断料 +
                ", 生化Ⅰ判断料=" + 生化Ⅰ判断料 +
                ", 生化Ⅱ判断料=" + 生化Ⅱ判断料 +
                ", 免疫検査判断料=" + 免疫検査判断料 +
                ", 微生物検査判断料=" + 微生物検査判断料 +
                ", 病理判断料=" + 病理判断料 +
                ", 静脈採血=" + 静脈採血 +
                ", 内服調剤=" + 内服調剤 +
                ", 外用調剤=" + 外用調剤 +
                ", 心電図=" + 心電図 +
                ", 処方料=" + 処方料 +
                ", 処方料７=" + 処方料７ +
                ", 処方せん料=" + 処方せん料 +
                ", 処方せん料７=" + 処方せん料７ +
                ", 調基=" + 調基 +
                ", 特定疾患処方=" + 特定疾患処方 +
                ", 特定疾患処方管理加算処方せん料=" + 特定疾患処方管理加算処方せん料 +
                ", 長期処方=" + 長期処方 +
                ", 長期投薬加算処方せん料=" + 長期投薬加算処方せん料 +
                ", 特定疾患管理=" + 特定疾患管理 +
                ", 薬剤情報提供=" + 薬剤情報提供 +
                ", 手帳記載加算=" + 手帳記載加算 +
                ", 診療情報提供料１=" + 診療情報提供料１ +
                ", 訪問看護指示料=" + 訪問看護指示料 +
                ", 療養費同意書交付料=" + 療養費同意書交付料 +
                ", 向精神薬=" + 向精神薬 +
                ", 外来後発加算１=" + 外来後発加算１ +
                ", 骨塩定量ＭＤ法=" + 骨塩定量ＭＤ法 +
                ", 血算=" + 血算 +
                ", 末梢血液像=" + 末梢血液像 +
                ", ＨｂＡ１ｃ=" + ＨｂＡ１ｃ +
                ", ＰＴ=" + ＰＴ +
                ", ＧＯＴ=" + ＧＯＴ +
                ", ＧＰＴ=" + ＧＰＴ +
                ", γＧＴＰ=" + γＧＴＰ +
                ", ＣＰＫ=" + ＣＰＫ +
                ", クレアチニン=" + クレアチニン +
                ", 尿酸=" + 尿酸 +
                ", ＬＤＬコレステロール=" + ＬＤＬコレステロール +
                ", ＨＤＬコレステロール=" + ＨＤＬコレステロール +
                ", ＴＧ=" + ＴＧ +
                ", グルコース=" + グルコース +
                ", カリウム=" + カリウム +
                ", ＴＳＨ=" + ＴＳＨ +
                ", ＦＴ４=" + ＦＴ４ +
                ", ＦＴ３=" + ＦＴ３ +
                ", ＰＳＡ=" + ＰＳＡ +
                ", 蛋白定量尿=" + 蛋白定量尿 +
                ", クレアチニン尿=" + クレアチニン尿 +
                ", ＣＲＰ=" + ＣＲＰ +
                ", 非特異的ＩｇＥ=" + 非特異的ＩｇＥ +
                '}';
    }
}
