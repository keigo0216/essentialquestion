package com.Essential.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.Essential.domain.model.GroupOrder;
import com.Essential.domain.model.SignupForm;
import com.Essential.domain.model.User;
import com.Essential.domain.service.AnswerRateService;
import com.Essential.domain.service.TestService;
import com.Essential.domain.service.UserService;

@Controller
public class SignupController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TestService testService;
	
	@Autowired
	private AnswerRateService answerRateService;
	
	
	//ユーザー登録画面のGET用コントローラー
	@GetMapping("/signup")
	public String getSignUp(@ModelAttribute SignupForm form,Model model) {
		return "signup";
	}
	
	//ユーザー登録画面のPOST用コントローラー
	@PostMapping("/signup")
	@Transactional
	public String postSignUp(@ModelAttribute @Validated(GroupOrder.class) SignupForm form,
			BindingResult bindingResult,
			Model model) {
		
		
		//入力チェックに引っかかった場合、ユーザー登録画面に戻る
		if(bindingResult.hasErrors()) {
			return getSignUp(form,model);
		}
		
		//ユーザーIDがすでに登録されいるか判定
		//判定用変数
		Boolean duplicate = true;
		
		try {
			duplicate = userService.isDuplicate(form.getUserId());
		} catch(DataAccessException e) {
			e.printStackTrace();
		}
		
		if(duplicate == true) {
			//duplicateをModelに追加
			model.addAttribute("duplicate", duplicate);
			return getSignUp(form,model);
		} else {
			
			//Userデータinsert用変数
			User user = new User();
			//formの値を代入
			user.setUserId(form.getUserId());
			user.setPassword(form.getPassword());
			user.setRole("ROLE_GENERAL");
			
			//ユーザーの登録、登録するユーザーのテスト問題テーブルを作成
			//判定用変数
			Boolean register = false;
			try{
				//登録するユーザーのテスト問題テーブルを作成
				testService.createTesttable(user.getUserId());
				
				//登録するユーザーの解答正解数テーブルを作成
				answerRateService.createAnswerRateTable(user.getUserId());
				
				//上記の処理を終えてから処理することで、テスト問題、解答正解数テーブルを持っていないユーザー登録を避ける
				userService.insert(user);
				
				//ここまでの操作を終えたらresisterをtrueに設定
				register = true;
			} catch(DataAccessException e) {
				e.printStackTrace();
				System.out.println("テスト問題テーブルまたは解答正解数テーブルを作成失敗");
				register = false;
			}
			
			//登録に失敗した場合、getSignUpメソッドを呼び出す
			if(register == false) {
				model.addAttribute("register", register);
				return getSignUp(form,model);
			}
			
			
		}
		
		return "redirect:/login";
	}
}
