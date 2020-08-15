package com.aurora.community;

import com.aurora.community.util.MailClient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTest {
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;

    @Test 
    public void testTextMail() {
        mailClient.sendMail("xiangbow@usc.edu", "Test Try", "welcome!");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "aurora");

        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        mailClient.sendMail("xiangbow@usc.edu", "html Test", content);
    }
}