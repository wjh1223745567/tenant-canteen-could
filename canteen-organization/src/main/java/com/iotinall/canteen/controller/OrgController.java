package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.org.OrgAddReq;
import com.iotinall.canteen.dto.org.OrgEditReq;
import com.iotinall.canteen.dto.org.OrgTreeDTO;
import com.iotinall.canteen.service.OrgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

/**
 * 组织 Controller
 *
 * @author xin-bing
 * @date 2019-10-24 13:55:41
 */
@Api(tags = SwaggerModule.MODULE_ORG)
@RestController
@RequestMapping("orgs")
public class OrgController {

    @Resource
    private OrgService orgService;

    /**
     * 新增组织
     *
     * @author xin-bing
     * @date 2019-10-24 13:55:41
     */
    @ApiOperation(value = "新增组织", notes = "新增组织")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ALL','ORG_ADD')")
    public ResultDTO create(@Valid @RequestBody OrgAddReq req) {
        return ResultDTO.success(orgService.add(req));
    }

    /**
     * 修改组织
     *
     * @author xin-bing
     * @date 2019-10-24 13:55:41
     */
    @ApiOperation(value = "修改组织", notes = "修改组织")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ALL','ORG_EDIT')")
    public ResultDTO update(@Valid @RequestBody OrgEditReq req) {
        return ResultDTO.success(orgService.update(req));
    }

    /**
     * 删除组织
     *
     * @author xin-bing
     * @date 2019-10-24 13:55:41
     */
    @ApiOperation(value = "删除组织", notes = "删除组织")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ALL','ORG_DELETE')")
    public ResultDTO delete(@ApiParam(name = "id", value = "需要删除的id", required = true) @PathVariable Long id) {
        return ResultDTO.success(orgService.delete(id));
    }

    /**
     * 查询树形结构所有数据
     *
     * @return
     */
    @GetMapping(value = "findTreeData")
    public ResultDTO<List<OrgTreeDTO>> findTreeData() {
        return ResultDTO.success(this.orgService.findTree());
    }

    /**
     * 获取用户组织机构树
     *
     * @return
     */
    @GetMapping(value = "findUserTree")
    public ResultDTO findUserTreeData() {
        return ResultDTO.success(orgService.findUserTree());
    }

    /**
     * 刷数据用
     *
     * @return
     */
    @GetMapping(value = "updateWallet")
    public ResultDTO updateWallet() {
        this.orgService.updateWallet();
        return ResultDTO.success();
    }

    /**
     * 组织机构树，包括下级的组织机构和组织机构下面的人（不包括子组织机构下面的人）
     *
     * @author loki
     * @date 2020/11/02 18:29
     */
    @GetMapping("tree")
    public ResultDTO getOrgTree(@ApiParam(value = "选择的组织机构") @RequestParam(value = "pid", required = false) Long pid, @ApiParam(value = "查询关键字") @RequestParam(value = "keyword", required = false) String keyword) {
        return ResultDTO.success(this.orgService.getOrgTree(pid, keyword));
    }

    @PostMapping(value = "feign/getAllChildOrg")
    public Set<Long> getAllChildOrg(@RequestBody List<Long> orgIds){
        return this.orgService.getAllChildOrg(orgIds);
    }
}