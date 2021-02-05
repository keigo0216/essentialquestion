package com.Essential.domain.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Data;


@Data
public class SignupForm {
	
	@NotBlank(groups = ValidGroup1.class)
	private String userId;
	
	@NotBlank(groups = ValidGroup1.class)
	@Length(min=4, max=100,groups = ValidGroup2.class)
	@Pattern(regexp="^[a-zA-Z0-9]+$",groups = ValidGroup3.class)
	private String password;
	
	
	
	
	
	
}
