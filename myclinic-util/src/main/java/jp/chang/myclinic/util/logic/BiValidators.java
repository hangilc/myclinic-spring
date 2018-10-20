package jp.chang.myclinic.util.logic;

import java.time.LocalDate;

public class BiValidators {

    private BiValidators() {

    }

    public static void notBothAreEmpty(String left, String right, String leftName, String rightName,
                                       ErrorMessages em) {
        if (left.isEmpty() && right.isEmpty()) {
            if (leftName == null) {
                leftName = "前者";
            }
            if (rightName == null) {
                rightName = "後者";
            }
            em.add(String.format("%sと%sが両方とも空白です。", leftName, rightName));
        }
    }

    public static void isValidInterval(LocalDate validFrom, LocalDate validUpto,
                                       String leftName, String rightName, ErrorMessages em) {
        if (validUpto != null && validFrom.isAfter(validUpto)) {
            if (leftName == null) {
                leftName = "開始日";
            }
            if (rightName == null) {
                rightName = "終了日";
            }
            em.add(String.format("%sが%sより前の値です。", rightName, leftName));
        }
    }

    public static void isValidIntervalSqldate(String validFromSqldate, String validUptoSqldate,
                                              String validFromName, String validUptoName,
                                              ErrorMessages em) {
        new BiLogicValue<>(validFromSqldate, validUptoSqldate)
                .validate(Validators::isNotNull, Validators::valid)
                .convert(Converters::sqldateToLocalDate)
                .validate(BiValidators::isValidInterval)
                .verify(validFromName, validUptoName, em);
    }

}
