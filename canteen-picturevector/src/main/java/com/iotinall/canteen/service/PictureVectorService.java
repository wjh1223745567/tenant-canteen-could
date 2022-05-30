package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.dto.VectorComputDto;
import com.iotinall.canteen.dto.VectorResp;
import com.iotinall.canteen.entity.PictureVector;
import com.iotinall.canteen.dto.picturevector.FeignVectorReq;
import com.iotinall.canteen.repository.PictureVectorRepository;
import com.iotinall.canteen.utils.SimilarityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RefreshScope
public class PictureVectorService {

    @Resource
    private PictureVectorRepository pictureVectorRepository;

    @Resource
    public CloseableHttpClient httpClient;

    /**
     * 缓存词向量实时计算，key 对应group
     */
    public static final Map<String, List<VectorComputDto>> vectorMap = Collections.synchronizedMap(new HashMap<>());

    @Value("${vector.url}")
    private String vectorSys;

    @PostConstruct
    public void init() {
        List<PictureVector> pictureVectors = pictureVectorRepository.findAll();
        for (PictureVector pictureVector : pictureVectors) {
            List<Double> doubles = JSON.parseArray(pictureVector.getVectorValue(), Double.class);
            Double[] doubleValue = new Double[doubles.size()];
            doubles.toArray(doubleValue);
            updateVector(pictureVector.getGroupName(), pictureVector.getDataId(), doubleValue);
        }
    }

    /**
     * 保存并缓存向量
     *
     * @param req
     */
    public void saveVector(FeignVectorReq req) {
        VectorResp resp = getVectorValue(req.getBase64());
        PictureVector pictureVectors = pictureVectorRepository.findDataId(req.getDataId());
        if (pictureVectors == null) {
            pictureVectors = new PictureVector();
        }
        pictureVectors.setDataId(req.getDataId())
                .setGroupName(req.getGroup())
                .setVectorValue(Arrays.toString(resp.getVector()));
        this.pictureVectorRepository.save(pictureVectors);
        List<Double> doubles = JSON.parseArray(pictureVectors.getVectorValue(), Double.class);
        Double[] doubleValue = new Double[doubles.size()];
        doubles.toArray(doubleValue);
        updateVector(pictureVectors.getGroupName(), pictureVectors.getDataId(), doubleValue);
    }

    public String searchMap(String base64, String group) {
        VectorResp vectorResp = getVectorValue(base64);
        if (!vectorMap.containsKey(group)) {
            throw new BizException("", group + "不存在分组");
        } else {
            List<VectorComputDto> vectorComputDtos = vectorMap.get(group);
            for (VectorComputDto vectorComputDto : vectorComputDtos) {
                if (SimilarityUtil.cosine(Arrays.asList(vectorResp.getVector()), Arrays.asList(vectorComputDto.getVector()))) {
                    return vectorComputDto.getDataId();
                }
            }
        }
        return null;
    }

    /**
     * 更新缓存
     *
     * @param group
     * @param dataId
     * @param vector
     */
    private synchronized void updateVector(String group, String dataId, Double[] vector) {
        if (vectorMap.containsKey(group)) {
            List<VectorComputDto> vectorComputDtos = vectorMap.get(group);
            Optional<VectorComputDto> optionalVectorComputDto = vectorComputDtos.stream().filter(item -> Objects.equals(item.getDataId(), dataId)).findAny();
            if (optionalVectorComputDto.isPresent()) {
                VectorComputDto vectorComputDto = optionalVectorComputDto.get();
                vectorComputDto.setVector(vector);
            } else {
                VectorComputDto vectorComputDto = new VectorComputDto();
                vectorComputDto.setDataId(dataId)
                        .setVector(vector);
                vectorComputDtos.add(vectorComputDto);
            }
        } else {
            List<VectorComputDto> vectorComputDtos = Collections.synchronizedList(new ArrayList<>());
            VectorComputDto vectorComputDto = new VectorComputDto();
            vectorComputDto.setDataId(dataId)
                    .setVector(vector);
            vectorComputDtos.add(vectorComputDto);
            vectorMap.put(group, vectorComputDtos);
        }
    }

    /**
     * 获取向量并添加修改数据
     *
     * @return
     */
    private VectorResp getVectorValue(String base64) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("img", base64);
            HttpUriRequest request = RequestBuilder.post(vectorSys)
                    .setEntity(new StringEntity(JSON.toJSONString(map)))
                    .build();
            CloseableHttpResponse response = httpClient.execute(request);
            if (Objects.equals(response.getStatusLine().getStatusCode(), 200)) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                VectorResp vectorResp = JSON.parseObject(json, new TypeReference<VectorResp>() {
                });
                if (vectorResp != null && Objects.equals(vectorResp.getResult(), 1)) {
                    return vectorResp;
                } else if (vectorResp != null) {
                    throw new BizException("", vectorResp.getInfo());
                } else {
                    throw new BizException("", "获取向量返回参数出错");
                }
            } else {
                throw new BizException("", "获取图片向量接口出错");
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new BizException("", "获取图片向量失败");
        }
    }
}
