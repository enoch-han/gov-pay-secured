package com.company.govpaysecured.cucumber;

import com.company.govpaysecured.GovpaysecuredApp;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

@CucumberContextConfiguration
@SpringBootTest(classes = GovpaysecuredApp.class)
@WebAppConfiguration
public class CucumberTestContextConfiguration {}
