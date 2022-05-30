package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.dto.menubrief.AllSearchDto;
import com.iotinall.canteen.dto.menubrief.MenuBriefDto;
import com.iotinall.canteen.dto.menubrief.SearchDto;
import com.iotinall.canteen.entity.MessProduct;
import com.iotinall.canteen.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RefreshScope
public class BaiduMenuApi {

    /**
     * 基础路径
     */
    private final String baseUrl = "https://aip.baidubce.com/rest/2.0/image-classify/v1/realtime_search/dish/";

    private final String addMenu = "add";

    private final String searchMenu = "search";

    private final String deleteMenu = "delete";

    /**
     * 所有菜谱
     */
    private final String allMenuUrl = "https://aip.baidubce.com/rest/2.0/image-classify/v2/dish";

    /**
     * redis百度token
     */
    private final String baiduauthtoken = "baiduauthtokentest";

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    protected static CloseableHttpClient httpClient = HttpClients.custom()
            .setMaxConnTotal(100)
            .setDefaultRequestConfig(RequestConfig.custom().setConnectionRequestTimeout(2000).setConnectTimeout(2000).setSocketTimeout(2000).build()).build();

    /**
     * 获取token
     */
    private final String tokenUrl = "https://aip.baidubce.com/oauth/2.0/token";

    @Value("${baidupic.appkey:null}")
    private String appkey;

    @Value("${baidupic.secretkey:null}")
    private String secretkey;

    /**
     * 获取缓存token
     * @return
     */
    private String getTokenToRedis(){
        String token = this.redisTemplate.opsForValue().get(baiduauthtoken);
        if(StringUtils.isBlank(token)){
            String nowKey = this.getAuth();
            if(StringUtils.isNotBlank(nowKey)){
                this.redisTemplate.opsForValue().set(baiduauthtoken, nowKey, 25, TimeUnit.DAYS);
            }else{
                throw new BizException("", "获取百度token鉴权失败");
            }
            return nowKey;
        }
        return token;
    }

    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public void dishAdd(MessProduct messProduct) {
        // 请求url
        String url = baseUrl + addMenu;
        if(StringUtils.isNotBlank(messProduct.getImg())){
            String base64 = FileUtil.getBase64FromUrl(ImgPair.getFileServer() + messProduct.getImg());

            MenuBriefDto briefDto = new MenuBriefDto()
                    .setId(messProduct.getId())
                    .setName(messProduct.getName())
                    .setMealType(messProduct.getCatalog());

            List<NameValuePair> content = new ArrayList<>();
            NameValuePair image = new BasicNameValuePair("image", base64);
            NameValuePair brief = new BasicNameValuePair("brief", JSON.toJSONString(briefDto));
            content.add(image);
            content.add(brief);

            try {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(content,"UTF-8");

                HttpUriRequest request = RequestBuilder.post()
                        .setUri(url)
                        .addParameter("access_token", this.getTokenToRedis())
                        .setHeader("Content-Type", "application/x-www-form-urlencoded")
                        .setEntity(entity).build();

                HttpResponse httpResponse = httpClient.execute(request);
                if (httpResponse.getEntity() != null) {
                    String res = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                    log.info("添加百度自定义菜谱：{}", res);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public SearchDto dishSearch(String base64){
        String url = baseUrl + searchMenu;

        List<NameValuePair> content = new ArrayList<>();
        NameValuePair image = new BasicNameValuePair("image", base64);
        content.add(image);

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(content,"UTF-8");

            HttpUriRequest request = RequestBuilder.post()
                    .setUri(url)
                    .addParameter("access_token", this.getTokenToRedis())
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .setEntity(entity).build();

            HttpResponse httpResponse = httpClient.execute(request);
            if (httpResponse.getEntity() != null) {
                String res = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                log.info("搜索百度自定义菜谱：{}", res);
                return JSON.parseObject(res, SearchDto.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public AllSearchDto dishSearchAll(String base64){
        List<NameValuePair> content = new ArrayList<>();
        NameValuePair image = new BasicNameValuePair("image", base64);
        content.add(image);

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(content,"UTF-8");

            HttpUriRequest request = RequestBuilder.post()
                    .setUri(allMenuUrl)
                    .addParameter("access_token", this.getTokenToRedis())
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .setEntity(entity).build();

            HttpResponse httpResponse = httpClient.execute(request);
            if (httpResponse.getEntity() != null) {
                String res = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                log.info("搜索所有自定义菜谱：{}", res);
                return JSON.parseObject(res, AllSearchDto.class);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除
     * @param messProduct
     */
    public void dishDelete(MessProduct messProduct){

        String base64 = FileUtil.getBase64FromUrl(ImgPair.getFileServer() + messProduct.getImg());

        String url = baseUrl + deleteMenu;

        List<NameValuePair> content = new ArrayList<>();
        NameValuePair image = new BasicNameValuePair("image", base64);
        content.add(image);

        try {

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(content,"UTF-8");

            HttpUriRequest request = RequestBuilder.post()
                    .setUri(url)
                    .addParameter("access_token", this.getTokenToRedis())
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .setEntity(entity).build();

            HttpResponse httpResponse = httpClient.execute(request);
            if (httpResponse.getEntity() != null) {
                String res = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                log.info("删除百度自定义菜谱：{}", res);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取token
     * @return
     */
    public String getAuth() {
        return getAuth(appkey, secretkey);
    }

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    private String getAuth(String ak, String sk) {
        // 获取token地址
        String authHost = tokenUrl + "?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.err.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
            System.err.println("result:" + result);
            JSONObject jsonObject = new JSONObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            log.info("获取百度token失败");
            e.printStackTrace(System.err);
        }
        return null;
    }


}
