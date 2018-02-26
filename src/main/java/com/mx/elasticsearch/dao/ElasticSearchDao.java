package com.mx.elasticsearch.dao;

import java.util.Map;

public interface ElasticSearchDao {

    /**
     * 创建索引
     * @param index
     * @return
     */
    public String createIndex(String index);

    /**
     * 创建索引
     * @param index
     * @param type
     * @return
     */
    public String createIndex(String index, String type);

    /**
     * 创建索引
     * @param index
     * @param type
     * @param map
     * @return
     */
    public String createIndex(String index, String type, Map map);

    /**
     * 创建mapping
     * @param map
     * @return
     */
    public String createMapping(Map map);

    /**
     * 向索引中添加数据
     * @param index
     * @param map
     * @return
     */
    public String insert(String index, Map map);

    /**
     * 向索引中添加数据
     * @param index
     * @param type
     * @param map
     * @return
     */
    public String insert(String index, String type, Map map);

    /**
     * 删除索引
     * @param index
     * @return
     */
    public String delete(String index);

    /**
     * 删除索引
     * @param index
     * @param type
     * @return
     */
    public String delete(String index, String type);

    /**
     * 更新索引
     * @param index
     * @param map
     * @return
     */
    public String update(String index, Map map);

    /**
     * 更新索引
     * @param index
     * @param type
     * @param map
     * @return
     */
    public String update(String index, String type, Map map);

    /**
     * restjson方式查询
     * @param json
     * @return
     */
    public String queryByJson(String json);

    /**
     * sql插件方式查询
     * @param sql
     * @return
     */
    public String queryBySql(String sql);

}
