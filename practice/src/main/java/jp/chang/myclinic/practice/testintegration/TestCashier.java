package jp.chang.myclinic.practice.testintegration;

import jp.chang.myclinic.practice.javafx.CashierDialog;

class TestCashier extends IntegrationTestBase {

    private Exam exam;

    TestCashier(Exam exam) {
        this.exam = exam;
    }

    void finishCashier() {
        getMainPane().simulateClickCashierButton();
        CashierDialog dialog = waitForWindow(CashierDialog.class);
        dialog.simulateClickEnterButton();
        waitForWindowDisappear(dialog);
    }

}
