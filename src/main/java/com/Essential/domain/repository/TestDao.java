package com.Essential.domain.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class TestDao {
	@Autowired
	JdbcTemplate jdbc;
	
	//ユーザーのテスト問題テーブルを作成
	public void createTesttable(String userId)  {
		String sql = "CREATE TABLE IF NOT EXISTS "
				+ userId + "_test("
				+ " number CHAR(5) PRIMARY KEY,"
				+ " result BOOLEAN NOT NULL);";
		jdbc.execute(sql);
	}
	
	//テストテーブルに問題をinsert
	public int insertQuestion(String userId,String questionNumber) {
		String sql = "INSERT INTO " + userId + "_test("
				+ " number,"
				+ " result)"
				+ "VALUES(?,false)";
		
		int rowNumber = jdbc.update(sql, questionNumber);
		
		return rowNumber;
	}

	//テストテーブルの問題を検索してresultを返却
	public boolean isResult(String userId,String questionNumber)  {
		
		String sql = "SELECT result FROM " + userId + "_test"
				+ " WHERE number = ?";

		//result取得
		Map<String, Object> map = jdbc.queryForMap(sql, questionNumber);
		
		return (boolean)map.get("result");
	}
	
	//正解した場合にresultをtrueに更新
	public void toTrue(String userId, String questionNumber)  {
		
		String sql = "UPDATE " + userId + "_test"
				+ " SET result = true"
				+ " WHERE number = '" + questionNumber + "'";
		
		//result更新
		jdbc.execute(sql);		
	}
	
	//resultをfalseに更新
	public void toFalse(String userId, String questionNumber) {
		String sql = "UPDATE " + userId + "_test"
				+ " SET result = false"
				+ " WHERE number = '" + questionNumber + "'";
		
		//result更新
		jdbc.execute(sql);
	}
}
