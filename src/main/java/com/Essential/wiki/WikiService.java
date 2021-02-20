package com.Essential.wiki;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

@Service
public class WikiService {
	
	@Autowired
	RestOperations restOperation;
	
	//wikiAPIを用いてWikipediaを検索
	public String searchWiki(String word) {
		//エンドポイント
		String url = "https://ja.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=" + word;
		
		//wikiAPIを使用
		String resource = restOperation.getForObject(url, String.class);
		
		System.out.println(resource.contains("extract"));
		
		return resource;
	}
	
	
	  //引数のString型List中のワード毎にwikipedia検索ができるかをチェック public ArrayList<Integer>
	  public ArrayList<Integer> checkWiki(ArrayList<String> answerList) {
		  //結果返却用リスト
		  ArrayList<Integer> checkWikiList = new ArrayList<>();
		  

		  for(String w : answerList) {
			  //wikiAPIで送られてきたJSONデータに、"extract"が含まれているかどうかでwikipedia検索ができるかをチェック
			  String resource = searchWiki(w);
			  boolean search = resource.contains("extract");
			  
			  //Jsonデータにextractが含まれていた場合、そのワードのインデックスをcheckListに追加
			  if(search == true) {
				  int index = answerList.indexOf(w);
				 checkWikiList.add(index);
			  }
			  
		  }
			  
		  return checkWikiList;
	  }
	 
}
