package com.Essential.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.Essential.domain.model.AnswerForm;
import com.Essential.domain.service.QuestionService;

@Controller
public class QuestionController {
	
	@Autowired
	private QuestionService questionService;
	
	//リクエストパラメーターで章番号(chapter)と問題番号(number)を受け取る
	@GetMapping("/question/{chapter}/{number}")
	public String getQuestion(
			Model model,
			@PathVariable("chapter") int chapter,
			@PathVariable("number") int number
			) {
		
		model.addAttribute("contents","question::question_contents"); 
		String question="";

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
	
	@PostMapping("/question/{chapter}/{number}")
	public String getAnswer(Model model,
			@ModelAttribute AnswerForm answerForm,
			@PathVariable("chapter") int chapter,
			@PathVariable("number") int number
			) {
			
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
			 * 問題が登録されたかどうかを変数endとして表し、Modelに登録
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
			
			//modelにth:fragmentのパスを追加
			model.addAttribute("contents","answer::answer_contents"); 

			//問題文格納変数
			String question = "";
			Boolean[] checkList;
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
				//エラーを避けるために空のanswerListを作成
				answerList = new ArrayList<String>();
			}
			
			
			
			//解答フォームと解答の正誤判定
			try {
				checkList = questionService.checkAnswer(answerForm, answerList);
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
	
			return "homeLayout";
	}
	
	@GetMapping("/end")
	public String end(Model model) {
		model.addAttribute("contents", "end::end_contents");
		return "homeLayout";
	}
}
