package jp.chang.myclinic.reportwaiting;


import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class AppReportWaiting implements CommandLineRunner {
    public static void main( String[] args )
    {
        SpringApplication.run(AppReportWaiting.class, args);
    }

    @Value("${homepage.dynamodb.table}")
    private String tableName;
    @Value("${homepage.dynamodb.region}")
    private String tableRegion;

    @Override
    public void run(String... strings) throws Exception {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(Regions.fromName(tableRegion))
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable(tableName);
        Item item = table.getItem("name", "waiting-count");
        System.out.println(item);
        Map<String, String> expressionAttributeNames = new HashMap<String, String>();
        expressionAttributeNames.put("#C", "value");
        expressionAttributeNames.put("#D", "issueDate");
        expressionAttributeNames.put("#T", "issueTime");
        Map<String, Object> expressiontAttributeValues = new HashMap<String, Object>();
        expressiontAttributeValues.put(":c", 1);
        expressiontAttributeValues.put(":d", "2017-11-19");
        expressiontAttributeValues.put(":t", "08:28:12");
        UpdateItemOutcome outcome = table.updateItem("name", "waiting-count", "set #C = :c, #D = :d, #T = :t",
                expressionAttributeNames, expressiontAttributeValues);
        System.out.println(outcome);
    }
}
