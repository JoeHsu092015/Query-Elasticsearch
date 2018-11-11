package com.tideisun.QueryElasticsearch.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tideisun.QueryElasticsearch.config.ElasticsearchConfiguration;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.elasticsearch.action.search.SearchResponse;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
public class ElasticsearchService {
    private final String INDEX = "kibana_sample_data_flights";
    private final String TYPE = "_doc";

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchService.class);
    private ObjectMapper objectMapper;

    public ElasticsearchService(ObjectMapper objectMapper, RestHighLevelClient restHighLevelClient) {
        this.objectMapper = objectMapper;
        this.restHighLevelClient = restHighLevelClient;
        System.out.println("call ElasticsearchService ");
    }


    public List<Map<String, Object>> searchRequest(String queryString) {
        try {
            SearchRequest searchRequest = new SearchRequest(INDEX);

            searchRequest.types(TYPE);
            System.out.println("call searchRequest ");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.termQuery("DestWeather", "Rain"));
            sourceBuilder.from(0);
            sourceBuilder.size(20);
            sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
            String[] includeFields = new String[] {"OriginLocation", "DestLocation"};
            String[] excludeFields = new String[] {"_type"};
            sourceBuilder.fetchSource(includeFields,excludeFields);




            searchRequest.source(sourceBuilder);
            List<Map<String, Object>> responseMap = new ArrayList<>();
            Map<String, Object> tmpMap;

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);


            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits.getHits()) {
                tmpMap = hit.getSourceAsMap();
                responseMap.add(tmpMap);
            }

            LOG.info("hit value: "+hits.totalHits);
            return responseMap;
        } catch (java.io.IOException e) {
            System.out.println(e.getLocalizedMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
