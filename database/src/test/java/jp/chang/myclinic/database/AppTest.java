package jp.chang.myclinic.database;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    public void exampleTest(){
        Patient patient = patientRepository.findOne(199);
        System.out.println(patient);
        assertThat(true);
    }
}
