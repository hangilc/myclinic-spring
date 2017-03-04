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
    public void testFindResource() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:master-map.txt");
        assertTrue("find resource", resource.exists());
    }

    @Test
    public void testLoadResouce() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:master-map.txt");
        MasterMap map = MasterMap.createFromResource(resource);
        // Y,611140694,2012-04-01,620098801 ロキソニン錠６０ｍｇ
        assertEquals("resolve loxonin", 620098801, map.resolveIyakuhinCode(611140694, LocalDate.of(2014, 4, 1)));
        // S,112009210,2010-04-01,112007410 再診
        assertEquals("resolve saishin", 112007410, map.resolveShinryouCode(112009210, LocalDate.of(2014, 4, 1)));
        // S,111012370,2010-04-01,000000000 電子化加算（廃止）
        assertEquals("resolve denka", 0, map.resolveShinryouCode(111012370, LocalDate.of(2014, 4, 1)));
    }
}
