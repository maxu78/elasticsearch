package com.mx.elasticsearch.demo;

import com.mx.elasticsearch.util.ElasticUtils;
import io.searchbox.client.JestResult;

public class ElasticSearchDemo {

    public static void main(String[] args) throws Exception {

        String index = "articles";
        String type = "raw";
        String settings = "{ \"number_of_shards\" : 1, \"number_of_replicas\" : 0}";
        String mappings = "{ \"properties\" : { \"message\" : {\"type\" : \"string\", \"store\" : \"yes\", \"index\" : \"not_analyzed\", \"null_value\" : \"jim\" } } }";
        String source = "{\"message\" : \"kimchy\"}";
        String query = "{\"from\": 0, \"size\": 2}";
//        JestResult result = ElasticUtils.createIndex(index);
//        JestResult result = ElasticUtils.createIndex(index, settings);
//        JestResult result = ElasticUtils.createMapping(index, type, mappings);
//        JestResult result = ElasticUtils.insert(index, type, source);
//        JestResult result = ElasticUtils.search(index, query);
//        System.out.println(result.getJsonObject().toString());
//        JestResult result = ElasticUtils.delete(index, type, "AWHRZM0UdEgGAXYQDicW");

//        String url = "http://localhost:10200/_bulk";
//        String bulk = "{ index: {\"_index\":\"articles\" , \"_type\":\"raw\"} }\n"
//                + "{\"message\":\"tom\",\"number\":987654321}\n"
//                + "{ index: {\"_index\":\"articles\" , \"_type\":\"raw\"} }\n"
//                + "{\"message\":\"cat\",\"number\":123456789}\n";
//        String rs = ElasticUtils.sendPost(url, bulk);
//        System.out.println(rs);

        String url = "http://127.0.0.1:10200/_sql";
        String sql = "select * from articles ";
        String rs = ElasticUtils.sendPost(url, sql);
        System.out.println(rs);
    }

}
