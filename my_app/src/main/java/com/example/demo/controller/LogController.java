package com.example.demo.controller;

import com.example.demo.service.ElasticsearchService;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

@RestController
public class LogController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @GetMapping("/search")
    public JsonNode searchLogs() throws IOException {
        return elasticsearchService.searchLogs();
    }

    
}
