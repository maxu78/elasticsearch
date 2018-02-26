package com.mx.elasticsearch.dao.impl;

import com.mx.elasticsearch.dao.ElasticSearchDao;

import java.util.Map;

public class ElasticSearchDaoImpl implements ElasticSearchDao {

    @Override
    public String createIndex(String index) {
        return null;
    }

    @Override
    public String createIndex(String index, String type) {
        return null;
    }

    @Override
    public String createIndex(String index, String type, Map map) {
        return null;
    }

    @Override
    public String createMapping(Map map) {
        return null;
    }

    @Override
    public String insert(String index, Map map) {
        return null;
    }

    @Override
    public String insert(String index, String type, Map map) {
        return null;
    }

    @Override
    public String delete(String index) {
        return null;
    }

    @Override
    public String delete(String index, String type) {
        return null;
    }

    @Override
    public String update(String index, Map map) {
        return null;
    }

    @Override
    public String update(String index, String type, Map map) {
        return null;
    }

    @Override
    public String queryByJson(String json) {
        return null;
    }

    @Override
    public String queryBySql(String sql) {
        return null;
    }
}
