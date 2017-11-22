package jp.chang.myclinic.reportwaiting;


import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.UpdateItemOutcome;
import jp.chang.myclinic.dto.WqueueDTO;
import jp.chang.myclinic.dto.WqueueFullDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class AppReportWaiting implements CommandLineRunner {
    public static void main( String[] args )
    {
        new SpringApplicationBuilder(AppReportWaiting.class).web(false).run(args);
    }

    @Value("${homepage.dynamodb.table}")
    private String tableName;
    @Value("${homepage.dynamodb.region}")
    private String tableRegion;

    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Map<String, String> expressionAttributeNames = new HashMap<String, String>();
    {
        expressionAttributeNames.put("#C", "value");
        expressionAttributeNames.put("#D", "issueDate");
        expressionAttributeNames.put("#T", "issueTime");
        expressionAttributeNames.put("#L", "visitIds");
    }


    @Override
    public void run(String... strings) throws Exception {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(Regions.fromName(tableRegion))
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);
        Table table = dynamoDB.getTable(tableName);
        while(true){
            try {
                List<WqueueFullDTO> wqueue = getWqueue();
                LocalDateTime now = LocalDateTime.now();
                String date = dateFormatter.format(now);
                String time = timeFormatter.format(now);
                List<Integer> visitIds = wqueue.stream().map(w -> w.visit.visitId).collect(Collectors.toList());
                updateDynamoDB(table, wqueue.size(), date, time, visitIds);
                System.out.printf("reportwaiting %d %s %s\n", wqueue.size(), date, time);
            } catch(Exception ex){
                ex.printStackTrace();
            }
            Thread.sleep(1 * 60 * 1000);
        }
    }

    private void updateDynamoDB(Table table, int waitingCount, String date, String time, List<Integer> visitIds){
        Map<String, Object> expressiontAttributeValues = new HashMap<String, Object>();
        expressiontAttributeValues.put(":c", waitingCount);
        expressiontAttributeValues.put(":d", date);
        expressiontAttributeValues.put(":t", time);
        expressiontAttributeValues.put(":l", visitIds);
        table.updateItem("name", "waiting-count", "set #C = :c, #D = :d, #T = :t, #L = :l",
                expressionAttributeNames, expressiontAttributeValues);
    }

    private List<WqueueFullDTO> getWqueue(){
        RestTemplate restTemplate = new RestTemplate();
        WqueueFullDTO[] arr = restTemplate.getForObject("http://localhost:18080/json/list-wqueue-full-in-waiting-exam", WqueueFullDTO[].class);
        return Arrays.asList(arr);
    }
}
