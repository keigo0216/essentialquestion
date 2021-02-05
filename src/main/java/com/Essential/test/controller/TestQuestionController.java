package com.Essential.test.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.Essential.domain.model.AnswerForm;
import com.Essential.domain.service.AnswerRateService;
import com.Essential.domain.service.QuestionService;
import com.Essential.domain.service.TestService;

@Controller
public class TestQuestionController {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private TestService testService;
	
	@Autowired
	private AnswerRateService answerRateService;
	
	//リクエストパラメーターで章番号(chapter)と問題番号(number)を受け取る
	@GetMapping("test/question/{chapter}/{number}")
	public String getQuestion(
			Model model,
			@PathVariable("chapter") int chapter,
			@PathVariable("number") int number
			) {
		//userIdを取得
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = user.getUsername();
				
		model.addAttribute("contents","test/testQuestion::testQuestion_contents"); 
		
		//問題プロパティに登録された問題数を取得
		int questionCount = 0;
		try {
			questionCount = questionService.chapterCount(chapter);
		} catch(ReflectiveOperationException e) {
			e.printStackTrace();
		} catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		
		//正誤判定用変数
		boolean result;
		//ループ回数
		int count = 0;
		
		do {
			
			/**
			 * ユーザーのテストテーブルにアクセスして一度正解しているかをresultで取得
			 * testテーブルへのアクセスに問題が起きた場合chapter選択画面へ戻る
			**/
			try{
				result = testService.checkQuestion(userId, chapter, number);
			} catch(DataAccessException e) {
				return "redirect:/";
			} catch(IllegalArgumentException e) {
				return "redirect:/";
			}
			//正解していなかった場合、ループ１週目のときはそのままループを抜け出す
			if(result == false) {
				if(count == 0) {
					break;
				} else {
					//ループ二週目以降で正解していない問題番号になったとき、その問題番号のURLにリダイレクトする
					String url = "redirect:/test/question/" + chapter + "/" + number;
					return url;
				}
			} else if(result == true){
				//正解していた場合は問題numberを+1、ループ数を+1して次のループに移る
				//number+1がその章の問題数(questionCount)より大きくなった場合/endへリダイレクト
				if(number+1 > questionCount) {
					return "redirect:/end";
				}
				number++;
				count++;
			}
		} while(true);
		
		//問題文用変数
		String question="";
		
		//問題文を検索
		try{
			question = questionService.questionSearch(chapter,number);
		} catch(ReflectiveOperationException e) {
			e.printStackTrace();
		} catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		//問題文を()で区切ってquestionWordsとしてModelに追加
		String[] questionWords = questionService.questionWords(question);
		model.addAttribute("questionWords", questionWords);
		//問題数をanswerCounterとしてModelに追加
		int answerCounter = questionWords.length-1;
		model.addAttribute("answerCounter",answerCounter);
		//answerFormをModelに登録
		model.addAttribute(new AnswerForm());
		
		
		return "homeLayout";
	}
	
	@PostMapping("test/question/{chapter}/{number}")
	@Transactional
	public String getAnswer(Model model,
			@ModelAttribute AnswerForm answerForm,
			@PathVariable("chapter") int chapter,
			@PathVariable("number") int number
			) {
			//userIdを取得
			User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			String userId = user.getUsername();			
		
			//問題プロパティに登録された問題数を取得
			int questionCount = 0;
			try {
				questionCount = questionService.chapterCount(chapter);
			} catch(ReflectiveOperationException e) {
				e.printStackTrace();
			} catch(IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			/*
			 * 問題プロパティに登録された問題数(qustionCount)によって操作を変更
			 * 問題が最後かどうかを変数endとして表し、Modelに登録
			 * 問題数が０の場合ホームへリダイレクト,問題が最後になったときにendをtrue,それ以外のときはリンクを次の問題にする
			 */
			Boolean end = false;
			if(questionCount == 0) {
				System.out.println("問題が0です");
				return "redirect:/home";
			} else if(questionCount == number) {
				end = true;
			} else {
				model.addAttribute("nextNumber", number+1);
			}
			model.addAttribute("end",end);
			
			//問題文格納変数
			String question = "";
			//正誤リスト用変数
			Boolean[] checkList;
			//答えリスト用変数
			ArrayList<String> answerList;
			
			//問題文取得
			try{
				question = questionService.questionSearch(chapter, number);
			} catch(ReflectiveOperationException e) {
				e.printStackTrace();
			} catch(IllegalArgumentException e) {
				e.printStackTrace();
			}
			
			//解答リスト取得
			try{
				answerList = questionService.answerList(question);
			} catch(IllegalArgumentException e) {
				e.printStackTrace();
				answerList = new ArrayList<String>();
			}
			
			
			//解答フォームと解答の正誤判定
			try {
				checkList = questionService.checkAnswer(answerForm, answerList);
				//解答がすべて正解していた場合
				if(!Arrays.asList(checkList).contains(false)) {
					try{
						//解答フォームが再送信されたとき用に、テストテーブルのresultが既にtrueになっていないか確認
						if(testService.getResult(userId, chapter, number) == false) {
							//テストテーブルに保存されているresultをtrueにする
							testService.toTrue(userId, chapter, number);
							
							//章の問題正解数を+1
							answerRateService.plusAnswer(userId, chapter);
							
							//問題が全部正解したとき、テストテーブル(user_test)のそのchapterのresultをすべてfalseに、解答数テーブル(user_answerRate)のanswerを0にリセット
							if(questionCount == answerRateService.getAnswer(userId, chapter)) {
								testService.toAllFalse(userId, chapter, questionCount);
								answerRateService.resetAnswer(userId, chapter);
								//th:fragmentのtestFinishをmodelに追加してhomeLayooutを返却
								model.addAttribute("contents", "test/testFinish::testFinish_contents");
								return "homeLayout";
							}					
						}
					}catch(DataAccessException e) {
						//上のDB操作に失敗した場合チャプター画面へ戻る
						e.printStackTrace();
						return "redirect:/";
					}
				}
			} catch(IllegalArgumentException e) {
				//checkListの取得に失敗した場合、checkListをnullにして渡す
				e.printStackTrace();
				checkList = null;
			}
			
			//問題文をquestionとしてModelに追加
			model.addAttribute("question", question);
			
			//問題数をanswerCounterとしてModelに追加
			String[] questionWords = questionService.questionWords(question);
			int answerCounter = questionWords.length-1;
			model.addAttribute("answerCounter",answerCounter);
			
			//答えをanswerListとしてModelに追加
			model.addAttribute("answerList", answerList);
			//正誤判定をcheckListとしてModelに追加
			model.addAttribute("checkList", checkList);
		
			//modelにth:fragmentのパスを追加
			model.addAttribute("contents","test/testAnswer::testAnswer_contents"); 

			return "homeLayout";
	}
}
