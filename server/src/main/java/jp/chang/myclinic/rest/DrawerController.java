package jp.chang.myclinic.rest;

import jp.chang.myclinic.drawer.*;
import jp.chang.myclinic.drawer.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/json")
@Transactional
public class DrawerController {

    @RequestMapping(value="/get-receipt-drawer-ops", method= RequestMethod.GET)
    public List<Op> getReceiptDrawerOps(@RequestParam("visit-id") int visitId){
        List<Op> ops = new ArrayList<>();
        ops.add(new OpMoveTo(10, 20));
        ops.add(new OpLineTo(20, 40));
        ops.add(new OpCreateFont("regular", "MS Mincho", 12, 0, false));
        ops.add(new OpSetFont("regular"));
        ops.add(new OpDrawChars("aaaaa",
                Arrays.asList(new Double(10.0), new Double(12.0), new Double(16.0), new Double(18.0), new Double(20.0)),
                Arrays.asList(new Double(30), new Double(32), new Double(34), new Double(36), new Double(38))));
        ops.add(new OpSetTextColor(255, 0, 12));
        ops.add(new OpDrawChars("hello", Arrays.asList(new Double(20.0)), Arrays.asList(new Double(30.0))));
        ops.add(new OpMoveTo(20, 10));
        ops.add(new OpLineTo(40, 20));
        ops.add(new OpCreatePen("green", 12, 255, 0, 0.1));
        ops.add(new OpSetPen("green"));
        ops.add(new OpMoveTo(120 - 60, 10));
        ops.add(new OpLineTo(140 - 60, 20));
        return ops;
    }

}