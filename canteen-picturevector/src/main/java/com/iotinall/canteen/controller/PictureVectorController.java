package com.iotinall.canteen.controller;

import com.iotinall.canteen.dto.picturevector.FeignVectorReq;
import com.iotinall.canteen.service.PictureVectorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping(value = "picture_vector")
public class PictureVectorController {

    @Resource
    private PictureVectorService pictureVectorService;

    @PostMapping(value = "feign/saveVector")
    public void saveVector(@RequestBody FeignVectorReq req){
        pictureVectorService.saveVector(req);
    }

}
