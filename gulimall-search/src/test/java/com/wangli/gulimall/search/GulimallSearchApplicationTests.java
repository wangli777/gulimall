package com.wangli.gulimall.search;

import cn.hutool.json.JSONUtil;
import com.wangli.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class GulimallSearchApplicationTests {

    @Autowired
    RestHighLevelClient highLevelClient;

    @Test
    public void contextLoads() {
        System.out.println(highLevelClient);
    }

    @Test
    public void searchData() throws IOException {
        //创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        //指定index
        searchRequest.indices("bank");
        //指定DSL检索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //1.1 构建检索条件 address 包含 mill
        searchSourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));

        //进行聚合
        //1.2按照年龄的值分布进行聚合
        searchSourceBuilder.aggregation(AggregationBuilders.terms("ageAgg").field("age").size(10));
        //1.3计算平均年龄
        searchSourceBuilder.aggregation(AggregationBuilders.avg("ageAvg").field("age"));
        //1.4计算平均薪水
        searchSourceBuilder.aggregation(AggregationBuilders.avg("balanceAvg").field("balance"));
        searchRequest.source(searchSourceBuilder);

        log.info("检索条件：{}", searchSourceBuilder.toString());
        // 2. 执行检索, 获得响应
        SearchResponse searchResponse = highLevelClient.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        log.info(searchResponse.toString());

        // 3. 分析结果
        // 3.1 获取所有查到的记录
        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits) {
            String sourceAsString = hit.getSourceAsString();
            Accout accout = JSONUtil.toBean(sourceAsString, Accout.class);
            String lastname = accout.getLastname();
            log.info("lastName：{}", lastname);
        }
        // 3.2 获取检索的分析信息(聚合数据等)
        Aggregations aggregations = searchResponse.getAggregations();
        //获取 按照年龄的值分布进行聚合 的结果
        Terms ageAgg = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAgg.getBuckets()) {
            log.info("年龄为{}岁的有{}人", bucket.getKeyAsString(), bucket.getDocCount());
        }
        //获取 平均年龄
        Avg ageAvg = aggregations.get("ageAvg");
        log.info("平均年龄为{}岁", ageAvg.getValue());

        Avg balanceAvg = aggregations.get("balanceAvg");
        log.info("平均薪资为{}", balanceAvg.getValue());
    }

    @Data
    static class Accout {

        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;
    }

    @Test
    public void indexData() throws IOException {
        //指定数据index为users
        IndexRequest request = new IndexRequest("users");
        //指定id为1
        request.id("1");
        User user = new User();
        user.setName("wangli");
        user.setAge(23);

        //设置数据，指定数据格式为JSON
        request.source(JSONUtil.toJsonStr(user), XContentType.JSON);

        //存储数据
        IndexResponse response = highLevelClient.index(request, GulimallElasticSearchConfig.COMMON_OPTIONS);

        log.info(JSONUtil.toJsonStr(response));

    }

    @Data
    static class User {
        private String name;
        private Integer age;
    }
}
