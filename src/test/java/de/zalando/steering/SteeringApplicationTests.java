package de.zalando.steering;

import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties(SteeringProperties.class)
public class SteeringApplicationTests {

    @Test
    public void contextLoads() { }

}
