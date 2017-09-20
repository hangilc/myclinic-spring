package jp.chang.myclinic.rest;

import jp.chang.myclinic.db.myclinic.DbGateway;
import jp.chang.myclinic.dto.PaymentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/json")
@Transactional
public class CashierController {

    @Autowired
    private DbGateway dbGateway;

    @RequestMapping(value="/finish-cashier", method= RequestMethod.POST)
    public boolean finishCashier(@RequestBody PaymentDTO payment){
        dbGateway.finishCashier(payment);
        return true;
    }

}
