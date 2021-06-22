package com.example.youtubesearcher.service;

import com.example.youtubesearcher.model.Query;
import com.example.youtubesearcher.model.QueryRepository;
import com.example.youtubesearcher.model.YouTubeVideo;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class YouTubeApiService {

    private static final Logger log = LoggerFactory.getLogger(YouTubeApiService.class);

    private final QueryRepository queryRepository;

    private static final long MAX_SEARCH_RESULTS = 20;
    private static final String GOOGLE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private static final String YOUTUBE_SEARCH_TYPE = "video";
    private static final String YOUTUBE_SEARCH_FIELDS = "items(id/kind,id/videoId,snippet/title,snippet/description,snippet/publishedAt)";
    private static final String YOUTUBE_APIKEY = "AIzaSyBJCyxRi8lebuSH7ijGoblfrvGNq7JkMX0";

    public YouTubeApiService(QueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public List<YouTubeVideo> getVideosByQuery(String query) {
        Optional<Query> findQuery = queryRepository.findById(query);
        List<YouTubeVideo> videos;
        if (findQuery.isPresent() && findQuery.get().getVideos().size() > 0) {
            log.info("Get YouTubeVideos from db");
            videos = findQuery.get().getVideos();
        } else {
            videos = fetchVideosByQuery(query);
            if (videos.size() > 0) {
                log.info("Create and save new query to db");
                Query newQuery = new Query();
                newQuery.setQuery(query);
                newQuery.setVideos(videos);
                newQuery.setCreatedAt(new Date());
                queryRepository.save(newQuery);
            }
        }
        return videos;
    }

    public List<YouTubeVideo> fetchVideosByQuery(String query) {
        List<YouTubeVideo> videos = new ArrayList<>();


        try {
            log.info("Starting YouTube fetch by query: " + query);

            YouTube youtube = getYouTube();

            YouTube.Search.List search = youtube.search().list("id,snippet");

            search.setKey(YOUTUBE_APIKEY);
            search.setQ(query);
            search.setType(YOUTUBE_SEARCH_TYPE);
            search.setFields(YOUTUBE_SEARCH_FIELDS);
            search.setMaxResults(MAX_SEARCH_RESULTS);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList.size() > 0) {
                for (SearchResult result : searchResultList) {

                    YouTubeVideo video = new YouTubeVideo();
                    video.setTitle(result.getSnippet().getTitle());
                    video.setUrl(buildVideoUrl(result.getId().getVideoId()));
                    video.setDescription(result.getSnippet().getDescription());
                    videos.add(video);
                }
            } else {
                log.info("No search results got from YouTube API");
            }
        } catch (Exception e) {
            log.warn("Fetch data errors", e);
            e.printStackTrace();
        }

        return videos;
    }

    private String buildVideoUrl(String videoId) {

        return GOOGLE_YOUTUBE_URL + videoId;
    }

    private YouTube getYouTube() {

        return new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(),
                (request) -> {
                }).setApplicationName("youtube-search").build();
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void removeQueryDataAfterDay() {
        log.info("Deleting old data");

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date oneDay = new Date(cal.getTimeInMillis());
        queryRepository.deleteByCreatedAtBefore(oneDay);
    }

}
