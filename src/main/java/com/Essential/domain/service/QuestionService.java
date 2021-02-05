package com.Essential.domain.service;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Essential.domain.model.AnswerForm;
import com.Essential.properties.QuestionChapter6Config;

@Service
public class QuestionService {

	@Autowired
	private QuestionChapter6Config questionChapter6;
	
	//問題検索
	public String questionSearch(int chapter,int QNumber) throws ReflectiveOperationException {
		String question = null;
		
		//リフレクションによって各問題プロパティコンフィグクラスからgetメソッドを呼び出し
		String strClassName = "com.Essential.properties.QuestionChapter" + chapter + "Config";
		String strMethodName = "getQ" + QNumber;
		Class cl = Class.forName(strClassName);
		Method method = cl.getMethod(strMethodName);
		//chapter番号によって問題プロパティコンフィグを変更
		
		if(chapter == 6) {
			question = (String)(method.invoke(questionChapter6));			
		}
		//エラー処理
		if(question == null) {
			throw new IllegalArgumentException("メソッド中のquestionがnullです。");
		}else if(question == "") {
			throw new IllegalArgumentException("メソッド中のquestionが空です。");
		}
		return question;
	}
	
	//問題プロパティに登録されている問題数を獲得
	public int chapterCount(int chapter) throws ReflectiveOperationException,IllegalArgumentException {
		Integer count = null;
		
		String strClassName = "com.Essential.properties.QuestionChapter" +chapter + "Config";
		String strMethodName ="getCount";
		Class cl = Class.forName(strClassName);
		Method method = cl.getMethod(strMethodName);
		//chapter番号によって問題プロパティコンフィグを変更
		
		if(chapter == 6) {
			count = (int)(method.invoke(questionChapter6));
		}
		//エラー処理
		if(count == null) {
			throw new IllegalArgumentException("メソッド中のcountがnullです");
		}else if(count == 0) {
			throw new IllegalArgumentException("メソッド中のcountが0です。");
		}
		return count;
	}
	
	//問題文の()を,に変更
	public String[] questionWords(String question) {
		return question.split("\\(.*?\\)");
	}
	
	
	//問題文から()の中身をanswerリストとして取り出す
	public ArrayList<String> answerList(String question) throws IllegalArgumentException {
		ArrayList<String> answerList = new ArrayList<>();
		String answer = null;
		// questionの()の数だけループ
		while(true){
			int beginIndex = question.indexOf("(");
			int endIndex = question.indexOf(")");
			
			if(beginIndex != -1 &&  endIndex != -1) {
				answer = question.substring(beginIndex+1, endIndex);
				answerList.add(answer);
				//questionを始めの（　　）を取り出した形にする
				question = question.replaceFirst("\\(.*?\\)","") ;
			} else {
				break;
			}
		}
		if(answerList.size()==0) {
			throw new IllegalArgumentException("questionの答えが不適切です。");
		}
		return answerList;
	}
	
	//answerFormで送られてきた解答一覧の正誤判定
	public Boolean[] checkAnswer(AnswerForm answerForm, ArrayList<String> rightAnswerList) throws IllegalArgumentException {
		ArrayList<String> answerFormList = answerForm.getAnswerList();
		int formSize = answerFormList.size();
		int rightSize = rightAnswerList.size();
		
		if(formSize == 0 || rightSize == 0) {
			throw new IllegalArgumentException("解答数が０です。");
		}
		if(formSize != rightSize) {
			throw new IllegalArgumentException("解答数がフォームとプロパティで異なります。");
		}
		
		//各answerの正誤判定してcheckList[]として保存
		Boolean[] checkList = new Boolean[rightSize];
		for(int i=0; i <= rightSize-1; i++) {
			checkList[i] = answerFormList.get(i).equals(rightAnswerList.get(i));
		}
		return checkList;
 	}
}
