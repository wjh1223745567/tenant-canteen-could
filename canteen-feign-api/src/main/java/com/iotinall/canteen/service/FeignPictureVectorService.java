package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.picturevector.FeignVectorReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "canteen-picturevector", contextId = "picture-vector")
public interface FeignPictureVectorService {

    /**
     * 推送缓存图片向量
     * @param req
     */
    @PostMapping(value = "/picture_vector/feign/saveVector")
    void saveVector(@RequestBody FeignVectorReq req);

}
