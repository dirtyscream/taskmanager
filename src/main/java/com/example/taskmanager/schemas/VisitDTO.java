package com.example.taskmanager.schemas;

import lombok.Data;

@SuppressWarnings("checkstyle:AbbreviationAsWordInName")
@Data
public class VisitDTO {
    private String url;
    private long visitCount;
}
