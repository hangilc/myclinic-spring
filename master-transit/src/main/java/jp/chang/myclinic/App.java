package jp.chang.myclinic;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication 
public class App implements CommandLineRunner {
    public static void main( String[] args ) throws IOException
    {
    	SpringApplication.run(App.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void run(String[] args) throws IOException {
        Menu menu = new MainMenu();
        while( menu != null ){
            menu = menu.exec();
        }

    	// ConsoleReader reader = new ConsoleReader();
    	// PrintWriter out = new PrintWriter(reader.getOutput());
    	// String line;
    	// line = reader.readLine("Search old iyakuhin by name: ");
    	// String[] texts = new String[]{ line };
    	// List<IyakuhinMaster> selections = searchIyakuhin(texts);
    	// while( selections.size() > 20 ){
    	// 	break;

    	// }
    	// for(int i=1;i<selections.size();i++){
    	// 	IyakuhinMaster m = selections.get(i-1);
    	// 	out.printf("%2d. %s\n", i, m.name);
    	// }
    	// reader.flush();
    }

    // private List<IyakuhinMaster> searchIyakuhin(String[] texts){
    // 	List<IyakuhinMaster> list = new ArrayList<>();
    // 	String text = "%" + String.join("%", texts) + "%";
    // 	jdbcTemplate.query("select * from iyakuhin_master_arch where " +
    // 		" name like ? and valid_upto = '0000-00-00'",
    // 		new Object[]{ text },
    // 		(row, rowNum) -> new IyakuhinMaster(
    // 			row.getInt("iyakuhincode"),
    // 			row.getString("valid_from"),
    // 			row.getString("name")
    // 		)
    // 	).forEach(master -> {
    // 		list.add(master);
    // 	});
    // 	return list;
    // }
}

// class IyakuhinMaster {
// 	public int iyakuhincode;
// 	public String validFrom;
// 	public String name;

// 	public IyakuhinMaster(){}

// 	public IyakuhinMaster(int iyakuhincode, String validFrom, String name){
// 		this.iyakuhincode = iyakuhincode;
// 		this.validFrom = validFrom;
// 		this.name = name;
// 	}
// }
