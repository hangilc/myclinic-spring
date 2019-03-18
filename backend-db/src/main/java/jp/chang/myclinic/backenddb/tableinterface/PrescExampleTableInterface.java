package jp.chang.myclinic.backenddb.tableinterface;

import jp.chang.myclinic.backenddb.TableInterface;
import jp.chang.myclinic.dto.PrescExampleDTO;

public interface PrescExampleTableInterface extends TableInterface<PrescExampleDTO> {

  String prescExampleId();

  String iyakuhincode();

  String masterValidFrom();

  String amount();

  String usage();

  String days();

  String category();

  String comment();
}
