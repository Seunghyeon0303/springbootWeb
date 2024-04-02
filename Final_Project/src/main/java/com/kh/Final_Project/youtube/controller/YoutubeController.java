package com.kh.Final_Project.youtube.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.kh.Final_Project.product.entity.Product;
import com.kh.Final_Project.product.repository.ProductRepository;
import com.kh.Final_Project.youtube.service.YoutubeService;

import java.util.*;
import org.json.*;

@Controller
@RequestMapping("/youtube")
public class YoutubeController {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private YoutubeService youtubeService;

	private final String apiKey = "AIzaSyAAj3F4R5PD53XWNFtYj74ce7w0CnQN5PY";
	RestTemplate restTemplate = new RestTemplate();

	// 컨트롤러에서 지정한 영상이 보여지도록 설정
//	@GetMapping("/showVideos")
//	public String showVideos(Model model,
//			@RequestParam(value = "query", required = false, defaultValue = "김종국") String query) {
//		final String searchUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + query
//				+ "&maxResults=5&key=" + apiKey + "&type=video";
//
//		String response = restTemplate.getForObject(searchUrl, String.class);
//
//		JSONObject jsonResponse = new JSONObject(response);
//		model.addAttribute("videos", jsonResponse.getJSONArray("items"));
//
//		return "youtube/youtubeList"; // resources/templates/youtube/youtubeList.html
//	}

	// 뷰 페이지에서 직접 검색어를 입력한 결과의 영상이 보여지도록 설정
	@GetMapping("/search")
	public String searchVideos(Model model, @RequestParam(value = "query", required = false,defaultValue = "요리") String query) {
		List<Map<String, String>> videos = new ArrayList<>();

		if (query != null && !query.trim().isEmpty()) {
			// API 키를 여기에 입력
			final String searchUrl = "https://www.googleapis.com/youtube/v3/search?part=snippet&q=" + query
					+ "&maxResults=5&key=" + apiKey + "&type=video";

			RestTemplate restTemplate = new RestTemplate();
			String response = restTemplate.getForObject(searchUrl, String.class);

			JSONObject jsonResponse = new JSONObject(response);
			JSONArray items = jsonResponse.getJSONArray("items");

			for (int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i);
				JSONObject id = item.getJSONObject("id");
				JSONObject snippet = item.getJSONObject("snippet");
				Map<String, String> video = new HashMap<>();
				video.put("videoId", id.getString("videoId"));
				video.put("title", snippet.getString("title"));
				video.put("description", snippet.getString("description"));
				videos.add(video);
			}
		}

		model.addAttribute("videos", videos);

		return "youtube/videos";
	}

	// mySql에 등록된 '데이터 + 요리' 검색 결과의 영상이 보여지도록 설정
//	@GetMapping("/youtubeVideos")
//    public String showEmbeddableVideos(Model model) {
//        // 데이터베이스에서 상품 리스트를 가져옵니다.
//        List<Product> products = productRepository.findAll();
//
//        // 상품이 존재하지 않으면 빈 화면을 반환합니다.
//        if (products.isEmpty()) {
//            return "youtube/videos";
//        }
//
//        // 예시로 첫 번째 상품의 이름을 사용합니다.
//        Product firstProduct = products.get(0);
//        String query = firstProduct.getProductName() + " 요리";
//
//        // 해당 상품 이름으로 YouTube 검색을 수행하고 임베드 가능한 동영상 ID 목록을 가져옵니다.
//        List<String> embeddableVideoIds = youtubeService.findEmbeddableVideosForProductName(query);
//
//        // 모델에 동영상 ID 목록을 추가합니다.
//        model.addAttribute("videoIds", embeddableVideoIds);
//		return "youtube/resultVideos"; // 뷰 페이지 이름
//	}
}
