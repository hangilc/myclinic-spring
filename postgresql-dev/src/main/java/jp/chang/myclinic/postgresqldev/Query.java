package jp.chang.myclinic.postgresqldev;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;

public class Query {

    private static Logger logger = LoggerFactory.getLogger(Query.class);

    public static void main(String[] args) throws Exception{
        Class.forName("org.postgresql.Driver");
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost/myclinic",
                System.getenv("MYCLINIC_DB_USER"), System.getenv("MYCLINIC_DB_PASS"));
        {
            IyakuhinMasterGateway iyakuhinMasterGateway = new IyakuhinMasterGateway();
            //System.out.println(iyakuhinMasterGateway.searchIyakuhinByName(conn, "アムロジンＯＤ", LocalDate.now()));
            System.out.println(iyakuhinMasterGateway.findIyakuhinMaster(conn, 621991201, LocalDate.now().toString()));
        }
    }

}
