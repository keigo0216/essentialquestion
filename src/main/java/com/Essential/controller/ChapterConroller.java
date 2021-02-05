package com.Essential.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChapterConroller {
	
	//chapter選択画面へ
	@GetMapping("/")
	public String getChapter(Model model) {
		model.addAttribute("contents", "chapter::chapter_contents");
		return "homeLayout";
	}
}
