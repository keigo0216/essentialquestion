package com.Essential;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	private DataSource dataSource;
	
	//ユーザーIDとパスワードを取得するSQL文
	private static final String USER_SQL = " SELECT" + " user_id," + " password," + " true" + " FROM" + " m_user" + " WHERE" + " user_id=?";
	
	//ユーザーのロールを取得するSQL文
	private static final String ROLE_SQL = "SELECT" + " user_id," + " role" + " FROM" + " m_user" +  " WHERE" + " user_id = ?";
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**","/webjars/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers("/css/**").permitAll()
			.antMatchers("/webjars/**").permitAll()
			.antMatchers("/login").permitAll()
			.antMatchers("/signup").hasAuthority("ROLE_DEFAULT")
			.antMatchers("/test/**").hasAuthority("ROLE_GENERAL")
			.anyRequest().authenticated();
		
		http
			.formLogin()
				.loginProcessingUrl("/login")
				.loginPage("/login")
				.failureUrl("/login?error")
				.usernameParameter("userId")
				.passwordParameter("password")
				.defaultSuccessUrl("/",true);
		
		http
			.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login");		
		
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication()
			.dataSource(dataSource)
			.usersByUsernameQuery(USER_SQL)
			.authoritiesByUsernameQuery(ROLE_SQL)
			.passwordEncoder(passwordEncoder());
	}
}














