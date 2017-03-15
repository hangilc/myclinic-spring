package jp.chang.myclinic.mastermap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by hangil on 2017/03/04.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MasterMapTest {
    @Autowired
    private ResourceLoader resourceLoader;

    @Test
    public void testFindCodeMapResource() {
        Resource resource = resourceLoader.getResource("classpath:master-map.txt");
        assertTrue("find resource", resource.exists());
    }

    @Test
    public void testLoadCodeMapResouce() throws IOException {
        MasterMap map = new MasterMap();
        Resource resource = resourceLoader.getResource("classpath:master-map.txt");
        map.loadCodeMapResource(resource);
        // Y,611140694,2012-04-01,620098801 ロキソニン錠６０ｍｇ
        assertEquals("resolve loxonin", 620098801, map.resolveIyakuhinCode(611140694, LocalDate.of(2014, 4, 1)));
        // S,112009210,2010-04-01,112007410 再診
        assertEquals("resolve saishin", 112007410, map.resolveShinryouCode(112009210, LocalDate.of(2014, 4, 1)));
        // S,111012370,2010-04-01,000000000 電子化加算（廃止）
        assertEquals("resolve denka", 0, map.resolveShinryouCode(111012370, LocalDate.of(2014, 4, 1)));
    }

    @Test
    public void testFindNameMapResource(){
        Resource resource = resourceLoader.getResource("classpath:name-map.txt");
        assertTrue("find resource", resource.exists());
    }

    @Test
    public void testLoadNameMapResource() throws IOException {
        MasterMap map = new MasterMap();
        Resource resource = resourceLoader.getResource("classpath:name-map.txt");
        map.loadNameMapResource(resource);
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
