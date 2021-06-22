package com.example.youtubesearcher.model;

import lombok.Data;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Data
@Table(name="youtube_video")
public class YouTubeVideo {
    @Id
    @Column(unique = true)
    private String url;
    private String title;
    private String description;

}
