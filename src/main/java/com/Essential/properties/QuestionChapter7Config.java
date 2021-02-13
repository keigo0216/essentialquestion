package com.Essential.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource("classpath:questionProperties/questionChapter7.properties")
@ConfigurationProperties(prefix = "7")
@Data
public class QuestionChapter7Config {
	//問題数
	private int count;
	//問題を変数として登録
	private String q1;
	private String q2;
	private String q3;
	private String q4;
	private String q5;
	private String q6;
	private String q7;
	private String q8;
	private String q9;
	private String q10;
	private String q11;
	private String q12;
	private String q13;
	private String q14;
	private String q15;
	private String q16;
	private String q17;
	private String q18;
	private String q19;
	private String q20;
	private String q21;
	private String q22;
	private String q23;
	private String q24;
	private String q25;
	private String q26;
	private String q27;
	private String q28;
	private String q29;
	private String q30;
	
}
