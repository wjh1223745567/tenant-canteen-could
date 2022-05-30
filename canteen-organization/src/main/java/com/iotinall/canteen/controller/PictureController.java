package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.common.util.FileHandler;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.service.OrgEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传
 *
 * @author xin-bing
 * @date 10/28/2019 11:14
 */
@Api(tags = "图片接口")
@RestController
@RequestMapping(value = "picture")
@Slf4j
public class PictureController {

    @Resource
    private FileHandler fileHandler;
    @Resource
    private OrgEmployeeService employeeService;

    /**
     * 统一上传接口
     *
     * @return
     */
    @ApiOperation(value = "图片上传")
    @PostMapping(value = "unified-upload")
    public ResultDTO<String> UnifiedUploading(@RequestParam(value = "file") @ApiParam(required = true, value = "文件") MultipartFile multipartFile) {
        String filePath = ImgPair.getFileServer() + fileHandler.saveFile("group1", multipartFile);
        return ResultDTO.success(filePath);
    }

    @ApiOperation(value = "上传头像接口")
    @PostMapping(value = "emp-img/upload")
    private ResultDTO<Map<String, String>> handleFaceImg(@RequestParam("file") MultipartFile multipartFile) {
        try {
            String filePath = fileHandler.saveFile("group1", multipartFile);
            Map<String, String> result = new HashMap<>();
            result.put("url", ImgPair.getFileServer() + filePath);
            return ResultDTO.success(result);
        } catch (Exception e) {
            throw new RuntimeException("getFaceCode err", e);
        }
    }

    @ApiOperation(value = "上传人员头像", notes = "上传人员头像", httpMethod = "POST")
    @PostMapping(value = "emp/photo")
    public ResultDTO uploadEmpPhoto(@RequestParam(value = "file") @ApiParam(required = true, value = "文件") MultipartFile file) {
        return ResultDTO.success(employeeService.uploadEmpPhoto(file));
    }
}