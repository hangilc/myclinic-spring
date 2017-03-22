package jp.chang.myclinic.mastermap;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

/**
 * Created by hangil on 2017/03/04.
 */
public class MasterMapTest {

    private BufferedReader openCodeMapFile() throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("master-map.txt");
        InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        return new BufferedReader(reader);
    }

    private BufferedReader openNameMapFile() throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream("name-map.txt");
        InputStreamReader reader = new InputStreamReader(in, StandardCharsets.UTF_8);
        return new BufferedReader(reader);
    }

    @Test
    public void testFindCodeMapResource() throws IOException {
        try( InputStream stream = getClass().getClassLoader().getResourceAsStream("master-map.txt") ){
            assertNotNull("find resource", stream);
        }
    }

    @Test
    public void testLoadCodeMapResouce() throws IOException {
        try( BufferedReader reader = openCodeMapFile(); Stream<String> lines = reader.lines() ){
            MasterMap map = new MasterMap();
            map.loadCodeMap(lines);
            // Y,611140694,2012-04-01,620098801 ロキソニン錠６０ｍｇ
            assertEquals("resolve loxonin", 620098801, map.resolveIyakuhinCode(611140694, LocalDate.of(2014, 4, 1)));
            // S,112009210,2010-04-01,112007410 再診
            assertEquals("resolve saishin", 112007410, map.resolveShinryouCode(112009210, LocalDate.of(2014, 4, 1)));
            // S,111012370,2010-04-01,000000000 電子化加算（廃止）
            assertEquals("resolve denka", 0, map.resolveShinryouCode(111012370, LocalDate.of(2014, 4, 1)));
        }
    }

    @Test
    public void testFindNameMapResource() throws IOException {
        try( InputStream stream = getClass().getClassLoader().getResourceAsStream("name-map.txt") ){
            assertNotNull("find resource", stream);
        }
    }

    @Test
    public void testLoadNameMapResource() throws IOException {
        try( BufferedReader reader = openNameMapFile(); Stream<String> lines = reader.lines() ){
            MasterMap map = new MasterMap();
            map.loadNameMap(lines);
            // s,単純撮影,170001910
            assertEquals("name map shinryou", Optional.of(170001910), map.getShinryoucodeByName("単純撮影"));
            assertEquals("name map empty", Optional.empty(), map.getShinryoucodeByName("単純撮影XX"));
            // k,大角,700030000
            assertEquals("name map kizai", Optional.of(700030000), map.getKizaicodeByName("大角"));
            // d,急性上気道炎,3041
            assertEquals("name map disease", Optional.of(3041), map.getShoubyoumeicodeByName("急性上気道炎"));
            // a,疑い,8002
            assertEquals("name map disease adj", Optional.of(8002), map.getShuushokugocodeByName("疑い"));
        }
    }
}
