package com.Essential.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.Essential.domain.service.AnswerRateService;
import com.Essential.domain.service.QuestionService;

@Controller
public class TestChapterController {

	@Autowired
	QuestionService questionService;
	
	@Autowired
	AnswerRateService answerRateService;
	//testChapter選択画面へ
	@GetMapping("/test/chapter")
	public String getChapter(Model model) {
		//ユーザー名
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userId = user.getUsername();	
		
		//ユーザーの解答率をリストで取得
		int[] answerRate = AnswerRateList(userId);
		
		//解答率リストをモデルへ追加
		model.addAttribute("answerRate", answerRate);
		//fragmentをモデルへ追加
		model.addAttribute("contents", "test/testChapter::testChapter_contents");
		return "homeLayout";
	}
	
	//ユーザーの解答率リストを取得
	private int[] AnswerRateList(String userId) {
		//chapter数
		final int chapters = 20;
		//解答率リスト用変数
		int[] answerRate = new int[chapters];
		
		for(int chapter=1;chapter<=chapters;chapter++) {
			//問題数用変数
			int questionCount = 0;
			//問題数取得
			try {
				questionCount = questionService.chapterCount(chapter);
			} catch(ReflectiveOperationException e) {
				e.printStackTrace();
			} catch(IllegalArgumentException e) {
				e.printStackTrace();
			}
			//questionCountが0より大きいときユーザーの章の解答率を求める
			if(questionCount > 0) {
				double answerRateD = (double)answerRateService.getAnswer(userId, chapter)/questionCount;
				System.out.println(answerRateService.getAnswer(userId, chapter) + "/" + questionCount + "=" + answerRateD);
				answerRate[chapter-1] = (int)(answerRateD*100);
			}
		}
		return answerRate;
	}
}
