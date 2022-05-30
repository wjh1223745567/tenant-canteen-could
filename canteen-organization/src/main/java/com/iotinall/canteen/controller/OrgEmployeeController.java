package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.constant.SwaggerModule;
import com.iotinall.canteen.dto.employee.KitchenCookDTO;
import com.iotinall.canteen.dto.organization.FeignCookDto;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.organization.FeignMessProductCookView;
import com.iotinall.canteen.dto.organization.FeignSimEmployeeDto;
import com.iotinall.canteen.dto.orgemployee.*;
import com.iotinall.canteen.service.ExcelService;
import com.iotinall.canteen.service.OrgEmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 组织员工 Controller
 *
 * @author xin-bing
 * @date 2019-10-24 13:55:41
 */
@Api(tags = SwaggerModule.MODULE_ORG)
@RestController
@RequestMapping("org-employees")
public class OrgEmployeeController {
    @Resource
    private OrgEmployeeService orgEmployeeService;
    @Resource
    private ExcelService excelService;

    /**
     * 查询组织员工列表
     *
     * @author xin-bing
     * @date 2019-10-24 13:55:41
     */
    @ApiOperation(value = "列表查询", notes = "根据条件查询组织员工列表")
    @GetMapping
    public ResultDTO<PageDTO<OrgEmployeeDTO>> list(OrgEmployeeQueryCriteria criteria, Pageable pageable) {
        return ResultDTO.success(orgEmployeeService.pageOrgEmployee(criteria, pageable));
    }

    /**
     * 查询组织员工列表
     *
     * @author xin-bing
     * @date 2019-10-24 13:55:41
     */
    @ApiOperation(value = "根据类型获取员工列表", notes = "根据类型获取员工列表")
    @GetMapping("type")
    public ResultDTO getByType(@RequestParam("type") Integer type) {
        return ResultDTO.success(orgEmployeeService.findAllByType(type, false));
    }

    /**
     * 查询组织员工列表
     *
     * @author xin-bing
     * @date 2019-10-24 13:55:41
     */
    @ApiOperation(value = "获取所有的厨师", notes = "获取所有的厨师")
    @GetMapping("cook")
    public ResultDTO getAllCook(@RequestParam(value = "keywords", required = false) String keywords,
                                @RequestParam(value = "type", required = false) Integer type) {
        return ResultDTO.success(orgEmployeeService.findAllCook(keywords, type));
    }

    /**
     * 花名册
     *
     * @author xin-bing
     * @date 2019-10-24 13:55:41
     */
    @ApiOperation(value = "获取所有花名册", notes = "获取所有花名册")
    @GetMapping("roster")
    public ResultDTO getRoster(@RequestParam(value = "keywords", required = false) String keywords, Pageable pageable) {
        return ResultDTO.success(orgEmployeeService.findBackKitchenPage(keywords, pageable));
    }

    /**
     * 员工关怀
     *
     * @author xin-bing
     * @date 2019-10-24 13:55:41
     */
    @ApiOperation(value = "员工关怀", notes = "员工关怀")
    @GetMapping("care")
    public ResultDTO getCareList(@RequestParam(value = "month") LocalDate month, Pageable pageable) {
        return ResultDTO.success(orgEmployeeService.findBackKitchenCare(month, pageable));
    }

    /**
     * 员工入职周年关怀
     *
     * @author xin-bing
     * @date 2019-10-24 13:55:41
     */
    @ApiOperation(value = "员工关怀", notes = "员工关怀")
    @GetMapping("anniversary")
    public ResultDTO getAnniversaryList(@RequestParam(value = "month") LocalDate month, Pageable pageable) {
        return ResultDTO.success(orgEmployeeService.findBackKitchenAnniversary(month, pageable));
    }

    /**
     * 组织员工详情
     *
     * @author xin-bing
     * @date 2019-10-24 13:55:41
     */
    @ApiOperation(value = "详情", notes = "组织员工详情")
    @GetMapping(value = "/{id}")
    public ResultDTO<OrgEmployeeDTO> detail(@ApiParam(name = "id", value = "id", required = true) @PathVariable Long id) {
        return ResultDTO.success(orgEmployeeService.detail(id));
    }

    /**
     * 新增组织员工
     *
     * @author xin-bing
     * @date 2019-10-24 13:55:41
     */
    @ApiOperation(value = "新增", notes = "新增组织员工")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ALL', 'ORG_EMP_ADD')")
    public ResultDTO<?> create(@Valid @RequestBody OrgEmployeeAddReq req) {
        return ResultDTO.success(orgEmployeeService.add(req));
    }

    /**
     * excel导入功能
     *
     * @param file
     * @return
     */
    @ApiOperation(value = "excel导入", notes = "excel导入")
    @PostMapping(value = "excel")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ALL', 'ORG_EMP_ADD')")
    public ResultDTO<?> excel(@RequestParam(name = "file") MultipartFile file) {
        this.excelService.addMemberFile(file);
        return ResultDTO.success();
    }

    /**
     * 修改组织员工
     *
     * @author xin-bing
     * @date 2019-10-24 13:55:41
     */
    @ApiOperation(value = "修改", notes = "修改组织员工")
    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ALL', 'ORG_EMP_EDIT')")
    public ResultDTO<?> update(@Valid @RequestBody OrgEmployeeEditReq req) {
        return ResultDTO.success(orgEmployeeService.update(req));
    }

