package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.employee.KitchenCookDTO;
import com.iotinall.canteen.dto.organization.FeignCookDto;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.organization.FeignMessProductCookView;
import com.iotinall.canteen.dto.organization.FeignSimEmployeeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 员工服务调用类
 *
 * @author loki
 * @date 2021/06/03 16:36
 */
@FeignClient(value = "canteen-organization", contextId = "employee")
public interface FeignEmployeeService {

    /**
     * 通过ID获取员工信息
     *
     * @author loki
     * @date 2021/06/03 15:24
     */
    @GetMapping("org-employees/feign/{id}")
    FeignEmployeeDTO findById(@PathVariable(name = "id") Long id);

    @GetMapping(value = "org-employees/feign/findCookView/{id}")
    FeignMessProductCookView findCookView(@PathVariable(name = "id") Long id);

    @PostMapping(value = "org-employees/feign/findByIds")
    Map<Long, FeignEmployeeDTO> findByIds(@RequestBody Set<Long> ids);

    @GetMapping(value = "org-employees/feign/findAllCook")
    List<FeignCookDto> findAllCook();

    /**
     * 设备同步获取信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "org-employees/feign/findSimById/{id}")
    FeignSimEmployeeDto findSimById(@PathVariable(name = "id") Long id);

    /**
     * 根据类型获取人员信息
     *
     * @param type
     * @param deleted
     * @return
     */
    @GetMapping(value = "org-employees/feign/findAllByType")
    List<FeignSimEmployeeDto> findAllByType(@RequestParam(value = "type") Integer type, @RequestParam(value = "deleted") Boolean deleted);

    /**
     * 设备对接，查询所有人员
     *
     * @return
     */
    @GetMapping(value = "org-employees/feign/findSimAll")
    List<FeignSimEmployeeDto> findSimAll();

    /**
     * 根据身份证号获取人员信息
     *
     * @return
     */
    @GetMapping(value = "org-employees/feign/findByIdNo")
    FeignSimEmployeeDto findByIdNo(@RequestParam("idNo") String idNo);

    /**
     * 大屏-获取后厨人员列表
     *
     * @author loki
     * @date 2021/7/13 17:48
     **/
    @GetMapping(value = "org-employees/feign/getKitchenCookList")
    List<KitchenCookDTO> getKitchenCookList();

    /**
     *
     * @Author JoeLau
     * @Description 根据string部门id(逗号分隔)查总人数
     * @Date  2021/7/15  15:53
     * @Param orgId
     * @return long
     */
    @GetMapping(value = "org-employees/feign/countEmployee/{orgIdString}")
    Integer countEmployee(@PathVariable(name = "orgIdString") String orgIdString);
}
