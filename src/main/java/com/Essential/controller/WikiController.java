package com.Essential.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.Essential.domain.service.QuestionService;
import com.Essential.wiki.WikiService;

@Controller
public class WikiController {
	
	@Autowired
	private QuestionService questionService;
		
	@Autowired
	private WikiService wikiService;	
	
	//chapterで章、numberで問題番号、answerで問題中の解答番号受け取る	
	@GetMapping(value="/wiki/{version}/{chapter}/{number}/{answer}")
	public String wiki(Model model,
			@PathVariable("version") String version,
			@PathVariable("chapter") int chapter,
			@PathVariable("number") int number,
			@PathVariable("answer") int answer) {
		
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
		 * 問題数が０の場合ホームへリダイレクト,問題が最後になったときにendをtrue,それ以外のときは次の問題番号をモデルに登録
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

		//解答リストのそれぞれのワードがWikipedia検索ができるかをチェック
		ArrayList<Integer> checkWikiList = wikiService.checkWiki(answerList);
		model.addAttribute("checkWikiList", checkWikiList);
		
		//問題文をquestionとしてModelに登録
		model.addAttribute("question", question);	
		
		//answerListをModelに登録
		model.addAttribute("answerList", answerList);
		
		//wikiAPIを用いて取得したデータをModelに登録
		String JsonStr = wikiService.searchWiki(answerList.get(answer));
		System.out.println(JsonStr);
		model.addAttribute("JsonStr", JsonStr);
		
		//th:fragmentの値をmodelに登録
		model.addAttribute("contents", "wiki::wiki_contents");
		
		return "homeLayout";
	}
}
