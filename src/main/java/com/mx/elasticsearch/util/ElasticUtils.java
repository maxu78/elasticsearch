package com.mx.elasticsearch.util;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.Delete;
import io.searchbox.core.Index;

import io.searchbox.core.Search;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.mapping.GetMapping;
import io.searchbox.indices.mapping.PutMapping;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.settings.Settings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 执行elastic rest工具
 * jest部分参考 https://github.com/searchbox-io/Jest/tree/master/jest
 */
@Slf4j
@Component
public class ElasticUtils {

    private static String scheme;

    private static String url;

    private static String port;

    /**
     * 获取ES restClient
     * @return
     */
    public static JestClient getJestClient() {
        // Construct a new Jest client according to configuration via factory
        JestClientFactory factory = new JestClientFactory();
        List<String> serverUris = new ArrayList<String>();
        //serverUris.add(scheme + "://"  + url + ":" + port);
        serverUris.add("http://127.0.0.1:10200");
        System.out.println(scheme + "://"  + url + ":" + port);
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder(serverUris)
                .multiThreaded(true)
                .build());
        JestClient client = factory.getObject();
        return client;
    }

    /**
     * 创建索引
     * @param indexName
     * @return
     * @throws IOException
     */
    public static JestResult createIndex(String indexName) throws IOException {
        JestResult result = getJestClient().execute(new CreateIndex.Builder(indexName).build());
        return result;
    }

    /**
     * 创建索引
     * @param indexName
     * @param settings
     * @return
     * @throws IOException
     */
    public static JestResult createIndex(String indexName, String settings) throws IOException {
        JestResult result = getJestClient().execute(new CreateIndex.Builder(indexName).settings(Settings.builder().loadFromSource(settings).build().getAsMap()).build());
        return result;
    }

    /**
     * 创建mapping
     * @param indexName
     * @param type
     * @param source
     * @return
     * @throws IOException
     */
    public static JestResult createMapping(String indexName, String type, String source) throws IOException {
        PutMapping putMapping = new PutMapping.Builder(indexName, type, source).build();
        JestResult result = getJestClient().execute(putMapping);
        return result;
    }

    /**
     * 插入一条数据
     * @param indexName
     * @param type
     * @param source
     * @return
     * @throws IOException
     */
    public static JestResult insert(String indexName, String type, String source) throws IOException {
        Index index = new Index.Builder(source).index(indexName).type(type).build();
        JestResult result = getJestClient().execute(index);
        return result;
    }

    /**
     * 插入一条数据
     * @param indexName
     * @param type
     * @param id
     * @param source
     * @return
     * @throws IOException
     */
    public static JestResult insert(String indexName, String type, String id, String source) throws IOException {
        Index index = new Index.Builder(source).index(indexName).type(type).id(id).build();
        JestResult result = getJestClient().execute(index);
        return result;
    }

    /**
     * 查询
     * @param indexName
     * @param type
     * @param source
     * @return
     * @throws IOException
     */
    public static JestResult search(String indexName, String type, String source) throws IOException {
        Search search = new Search.Builder(source).addIndex(indexName).addType(type).build();
        JestResult result = getJestClient().execute(search);
        return result;
    }

    /**
     * 查询
     * @param indexName
     * @param source
     * @return
     * @throws IOException
     */
    public static JestResult search(String indexName, String source) throws IOException {
        Search search = new Search.Builder(source).addIndex(indexName).build();
        JestResult result = getJestClient().execute(search);
        return result;
    }


    /**
     * 批量插入
     * @param indexName
     * @param typeName
     * @param objs
     * @return
     * @throws Exception
     */
    public static JestResult bulk(String indexName, String typeName, List<Object> objs) throws Exception {

        Bulk.Builder bulk = new Bulk.Builder().defaultIndex(indexName).defaultType(typeName);
        for (Object obj : objs) {
            Index index = new Index.Builder(obj).build();
            bulk.addAction(index);
        }
        JestResult result = getJestClient().execute(bulk.build());
        return result;
    }

    /**
     * 删除
     * @param indexName
     * @param type
     * @param id
     * @return
     * @throws IOException
     */
    public static JestResult delete(String indexName, String type, String id) throws IOException {
        JestResult result = getJestClient().execute(new Delete.Builder(id).index(indexName).type(type).build());
        return result;
    }

    /**
     * Delete索引
     * @param jestClient
     * @param indexName
     * @return
     * @throws Exception
     */
    public static JestResult delete(JestClient jestClient, String indexName) throws Exception {
        JestResult result = jestClient.execute(new DeleteIndex.Builder(indexName).build());
        return result;
    }

    /**
     * Put映射
     * @param indexName
     * @param typeName
     * @param source
     * @return
     * @throws Exception
     */
    public static JestResult createIndexMapping(String indexName, String typeName, String source) throws Exception {
        PutMapping putMapping = new PutMapping.Builder(indexName, typeName, source).build();
        JestResult result = getJestClient().execute(putMapping);
        return result;
    }

    /**
     * Get映射
     * @param indexName
     * @param typeName
     * @return
     * @throws Exception
     */
    public static JestResult getIndexMapping(String indexName, String typeName) throws Exception {
        GetMapping getMapping = new GetMapping.Builder().addIndex(indexName).addType(typeName).build();
        JestResult result = getJestClient().execute(getMapping);
        return result;
    }

    /**
     * 向指定URL发送GET方法的请求
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            java.net.HttpURLConnection connection = (java.net.HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {}
            // 定义 BufferedReader输入流来读取URL的响应-设置编码
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定URL发送方法的请求
     * @param url 发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义 BufferedReader输入流来读取URL的响应-设置编码
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    @Value("${elasticsearch.scheme: http}")
    public void setScheme(String scheme) {
       this.scheme = scheme;
    }

    @Value("${elasticsearch.url: 127.0.0.1}")
    public void setUrl(String url) {
        this.url = url;
    }

    @Value("${elasticsearch.port: 9200}")
    public void setPort(String port) {
        this.port = port;
    }
}
