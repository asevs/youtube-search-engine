package com.example.youtubesearcher.model;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class SearchQueryCriteria {

    @Size(min=2, max=64, message="Search term must be between 1 and 64 characters")
    private String query;

}
