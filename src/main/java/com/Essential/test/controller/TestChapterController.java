package com.Essential.test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestChapterController {

	//testChapter選択画面へ
	@GetMapping("/test/chapter")
	public String getChapter(Model model) {
		model.addAttribute("contents", "test/testChapter::testChapter_contents");
		return "homeLayout";
	}
}
