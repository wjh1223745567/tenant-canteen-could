package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.messfeedback.MessFeedbackTypeReq;
import com.iotinall.canteen.dto.stature.StatureDTO;
import com.iotinall.canteen.dto.sysdictdetail.SysDictDetailAddReq;
import com.iotinall.canteen.dto.sysdictdetail.SysDictDetailDTO;
import com.iotinall.canteen.dto.sysdictdetail.SysDictDetailEditReq;
import com.iotinall.canteen.service.SysDictDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WJH
 * @date 2019/11/1416:30
 */
@Api(value = "字典接口")
@RestController
@RequestMapping(value = "app_dict")
public class AppDictDetailsController {

    @Resource
    private SysDictDetailService sysDictDetailService;

    final static String TASTE_TAG = "taste_tag";

    final static String ENV_TAG = "env_tag";

    /**
     * 获取反馈类型
     * @author xin-bing
     * @date 2019-10-26 17:23:08
     */
    @ApiOperation(value = "反馈类型", notes = "查看反馈类型")
    @GetMapping("types")
    @PreAuthorize("hasAnyRole('ADMIN','MESSDICT_ALL','MESSDICT_TYPE_LIST')")
    public ResultDTO listDict() {
        List<SysDictDetailDTO> env = sysDictDetailService.listAll(TASTE_TAG);
        List<SysDictDetailDTO> tast = sysDictDetailService.listAll(ENV_TAG);
        Map<String, List<SysDictDetailDTO>> map = new HashMap<>();
        map.put("envir", env);
        map.put("taste", tast);
        return ResultDTO.success(map);
    }

    /**
     * 添加反馈类型
     * @author xin-bing
     * @date 2019-10-26 17:23:08
     */
    @ApiOperation(value = "新增反馈类型", notes = "新增反馈类型")
    @PostMapping("types")
    @PreAuthorize("hasAnyRole('ADMIN','MESSDICT_ALL','MESSDICT_TYPE_ADD')")
    public ResultDTO addDict(@Valid @RequestBody MessFeedbackTypeReq.AddReq req) {
        SysDictDetailAddReq dictReq = new SysDictDetailAddReq();
        dictReq.setLabel(req.getLabel());
        dictReq.setValue(req.getLabel());
        dictReq.setSort(0);
//        dictReq.setGroupCode(req.getGroupCode()); //FIXME
        dictReq.setRemark(req.getRemark());
        sysDictDetailService.add(dictReq);
        return ResultDTO.success();
    }

    /**
     * 编辑反馈类型
     * @author xin-bing
     * @date 2019-10-26 17:23:08
     */
    @ApiOperation(value = "编辑反馈类型", notes = "编辑反馈类型")
    @PutMapping("types")
    @PreAuthorize("hasAnyRole('ADMIN','MESSDICT_ALL','MESSDICT_TYPE_EDIT')")
    public ResultDTO editDict(@Valid @RequestBody MessFeedbackTypeReq.EditReq req) {
        SysDictDetailEditReq dictReq = new SysDictDetailEditReq();
        dictReq.setId(req.getId());
        dictReq.setLabel(req.getLabel());
        dictReq.setValue(req.getLabel());
        dictReq.setSort(0);
        dictReq.setRemark(req.getRemark());
        sysDictDetailService.update(dictReq);
        return ResultDTO.success();
    }

    /**
     * 删除反馈类型
     * @author xin-bing
     * @date 2019-10-26 17:23:08
     */
    @ApiOperation(value = "删除反馈类型", notes = "删除反馈类型")
    @DeleteMapping("types/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MESSDICT_ALL','MESSDICT_TYPE_DELETE')")
    public ResultDTO deleteDict(@ApiParam(name = "id", value = "需要删除的id", required = true)@PathVariable Long id) {
        sysDictDetailService.delete(id);
        return ResultDTO.success();
    }

    /**
     * 查询所有类型
     * @return
     */
    @GetMapping(value = "listAllApp")
    public ResultDTO listAllApp() {
        List<SysDictDetailDTO> env = sysDictDetailService.listAll(TASTE_TAG);
        List<SysDictDetailDTO> tast = sysDictDetailService.listAll(ENV_TAG);
        env.addAll(tast);
        return ResultDTO.success(env);
    }


    @ApiOperation(value = "查询身材选择项", response = StatureDTO.class)
    @GetMapping("options")
    public ResultDTO get() {
        return ResultDTO.success(sysDictDetailService.findOptions());
    }

}
