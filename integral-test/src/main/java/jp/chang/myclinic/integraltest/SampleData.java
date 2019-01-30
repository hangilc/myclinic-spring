package jp.chang.myclinic.integraltest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class SampleData {

    //private static Logger logger = LoggerFactory.getLogger(SampleData.class);
    List<String> firstNames;
    Random random = new Random();

    public SampleData(){
        this.firstNames = loadFirstNames();
    }

    private List<String> loadFirstNames(){
        List<String> lines = new ArrayList<>();
        try {
            InputStream ins = getClass().getResourceAsStream("/first-names.txt");
            if( ins == null ){
                throw new RuntimeException("Cannot read first-names.txt");
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(ins, StandardCharsets.UTF_8));
            reader.lines().forEach(lines::add);
            reader.close();
            return lines;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String pickFirstName(){
        int i = random.nextInt(firstNames.size());
        return firstNames.get(i);
    }

}
