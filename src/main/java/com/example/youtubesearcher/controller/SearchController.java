package com.example.youtubesearcher.controller;

import com.example.youtubesearcher.model.SearchQueryCriteria;
import com.example.youtubesearcher.model.YouTubeVideo;
import com.example.youtubesearcher.service.YouTubeApiService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class SearchController {

    private final YouTubeApiService youtubeApiService;

    public SearchController(YouTubeApiService youtubeApiService) {
        this.youtubeApiService = youtubeApiService;
    }

    @RequestMapping(value = "api/search", method = RequestMethod.GET)
    public List<YouTubeVideo> searchYoutubeVideos(@Valid SearchQueryCriteria searchQueryCriteria, Model model) {

        model.addAttribute("searchQueryCriteria", searchQueryCriteria);
        return youtubeApiService.getVideosByQuery(searchQueryCriteria.getQuery());
    }

    @RequestMapping(value = "api/search", method = RequestMethod.DELETE)
    public void deleteYoutubeVideo() {
        youtubeApiService.removeQueryDataAfterDay();
    }
}
