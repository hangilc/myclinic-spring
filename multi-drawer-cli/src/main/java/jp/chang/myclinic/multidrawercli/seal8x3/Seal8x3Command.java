package jp.chang.myclinic.multidrawercli.seal8x3;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jp.chang.myclinic.drawer.Op;
import jp.chang.myclinic.multidrawer.seal8x3.Seal8x3Data;
import jp.chang.myclinic.multidrawer.seal8x3.Seal8x3Drawer;
import jp.chang.myclinic.multidrawercli.Command;

import java.util.List;
import java.util.stream.Stream;

public class Seal8x3Command implements Command {

    private void usage(){
        System.err.println("subcommand options:");
        System.err.println("  -r | --row-start ROW-INDEX)");
        System.err.println("  -c | --col-start COL-INDEX)");
    }

    private int rowStart = 1;
    private int colStart = 1;

    @Override
    public String getDescription() {
        return "prints 8 x 3 label seal (A4)";
    }

    @Override
    public void initialize(List<String> args) {
        for(int i=0;i<args.size();i++){
            String arg = args.get(i);
            switch(arg){
                case "-r":
                case "--row-start":{
                    this.rowStart = Integer.parseInt(args.get(i+1));
                    i += 1;
                    break;
                }
                case "-c":
                case "--col-start":{
                    this.colStart = Integer.parseInt(args.get(i+1));
                    i += 1;
                    break;
                }
                default: {
                    usage();
                    System.exit(1);
                    break;
                }
            }
        }
    }

    @Override
    public List<List<Op>> render(Stream<String> src) throws Exception {
        String json = getSourceAsString(src);
        ObjectMapper mapper = new ObjectMapper();
        Seal8x3Data data = mapper.readValue(json, new TypeReference<Seal8x3Data>() {});
        data.startRow = rowStart;
        data.startColumn = colStart;
        Seal8x3Drawer drawer = new Seal8x3Drawer();
        return drawer.draw(data);
    }
}
