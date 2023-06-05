package com.fool.mirai.console.openai;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fool.mirai.console.Data;
import com.fool.mirai.console.YmcPlugin;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class OpenAi {

    public static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        OBJECT_MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    }

    public static ChatCompletionResponse chatCompletion(ChatCompletionParameters parameters) {
        HttpPost httpPost = new HttpPost(Data.INSTANCE.getApi());
        httpPost.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + Data.INSTANCE.getApiKey());

        RequestConfig.Builder builder = RequestConfig
                .custom()
                .setConnectTimeout(60000)
                .setSocketTimeout(60000)
                .setConnectionRequestTimeout(60000);

        if (!Data.INSTANCE.getProxy().isBlank()) {
            HttpHost httpHost = new HttpHost(Data.INSTANCE.getProxy(), Data.INSTANCE.getPort());
            builder.setProxy(httpHost);
        }

        RequestConfig defaultRequestConfig = builder.build();
        httpPost.setConfig(defaultRequestConfig);

        String requestParameters;

        try {
            requestParameters = OBJECT_MAPPER.writeValueAsString(parameters);
        } catch (JsonProcessingException e) {
            YmcPlugin.INSTANCE.getLogger().error("请求参数转JSON异常！");
            return null;
        }
        YmcPlugin.INSTANCE.getLogger().info(String.format("OpenAi请求参数：%s", requestParameters));

        StringEntity requestEntity = new StringEntity(requestParameters, ContentType.APPLICATION_JSON);
        httpPost.setEntity(requestEntity);

        try (CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse execute = httpClient.execute(httpPost);) {
            if (execute.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                return null;
            }
            HttpEntity entity = execute.getEntity();
            String responseString = EntityUtils.toString(entity);
            YmcPlugin.INSTANCE.getLogger().info(String.format("请求OpenAi响应：%s", responseString));
            return OBJECT_MAPPER.readValue(responseString, ChatCompletionResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
