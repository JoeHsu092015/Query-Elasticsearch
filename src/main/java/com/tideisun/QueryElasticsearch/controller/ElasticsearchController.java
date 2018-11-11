package com.tideisun.QueryElasticsearch.controller;


import com.tideisun.QueryElasticsearch.elasticsearch.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class ElasticsearchController {

    @Autowired
    private ElasticsearchService esService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public List<Map<String, Object>> getData() {
        System.out.println("hello");

        return esService.searchRequest("debug");
    }


}
