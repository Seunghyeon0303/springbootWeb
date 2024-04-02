package com.kh.Final_Project.youtube.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class YoutubeService {

	private final String apiKey = "AIzaSyAAj3F4R5PD53XWNFtYj74ce7w0CnQN5PY";
	private final RestTemplate restTemplate = new RestTemplate();

    public List<String> findEmbeddableVideosForProductName(String query) {
        List<String> embeddableVideoIds = new ArrayList<>();
        String searchUrl = String.format(
                "https://www.googleapis.com/youtube/v3/search?part=id&q=%s+요리&maxResults=5&key=%s&type=video",
                query, apiKey);

        String response = restTemplate.getForObject(searchUrl, String.class);
        JSONObject searchResults = new JSONObject(response);
        JSONArray items = searchResults.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            String videoId = items.getJSONObject(i).getJSONObject("id").getString("videoId");

            // Check if the video is embeddable
            String videoDetailsUrl = String.format(
                    "https://www.googleapis.com/youtube/v3/videos?part=status&id=%s&key=%s",
                    videoId, apiKey);
            String detailsResponse = restTemplate.getForObject(videoDetailsUrl, String.class);
            JSONObject videoDetails = new JSONObject(detailsResponse);
            boolean isEmbeddable = videoDetails.getJSONArray("items").getJSONObject(0)
                    .getJSONObject("status").getBoolean("embeddable");

            if (isEmbeddable) {
                embeddableVideoIds.add(videoId);
            }
        }

        return embeddableVideoIds;
    }

	
	
}
