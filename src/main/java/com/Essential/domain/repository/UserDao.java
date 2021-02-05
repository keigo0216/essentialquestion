package com.Essential.domain.repository;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.Essential.domain.model.User;

@Repository
public class UserDao {
	@Autowired
	JdbcTemplate jdbc;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	//DBにUserを保存
	public void insertOne(User user) {
		
		//パスワード暗号化
		String password = passwordEncoder.encode(user.getPassword());
		
		String sql = "INSERT INTO m_user"
				+ " VALUES('" + user.getUserId() + "','" + password + "','" + user.getRole() + "')";
		
		jdbc.execute(sql);
	}
	
	//DBからuserIdによってUserを検索
	public User selectOne(String userId) {
		
		//1件取得
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM m_user" 
				+ " WHERE user_id = ?"
				,userId);
		//結果返却用の変数
		User user = new User();
		user.setUserId((String)map.get("user_id"));
		user.setPassword((String)map.get("password"));
		user.setRole((String)map.get("role"));
		
		return user;
	}
	

	
}
