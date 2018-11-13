package com.tideisun.QueryElasticsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchConfiguration.class);

    @Value("${spring.data.elasticsearch.cluster-nodes}")
    private String clusterNodes;

    @Value("${spring.data.elasticsearch.cluster-name}")
    private String clusterName;

    private static final String elkServerIP = "192.168.50.199";

    @Bean
    public RestHighLevelClient getRestHighLevelClient() {
        System.out.println("buildClient");
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost(elkServerIP, 9200, "http")));
        return client;
    }
}

