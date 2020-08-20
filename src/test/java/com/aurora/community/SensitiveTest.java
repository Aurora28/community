package com.aurora.community;


import com.aurora.community.util.SensitiveFilter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {
    @Autowired
    private SensitiveFilter sensitiveFilter;
    
    @Test
    public void testSensitiveFilter() {
        String text = "吸||毒是不对的，有害社会，赌**博会让人上瘾";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }
}