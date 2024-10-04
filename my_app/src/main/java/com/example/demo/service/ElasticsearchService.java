package com.example.demo.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.Map;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.elasticsearch.core.search.Hit;
import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;

@Service
public class ElasticsearchService {

    @Autowired
    private ElasticsearchClient client;

    private final ObjectMapper objectMapper;

    public ElasticsearchService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public JsonNode searchLogs() throws IOException {
        SearchRequest searchRequest = new SearchRequest.Builder()
            .index("filebeat-*")
            .query(q -> q
                .exists(m -> m
                    .field("agent.hostname")

                )
            )
            .build();

        // Execute the search and get the response as Map
        SearchResponse<JsonNode> searchResponse = client.search(searchRequest, JsonNode.class);

        // Extract hits from the response
        List<Hit<JsonNode>> hits = searchResponse.hits().hits();

        // Create an array node to hold the hit sources
        ArrayNode hitsNode = objectMapper.createArrayNode();
        for (Hit<JsonNode> hit : hits) {
            // Add each hit's source to the array node
            hitsNode.add(hit.source());
        }

        return hitsNode;
    }

}
