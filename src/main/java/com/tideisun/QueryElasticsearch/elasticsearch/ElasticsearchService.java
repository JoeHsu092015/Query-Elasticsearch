package com.tideisun.QueryElasticsearch.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.elasticsearch.action.search.SearchResponse;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class ElasticsearchService {
    private final String INDEX = "logstash-2018*";
    private final String TYPE = "doc";
    private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchService.class);

    private RestHighLevelClient restHighLevelClient;

    private ObjectMapper objectMapper;

    @Autowired
    public ElasticsearchService(ObjectMapper objectMapper,RestHighLevelClient restHighLevelClient) {
        for (Node a : restHighLevelClient.getLowLevelClient().getNodes()) {

            System.out.println("ElasticsearchService host names: "+a.getHost().getHostName());
        }

        this.objectMapper = objectMapper;
        this.restHighLevelClient = restHighLevelClient;
    }


    public List<Map<String, Object>> searchRequest(String queryString) {
        try {
            SearchRequest searchRequest = new SearchRequest(INDEX);

            searchRequest.types(TYPE);
            System.out.println("call searchRequest ");
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.query(QueryBuilders.termQuery("host", "192.168.1.152"));
            sourceBuilder.from(0);
            sourceBuilder.size(5);
            sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
            searchRequest.source(sourceBuilder);
            List<Map<String, Object>> responseMap = new ArrayList<>();

            Map<String, Object> tmpMap;

            //restHighLevelClient = new RestHighLevelClient(
            //        RestClient.builder(
            //                new HttpHost("192.168.50.199", 9200, "http")));

            for (Node a : restHighLevelClient.getLowLevelClient().getNodes()) {

                System.out.println("searchRequest host names: "+a.getHost().getHostName());
                //host names should be : 192.168.50.199
            }

            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);


            SearchHits hits = searchResponse.getHits();
            for (SearchHit hit : hits.getHits()) {
                tmpMap = hit.getSourceAsMap();
                responseMap.add(tmpMap);
            }

            LOG.info("hit value: "+hits.totalHits);
            return responseMap;
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
