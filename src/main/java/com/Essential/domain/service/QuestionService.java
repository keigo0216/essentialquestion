package com.Essential.domain.service;

import java.lang.reflect.Method;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Essential.domain.model.AnswerForm;
import com.Essential.properties.QuestionChapter10Config;
import com.Essential.properties.QuestionChapter11Config;
import com.Essential.properties.QuestionChapter12Config;
import com.Essential.properties.QuestionChapter13Config;
import com.Essential.properties.QuestionChapter14Config;
import com.Essential.properties.QuestionChapter15Config;
import com.Essential.properties.QuestionChapter16Config;
import com.Essential.properties.QuestionChapter17Config;
import com.Essential.properties.QuestionChapter18Config;
import com.Essential.properties.QuestionChapter19Config;
import com.Essential.properties.QuestionChapter1Config;
import com.Essential.properties.QuestionChapter20Config;
import com.Essential.properties.QuestionChapter2Config;
import com.Essential.properties.QuestionChapter3Config;
import com.Essential.properties.QuestionChapter4Config;
import com.Essential.properties.QuestionChapter5Config;
import com.Essential.properties.QuestionChapter6Config;
import com.Essential.properties.QuestionChapter7Config;
import com.Essential.properties.QuestionChapter8Config;
import com.Essential.properties.QuestionChapter9Config;

@Service
public class QuestionService {

	@Autowired
	private QuestionChapter1Config questionChapter1;
	@Autowired
	private QuestionChapter2Config questionChapter2;
	@Autowired
	private QuestionChapter3Config questionChapter3;
	@Autowired
	private QuestionChapter4Config questionChapter4;
	@Autowired
	private QuestionChapter5Config questionChapter5;
	@Autowired
	private QuestionChapter6Config questionChapter6;
	@Autowired
	private QuestionChapter7Config questionChapter7;
	@Autowired
	private QuestionChapter8Config questionChapter8;
	@Autowired
	private QuestionChapter9Config questionChapter9;
	@Autowired
	private QuestionChapter10Config questionChapter10;
	@Autowired
	private QuestionChapter11Config questionChapter11;
	@Autowired
	private QuestionChapter12Config questionChapter12;
	@Autowired
	private QuestionChapter13Config questionChapter13;
	@Autowired
	private QuestionChapter14Config questionChapter14;
	@Autowired
	private QuestionChapter15Config questionChapter15;
	@Autowired
	private QuestionChapter16Config questionChapter16;
	@Autowired
	private QuestionChapter17Config questionChapter17;
	@Autowired
	private QuestionChapter18Config questionChapter18;
	@Autowired
	private QuestionChapter19Config questionChapter19;
	@Autowired
	private QuestionChapter20Config questionChapter20;
	
	//問題検索
	public String questionSearch(int chapter,int QNumber) throws ReflectiveOperationException {
		String question = null;
		
		//リフレクションによって各問題プロパティコンフィグクラスからgetメソッドを呼び出し
		String strClassName = "com.Essential.properties.QuestionChapter" + chapter + "Config";
		String strMethodName = "getQ" + QNumber;
		Class cl = Class.forName(strClassName);
		Method method = cl.getMethod(strMethodName);
		//chapter番号によって問題プロパティコンフィグを変更
		
		switch(chapter) {
		case 1:
			question = (String)(method.invoke(questionChapter1));			
			break;
		case 2:
			question = (String)(method.invoke(questionChapter2));			
			break;
		case 3:
			question = (String)(method.invoke(questionChapter3));			
			break;
		case 4:
			question = (String)(method.invoke(questionChapter4));			
			break;
		case 5:
			question = (String)(method.invoke(questionChapter5));			
			break;
		case 6:
			question = (String)(method.invoke(questionChapter6));	
			break;
		case 7:
			question = (String)(method.invoke(questionChapter7));			
			break;
		case 8:
			question = (String)(method.invoke(questionChapter8));			
			break;
		case 9:
			question = (String)(method.invoke(questionChapter9));			
			break;
		case 10:
			question = (String)(method.invoke(questionChapter10));			
			break;
		case 11:
			question = (String)(method.invoke(questionChapter11));			
			break;
		case 12:
			question = (String)(method.invoke(questionChapter12));			
			break;
		case 13:
			question = (String)(method.invoke(questionChapter13));			
			break;
		case 14:
			question = (String)(method.invoke(questionChapter14));			
			break;
		case 15:
			question = (String)(method.invoke(questionChapter15));			
			break;
		case 16:
			question = (String)(method.invoke(questionChapter16));			
			break;
		case 17:
			question = (String)(method.invoke(questionChapter17));			
			break;
		case 18:
			question = (String)(method.invoke(questionChapter18));			
			break;
		case 19:
			question = (String)(method.invoke(questionChapter19));			
			break;
		case 20:
			question = (String)(method.invoke(questionChapter20));			
			break;
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
		
		switch(chapter) {
		case 1:
			count = (int)(method.invoke(questionChapter1));		
			break;
		case 2:
			count = (int)(method.invoke(questionChapter2));			
			break;
		case 3:
			count = (int)(method.invoke(questionChapter3));			
			break;
		case 4:
			count = (int)(method.invoke(questionChapter4));			
			break;
		case 5:
			count = (int)(method.invoke(questionChapter5));			
			break;
		case 6:
			
			count = (int)(method.invoke(questionChapter6));
			break;
		case 7:
			count = (int)(method.invoke(questionChapter7));			
			break;
		case 8:
			count = (int)(method.invoke(questionChapter8));			
			break;
		case 9:
			count = (int)(method.invoke(questionChapter9));			
			break;
		case 10:
			count = (int)(method.invoke(questionChapter10));			
			break;
		case 11:
			count = (int)(method.invoke(questionChapter11));			
			break;
		case 12:
			count = (int)(method.invoke(questionChapter12));			
			break;
		case 13:
			count = (int)(method.invoke(questionChapter13));			
			break;
		case 14:
			count = (int)(method.invoke(questionChapter14));			
			break;
		case 15:
			count = (int)(method.invoke(questionChapter15));			
			break;
		case 16:
			count = (int)(method.invoke(questionChapter16));			
			break;
		case 17:
			count = (int)(method.invoke(questionChapter17));			
			break;
		case 18:
			count = (int)(method.invoke(questionChapter18));			
			break;
		case 19:
			count = (int)(method.invoke(questionChapter19));			
			break;
		case 20:
			count = (int)(method.invoke(questionChapter20));			
			break;
		}
		//エラー処理
		if(count == null) {
			throw new IllegalArgumentException("メソッド中のcountがnullです");
		}else if(count == 0) {
			System.out.println("メソッド中のcountが0です。");
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
