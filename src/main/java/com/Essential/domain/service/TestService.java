package com.Essential.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.Essential.domain.repository.TestDao;

@Service
public class TestService {

	@Autowired
	TestDao dao;
	
	//ユーザーのテストテーブル作成メソッド
	public void createTesttable(String userId) throws DataAccessException{
		dao.createTesttable(userId);
	}
	
	//問題がテストテーブルに登録してあるか、登録してあった場合result(正解しているかどうか)がtrueかfalseかを確認
	//登録されていなかった場合EmptyDataAccessExceptionを投げる
	public boolean isResult(String userId,int chapter,int number) throws DataAccessException {
		
		//chapter番号と問題numberを５桁のString型に変換
		String questionNumber = toStr(chapter,number);
		
		return dao.isResult(userId, questionNumber);
	}
	
	//testTableに新しく解く問題文を、resultをfalseにして登録
	public boolean insertQuestion(String userId,int chapter,int number) throws DataAccessException {
		
		//chapter番号と問題numberを5桁のString型に変換
		String questionNumber = toStr(chapter,number);
		
		int rowNumber = dao.insertQuestion(userId, questionNumber);
		
		//判定用変数
		boolean success = false;
		
		if(rowNumber > 0) {
			success = true;
		}
		
		return success;	
	}
	
	/**ユーザーのテスト問題テーブルに問題が登録されているかを確認
	 * 登録されていたらresultを返却
	 * 登録されていなかったら問題を登録し、result(false)を返却
	 * insertに失敗した場合、IllegalArgumentExceptionを投げる
	**/
	public boolean checkQuestion(String userId,int chapter,int number) throws DataAccessException,IllegalArgumentException {
		//結果返却用変数
		boolean result;
		try{
			result = isResult(userId, chapter, number);
		} catch(EmptyResultDataAccessException e) {
			boolean insert = insertQuestion(userId, chapter, number);
			if (insert == false) {
				throw new IllegalArgumentException("問題のinsertに失敗しました");
			}
			result = false;
		}
		return result;
	}
	
	//resultをtrueに更新
	public void toTrue(String userId,int chapter,int number) throws DataAccessException {
		//chapter番号と問題numberを５桁のString型に変換
		String questionNumber = toStr(chapter, number);
		
		//result
		dao.toTrue(userId, questionNumber);		
	}
	
	//全てのresultをfalseに更新
	public void toAllFalse(String userId, int chapter, int questionCount) {
		
		//問題数(questionCount)の数だけループ
		for(int i=1; i<=questionCount; i++) {
			//問題番号(i)のresultをfalseに更新
			String questionNumber = toStr(chapter,i);
			dao.toFalse(userId, questionNumber);
			
		}
	}
	//指定された問題のresultを返却
	public boolean getResult(String userId, int chapter, int number) {
		//chapter番号と問題numberを５桁のString型に変換
		String questionNumber = toStr(chapter,number);
		//resultを返却
		return dao.isResult(userId, questionNumber);
	}
	
	//chapter番号と問題のnumberをテストテーブル用にString型の５桁にまとめる
	private String toStr(int chapter,int question) {
		//結果用変数
		String questionNumber = "00000";
		
		//chapterをString型の２桁で表す
		String strChapter = "00";
		if((chapter>0) && (chapter<10)) {
			strChapter = "0" + chapter;
		} else if((chapter>=10) && (chapter<100)) {
			strChapter = Integer.toString(chapter);
		} else {
			throw new IllegalArgumentException("chapter番号が不適切です");
		}
		
		//numberをString型の3桁で表す
		String strNumber = "000";
		if((question>0) && (question<10)) {
			strNumber = "00" + question;
		} else if((question>=10) && (question<100)) {
			strNumber = "0" + question;
		} else if((question>=100) && (question<1000)) {
			strNumber = Integer.toString(question);
		} else {
			throw new IllegalArgumentException("問題numberが不適切です");
		}
		
		//結果
		questionNumber = strChapter + strNumber;
		
		return questionNumber;
	}
}