    @ApiOperation(value = "修改密码", notes = "修改员工登录密码")
    @PostMapping(value = "edit-pwd")
    public ResultDTO<?> editPwd(@Valid @RequestBody OrgEmpEditPwdReq req) {
        return ResultDTO.success(orgEmployeeService.editPwd(req));
    }

    @ApiOperation(value = "重置密码", notes = "重置员工登录密码")
    @PostMapping(value = "reset-pwd")
    public ResultDTO<?> resetPwd(@Valid @RequestBody OrgEmpEditPwdReq req) {
        String defaultPwd = "666888";
        if (StringUtils.isBlank(req.getPwd())) {
            req.setPwd(defaultPwd);
        }
        return ResultDTO.success(orgEmployeeService.editPwd(req));
    }

    /**
     * 删除组织员工
     *
     * @author xin-bing
     * @date 2019-10-24 13:55:41
     */
    @ApiOperation(value = "删除", notes = "修改组织员工")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ALL', 'ORG_EMP_DELETE')")
    public ResultDTO delete(@ApiParam(name = "id", value = "需要删除的id", required = true) @PathVariable Long id) {
        return ResultDTO.success(orgEmployeeService.delete(id));
    }

    /**
     * 批量删除组织员工
     *
     * @author xin-bing
     * @date 2019-10-24 13:55:41
     */
    @ApiOperation(value = "批量删除")
    @DeleteMapping
    @PreAuthorize("hasAnyRole('ADMIN','ORG_ALL','ORG_EMP_DELETE')")
    public ResultDTO deleteBatch(@ApiParam(name = "batch", value = "需要删除的id，逗号分隔", required = true) @RequestParam(value = "batch") Long[] ids) {
        orgEmployeeService.batchDelete(ids);
        return ResultDTO.success();
    }

    @ApiOperation(value = "根据名称搜索", notes = "用户搜索下拉框")
    @GetMapping(value = "search")
    public ResultDTO<List<OrgEmployeeDTO>> search(@ApiParam(name = "name", value = "名称") String name) {
        return ResultDTO.success(orgEmployeeService.searchByName(name));
    }

    @GetMapping(value = "findByOrgs")
    public ResultDTO<List<OrgEmployeeDTO>> findByOrgs(String ids) {
        String[] strIds = ids.split(",");
        List<Long> idArray = new ArrayList<>();
        for (String id : strIds) {
            idArray.add(Long.valueOf(id));
        }
        return ResultDTO.success(orgEmployeeService.findByOrgIds(idArray));
    }

    @GetMapping(value = "convert-photo")
    public ResultDTO<?> convertPhoto() {
        this.orgEmployeeService.convertEmpPhoto();
        return ResultDTO.success();
    }


    /**
     * Feign获取员工信息
     *
     * @author loki
     * @date 2021/06/03 15:27
     */
    @GetMapping(value = "feign/{id}")
    public FeignEmployeeDTO getEmployeeInfoByFeign(@PathVariable(value = "id") Long id) {
        return orgEmployeeService.getEmployeeInfoByFeign(id);
    }

    /**
     * 查询厨师详情信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "feign/findCookView/{id}")
    public FeignMessProductCookView findCookView(@PathVariable(value = "id") Long id) {
        return orgEmployeeService.findCookView(id);
    }

    @PostMapping(value = "feign/findByIds")
    public Map<Long, FeignEmployeeDTO> findByIds(@RequestBody Set<Long> ids) {
        return orgEmployeeService.findByIds(ids);
    }

    /**
     * 查询所有厨师
     *
     * @return
     */
    @GetMapping(value = "feign/findAllCook")
    public List<FeignCookDto> findAllCook(@RequestParam(value = "type", required = false) Integer type) {
        return orgEmployeeService.findAllCook("", type);
    }

    @GetMapping(value = "feign/findSimById/{id}")
    public FeignSimEmployeeDto findSimById(@PathVariable(name = "id") Long id) {
        return orgEmployeeService.findSimById(id);
    }

    /**
     * 根据人员类型
     *
     * @param type
     * @param deleted
     * @return
     */
    @GetMapping(value = "feign/findAllByType")
    public List<FeignSimEmployeeDto> findAllByType(@RequestParam(value = "type") Integer type, @RequestParam(value = "deleted") Boolean deleted) {
        return orgEmployeeService.findAllByType(type, deleted);
    }

    @GetMapping(value = "feign/findSimAll")
    public List<FeignSimEmployeeDto> findSimAll() {
        return orgEmployeeService.findSimAll();
    }

    @GetMapping(value = "feign/findByIdNo")
    FeignSimEmployeeDto findByIdNo(@RequestParam("idNo") String idNo) {
        return orgEmployeeService.findByIdNo(idNo);
    }

    /**
     * 大屏-获取后厨人员列表
     *
     * @author loki
     * @date 2021/7/13 17:48
     **/
    @GetMapping(value = "/feign/getKitchenCookList")
    List<KitchenCookDTO> getKitchenCookList() {
        return orgEmployeeService.getKitchenCookList();
    }

    /**
     * @Author JoeLau
     * @Description 根据部门idString(逗号分隔)查总人数
     * @Date 2021/7/15  15:53
     */
    @GetMapping(value = "feign/countEmployee/{orgIdString}")
    public Integer countEmployee(@PathVariable(name = "orgIdString") String orgIdString) {
        return orgEmployeeService.countEmployee(orgIdString);
    }
}