package com.example.youtubesearcher.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Query {
    @Id
    @Column(unique = true)
    private String query;
    private Date createdAt;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<YouTubeVideo> videos;
}
