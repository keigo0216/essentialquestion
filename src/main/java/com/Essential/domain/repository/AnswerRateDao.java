package com.Essential.domain.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AnswerRateDao {
	@Autowired
	JdbcTemplate jdbc;

	//AnswerRateテーブルを作成
	public void createAnswerRate(String userId) {
		
		String sql = "CREATE TABLE IF NOT EXISTS "
				+ userId + "_answerRate("
				+ " chapter INTEGER PRIMARY KEY,"
				+ " answer INTEGER NOT NULL)";
		jdbc.execute(sql);
	}
	
	//chapter番号をanswerは0の状態でinsert
	public void insertChapter(String userId) {
		//chapterの数
		final int chapter = 20;
		
		for(int i=0; i<=chapter; i++) {
			String sql = "INSERT INTO "  + userId + "_answerRate("
					+ "chapter,"
					+ " answer)"
					+ "VALUES(" + i + ",0)";
			jdbc.execute(sql);
		}
	}
	
	//answerを取得
	public int getAnswer(String userId,int chapter) {
		
		String sql = "SELECT answer FROM " + userId + "_answerRate"
				+ " WHERE chapter = " + chapter;
		
		Map<String,Object> map = jdbc.queryForMap(sql);
		
		return (int)map.get("answer");
	}
	
	//answerを更新
	public void updateAnswer(String userId,int chapter,int number) {
		
		String sql = "UPDATE " + userId + "_answerRate"
				+ " SET answer = " + number
				+ " WHERE chapter = " + chapter;
		
		jdbc.execute(sql);
	}
 }



















