package jp.chang.myclinic.util.dto_logic;

import jp.chang.myclinic.dto.KouhiDTO;
import jp.chang.myclinic.util.logic.*;

public class KouhiLogic extends LogicUtil {

    //private static Logger logger = LoggerFactory.getLogger(KouhiLogic.class);

    private KouhiLogic() {

    }

    public static void todoufukenIsValidInKouhiFutansha(Integer futanshaBangou, String name,
                                                        ErrorMessages em) {
        int rem = futanshaBangou % 1000000;
        int todoufuken = rem / 10000;
        if (!HokenLib.isValidTodoufukenBangou(todoufuken)) {
            em.add(nameWith(name, "の") + "負担者番号の都道府県部分が不適切です。");
        }
    }

    public static Logic<Integer> isValidKouhiFutanshaBangou(Logic<Integer> src) {
        return src
                .validate(Validators::isPositive)
                .validate(Validators.hasDigitsInRange(8, 8))
                .validate(KouhiLogic::todoufukenIsValidInKouhiFutansha)
                .validate(Validators::hasValidCheckingDigit);
    }

    public static Logic<Integer> isValidKouhiJukyuushaBangou(Logic<Integer> src) {
        return src
                .validate(Validators::isPositive)
                .validate(Validators.hasDigitsInRange(7, 7))
                .validate(Validators::hasValidCheckingDigit);
    }

    public static void isValidKouhiDTOBody(KouhiDTO dto, String name, ErrorMessages em) {
        new LogicValue<>(dto.patientId)
                .validate(Validators.isNot(0))
                .verify(name, em);
        new LogicValue<>(dto.futansha)
                .validate(KouhiLogic::isValidKouhiFutanshaBangou)
                .verify(name, em);
        new LogicValue<>(dto.jukyuusha)
                .validate(KouhiLogic::isValidKouhiJukyuushaBangou)
                .verify(name, em);
        BiValidators.isValidIntervalSqldate(dto.validFrom, dto.validUpto,
                "公費負担者番号", "公費受給者番号", em);
    }

    public static void isValidKouhiDTOForEnter(KouhiDTO dto, String name, ErrorMessages em) {
        new LogicValue<>(dto.kouhiId)
                .validate(Validators.is(0))
                .verify(name, em);
        isValidKouhiDTOBody(dto, name, em);
    }

    public static void isValidKouhiDTOForUpdate(KouhiDTO dto, String name, ErrorMessages em) {
        new LogicValue<>(dto.kouhiId)
                .validate(Validators.isNot(0))
                .verify(name, em);
        isValidKouhiDTOBody(dto, name, em);
    }

}
