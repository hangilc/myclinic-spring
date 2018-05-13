package jp.chang.myclinic.rcpt.create;

public class BatchCreate {

    //private static Logger logger = LoggerFactory.getLogger(BatchCreate.class);

    private BatchCreate() {

    }

    private static void usage(){
        System.err.println("Usage: java -jar rcpt.jar create data-xml-file");
    }

    public static void run(String[] args){
        if( args.length != 2 ){
            usage();
            System.exit(1);
        }
        String srcXml = args[1];
        new Create(srcXml).run();
    }

}
