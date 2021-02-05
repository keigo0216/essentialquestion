package com.Essential.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.Essential.domain.model.User;
import com.Essential.domain.repository.UserDao;

@Service
public class UserService {
	
	@Autowired
	UserDao dao;
	
	//insert用メソッド
	public void insert(User user) {
		dao.insertOne(user);
	}
	
	//userId重複確認メソッド
	public boolean isDuplicate(String userId)throws DataAccessException {
		//判定用変数
		boolean duplicate = true;
		
		//ユーザーIDが見つからない場合の例外をキャッチしてduplicateをfalseに設定
		try{
			dao.selectOne(userId);
		} catch(EmptyResultDataAccessException e) {
			duplicate = false;
		}
		return duplicate;
	}
	
	
 		
}
