package com.wangli.gulimall.thirdparty.service;

import cn.hutool.json.JSONUtil;
import com.wangli.common.utils.HttpUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author WangLi
 * @date 2021/7/17 14:54
 * @description 短信服务
 */
@ConfigurationProperties(prefix = "spring.cloud.alicloud.sms")
@Service
@Data
@Slf4j
public class SmsService {
    private String host;
    private String path;
    private String appcode;

    public void sendSms(String mobile, String code) {
//        String host = "https://gyytz.market.alicloudapi.com";
//        String path = "/sms/smsSend";
        String method = "POST";
//        String appcode = appcode;
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<>();
        querys.put("mobile", mobile);
        querys.put("param", "**code**:" + code + ",**minute**:5");
        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, String> bodys = new HashMap<>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            log.debug("发送短信后响应：{},=== {}", EntityUtils.toString(response.getEntity()), JSONUtil.toJsonStr(response.getStatusLine()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
