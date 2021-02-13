package com.Essential.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource("classpath:questionProperties/questionChapter13.properties")
@ConfigurationProperties(prefix = "13")
@Data
public class QuestionChapter13Config {
	//問題数
	private int count;
	//問題を変数として登録
}
