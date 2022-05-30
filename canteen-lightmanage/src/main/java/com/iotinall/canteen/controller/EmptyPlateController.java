package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.emptyplate.EmptyPlateAlarmReq;
import com.iotinall.canteen.dto.emptyplate.EmptyPlateRecordQueryCriteria;
import com.iotinall.canteen.service.EmptyPlateService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 光盘行动请求处理类
 *
 * @author loki
 * @date 2021/02/05 16:22
 */
@Slf4j
@RestController
@RequestMapping(value = "empty-plate")
public class EmptyPlateController {
    @Resource
    private EmptyPlateService emptyPlateService;

    /**
     * 光盘行动
     * 检测到浪费粮食报警
     *
     * @author loki
     * @date 2021/02/05 16:23
     */
    @PostMapping
    public void alarm(@RequestBody EmptyPlateAlarmReq req) {
        if (StringUtils.isBlank(req.getBase64_img())) {
            log.info("光盘行动，图片为空");
            return;
        }

        this.emptyPlateService.alarm(req);
    }

    /**
     * 分析光盘行动违规记录
     *
     * @author loki
     * @date 2021/7/6 14:45
     **/
    @PostMapping("analysis")
    public ResultDTO analysis() {
        this.emptyPlateService.analysis();
        return ResultDTO.success();
    }
}
