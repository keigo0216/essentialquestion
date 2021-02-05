package com.Essential.properties;

import java.util.ArrayList;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Component
@PropertySource("classpath:questionChapter6.properties")
@ConfigurationProperties
@Data
public class QuestionChapter6Config {
	//問題を変数として登録
	
	private String q1;
	private String q2;
	private String q3;
	private int count;
	
	
	
	
	
	
}
