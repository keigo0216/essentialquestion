package com.Essential.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.Essential.domain.repository.AnswerRateDao;

@Service
public class AnswerRateService {

	@Autowired
	AnswerRateDao dao;
	
	//chapter番号が追加されたテーブルを作成
	public void createAnswerRateTable(String userId) throws DataAccessException {
		//テーブルを作成
		dao.createAnswerRate(userId);
			
		//chapter番号をそれぞれの正解数を0でinsert
		dao.insertChapter(userId);
	}
	
	//解答数を取得
	public int getAnswer(String userId,int chapter) {
		return dao.getAnswer(userId, chapter);
	}
	
	//answerを0にリセット
	public void resetAnswer(String userId, int chapter) {
		dao.updateAnswer(userId, chapter, 0);
	}
	
	//正解した場合にanswerを+1
	public void plusAnswer(String userId, int chapter) {
		
		//answerを取得
		int answer = dao.getAnswer(userId, chapter);

		//answerを+1して更新
		dao.updateAnswer(userId, chapter, answer+1);
	}
}
