package com.kh.Final_Project.recipes.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.Final_Project.product.entity.Product;
import com.kh.Final_Project.product.repository.ProductRepository;
import com.kh.Final_Project.recipes.repository.RecipesRepository;
import com.kh.Final_Project.recipes.vo.Recipe;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecipesService {
	
	@Autowired
	private RecipesRepository recipesRepository;
    
	@Autowired
	private ProductRepository productRepository;
	
	public void resetRecipesTable() {
		if(recipesRepository.count() == 0) {
			fetchRecipes();
			fetchRecipes2();
		}
	}
	
	public void deleteAllRecipes() {
		recipesRepository.deleteAll();
	}
	

	public void fetchRecipes() {

		log.info("레시피 서비스  호출 성공");

		String targetUrl = "http://openapi.foodsafetykorea.go.kr/api/f8ecd6b02eda4136a90f/COOKRCP01/json/1/999";

		try {

			URL url = new URL(targetUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			// 다시 문자열을 저장하는 공간에 저장
			StringBuffer newResult = new StringBuffer();

			String line;

			while ((line = rd.readLine()) != null) {
				newResult.append(line);
			}

			rd.close();
			// 서버랑 연결도 종료한다.
			conn.disconnect();

			JSONParser parser = new JSONParser();

			JSONObject jsonObject = (JSONObject) parser.parse(newResult.toString());

			JSONObject jobj = (JSONObject) jsonObject.get("COOKRCP01");

			JSONArray array = (JSONArray) jobj.get("row");

			for(Object obj : array) {
			    JSONObject temp = (JSONObject) obj;

			    Recipe recipe = new Recipe();

			    recipe.setRcpSeq((String) temp.get("RCP_SEQ"));
			    recipe.setRcpNm((String) temp.get("RCP_NM"));
			    recipe.setRcpWay2((String) temp.get("RCP_WAY2"));
			    recipe.setRcpPat2((String) temp.get("RCP_PAT2"));
			    recipe.setInfoWgt((String) temp.get("INFO_WGT"));
			    recipe.setInfoEng((String) temp.get("INFO_ENG"));
			    recipe.setInfoCar((String) temp.get("INFO_CAR"));
			    recipe.setInfoPro((String) temp.get("INFO_PRO"));
			    recipe.setInfoFat((String) temp.get("INFO_FAT"));
			    recipe.setInfoNa((String) temp.get("INFO_NA"));
			    recipe.setHashTag((String) temp.get("HASH_TAG"));
			    recipe.setAttFileNoMain((String) temp.get("ATT_FILE_NO_MAIN"));
			    recipe.setAttFileNoMk((String) temp.get("ATT_FILE_NO_MK"));
			    recipe.setRcpPartsDtls((String) temp.get("RCP_PARTS_DTLS"));
			    recipe.setManual01((String) temp.get("MANUAL01"));
			    recipe.setManualImg01((String) temp.get("MANUAL_IMG01"));
			    recipe.setManual02((String) temp.get("MANUAL02"));
			    recipe.setManualImg02((String) temp.get("MANUAL_IMG02"));
			    recipe.setManual03((String) temp.get("MANUAL03"));
			    recipe.setManualImg03((String) temp.get("MANUAL_IMG03"));
			    recipe.setManual04((String) temp.get("MANUAL04"));
			    recipe.setManualImg04((String) temp.get("MANUAL_IMG04"));
			    recipe.setManual05((String) temp.get("MANUAL05"));
			    recipe.setManualImg05((String) temp.get("MANUAL_IMG05"));
			    recipe.setManual06((String) temp.get("MANUAL06"));
			    recipe.setManualImg06((String) temp.get("MANUAL_IMG06"));
			    recipe.setManual07((String) temp.get("MANUAL07"));
			    recipe.setManualImg07((String) temp.get("MANUAL_IMG07"));
			    recipe.setManual08((String) temp.get("MANUAL08"));
			    recipe.setManualImg08((String) temp.get("MANUAL_IMG08"));
			    recipe.setManual09((String) temp.get("MANUAL09"));
			    recipe.setManualImg09((String) temp.get("MANUAL_IMG09"));
			    recipe.setManual10((String) temp.get("MANUAL10"));
			    recipe.setManualImg10((String) temp.get("MANUAL_IMG10"));
			    recipe.setManual11((String) temp.get("MANUAL11"));
			    recipe.setManualImg11((String) temp.get("MANUAL_IMG11"));
			    recipe.setManual12((String) temp.get("MANUAL12"));
			    recipe.setManualImg12((String) temp.get("MANUAL_IMG12"));
			    recipe.setManual13((String) temp.get("MANUAL13"));
			    recipe.setManualImg13((String) temp.get("MANUAL_IMG13"));
			    recipe.setManual14((String) temp.get("MANUAL14"));
			    recipe.setManualImg14((String) temp.get("MANUAL_IMG14"));
			    recipe.setManual15((String) temp.get("MANUAL15"));
			    recipe.setManualImg15((String) temp.get("MANUAL_IMG15"));
			    recipe.setManual16((String) temp.get("MANUAL16"));
			    recipe.setManualImg16((String) temp.get("MANUAL_IMG16"));
			    recipe.setManual17((String) temp.get("MANUAL17"));
			    recipe.setManualImg17((String) temp.get("MANUAL_IMG17"));
			    recipe.setManual18((String) temp.get("MANUAL18"));
			    recipe.setManualImg18((String) temp.get("MANUAL_IMG18"));
			    recipe.setManual19((String) temp.get("MANUAL19"));
			    recipe.setManualImg19((String) temp.get("MANUAL_IMG19"));
			    recipe.setManual20((String) temp.get("MANUAL20"));
			    recipe.setManualImg20((String) temp.get("MANUAL_IMG20"));
			    recipe.setRcpNaTip((String) temp.get("RCP_NA_TIP"));

			    // recipe 객체를 리스트에 추가하는 부분
			    recipesRepository.save(recipe);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void fetchRecipes2() {

		log.info("레시피2 서비스  호출 성공");

		String targetUrl = "http://openapi.foodsafetykorea.go.kr/api/f8ecd6b02eda4136a90f/COOKRCP01/json/1000/1124";

		try {

			URL url = new URL(targetUrl);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			// 다시 문자열을 저장하는 공간에 저장
			StringBuffer newResult = new StringBuffer();

			String line;

			while ((line = rd.readLine()) != null) {
				newResult.append(line);
			}

			rd.close();
			// 서버랑 연결도 종료한다.
			conn.disconnect();

			JSONParser parser = new JSONParser();

			JSONObject jsonObject = (JSONObject) parser.parse(newResult.toString());

			JSONObject jobj = (JSONObject) jsonObject.get("COOKRCP01");

			JSONArray array = (JSONArray) jobj.get("row");

			for(Object obj : array) {
			    JSONObject temp = (JSONObject) obj;

			    Recipe recipe = new Recipe();

			    recipe.setRcpSeq((String) temp.get("RCP_SEQ"));
			    recipe.setRcpNm((String) temp.get("RCP_NM"));
			    recipe.setRcpWay2((String) temp.get("RCP_WAY2"));
			    recipe.setRcpPat2((String) temp.get("RCP_PAT2"));
			    recipe.setInfoWgt((String) temp.get("INFO_WGT"));
			    recipe.setInfoEng((String) temp.get("INFO_ENG"));
			    recipe.setInfoCar((String) temp.get("INFO_CAR"));
			    recipe.setInfoPro((String) temp.get("INFO_PRO"));
			    recipe.setInfoFat((String) temp.get("INFO_FAT"));
			    recipe.setInfoNa((String) temp.get("INFO_NA"));
			    recipe.setHashTag((String) temp.get("HASH_TAG"));
			    recipe.setAttFileNoMain((String) temp.get("ATT_FILE_NO_MAIN"));
			    recipe.setAttFileNoMk((String) temp.get("ATT_FILE_NO_MK"));
			    recipe.setRcpPartsDtls((String) temp.get("RCP_PARTS_DTLS"));
			    recipe.setManual01((String) temp.get("MANUAL01"));
			    recipe.setManualImg01((String) temp.get("MANUAL_IMG01"));
			    recipe.setManual02((String) temp.get("MANUAL02"));
			    recipe.setManualImg02((String) temp.get("MANUAL_IMG02"));
			    recipe.setManual03((String) temp.get("MANUAL03"));
			    recipe.setManualImg03((String) temp.get("MANUAL_IMG03"));
			    recipe.setManual04((String) temp.get("MANUAL04"));
			    recipe.setManualImg04((String) temp.get("MANUAL_IMG04"));
			    recipe.setManual05((String) temp.get("MANUAL05"));
			    recipe.setManualImg05((String) temp.get("MANUAL_IMG05"));
			    recipe.setManual06((String) temp.get("MANUAL06"));
			    recipe.setManualImg06((String) temp.get("MANUAL_IMG06"));
			    recipe.setManual07((String) temp.get("MANUAL07"));
			    recipe.setManualImg07((String) temp.get("MANUAL_IMG07"));
			    recipe.setManual08((String) temp.get("MANUAL08"));
			    recipe.setManualImg08((String) temp.get("MANUAL_IMG08"));
			    recipe.setManual09((String) temp.get("MANUAL09"));
			    recipe.setManualImg09((String) temp.get("MANUAL_IMG09"));
			    recipe.setManual10((String) temp.get("MANUAL10"));
			    recipe.setManualImg10((String) temp.get("MANUAL_IMG10"));
			    recipe.setManual11((String) temp.get("MANUAL11"));
			    recipe.setManualImg11((String) temp.get("MANUAL_IMG11"));
			    recipe.setManual12((String) temp.get("MANUAL12"));
			    recipe.setManualImg12((String) temp.get("MANUAL_IMG12"));
			    recipe.setManual13((String) temp.get("MANUAL13"));
			    recipe.setManualImg13((String) temp.get("MANUAL_IMG13"));
			    recipe.setManual14((String) temp.get("MANUAL14"));
			    recipe.setManualImg14((String) temp.get("MANUAL_IMG14"));
			    recipe.setManual15((String) temp.get("MANUAL15"));
			    recipe.setManualImg15((String) temp.get("MANUAL_IMG15"));
			    recipe.setManual16((String) temp.get("MANUAL16"));
			    recipe.setManualImg16((String) temp.get("MANUAL_IMG16"));
			    recipe.setManual17((String) temp.get("MANUAL17"));
			    recipe.setManualImg17((String) temp.get("MANUAL_IMG17"));
			    recipe.setManual18((String) temp.get("MANUAL18"));
			    recipe.setManualImg18((String) temp.get("MANUAL_IMG18"));
			    recipe.setManual19((String) temp.get("MANUAL19"));
			    recipe.setManualImg19((String) temp.get("MANUAL_IMG19"));
			    recipe.setManual20((String) temp.get("MANUAL20"));
			    recipe.setManualImg20((String) temp.get("MANUAL_IMG20"));
			    recipe.setRcpNaTip((String) temp.get("RCP_NA_TIP"));

			    // recipe 객체를 리스트에 추가하는 부분
			    recipesRepository.save(recipe);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

	}



	public List<Recipe> getRecipeById(Long productId) {
		
		Optional<Product> productOpt = productRepository.findById(productId);
        if(productOpt.isEmpty()) {
        	
        	return null;
        }
        String productName = productOpt.get().getProductName();
		
		return recipesRepository.findTop10ByRcpPartsDtlsContaining(productName);
		
	}



}
