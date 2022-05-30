package com.iotinall.canteen.utils;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.SpringContextUtil;
import com.iotinall.canteen.service.FeignTenantOrganizationService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TenantOrgUtil {

    /**
     * 单库需要根据食堂租户ID分表时查询条件
     */
    public static void getTenantSpec(SpecificationBuilder builder) {
        //非管理员查询租户ID
        FeignTenantOrganizationService feignTenantOrganizationService = SpringContextUtil.getContext().getBean(FeignTenantOrganizationService.class);
        Long menuId = feignTenantOrganizationService.findMenuId();
        if (menuId == null) {
            throw new BizException("", "未找到当前食堂信息");
        }
        builder.where(Criterion.eq("tenantId", menuId));
    }

    /**
     * 获取当前餐厅租户ID
     *
     * @return
     */
    public static Long findMenuId() {
        Long menuId = SpringContextUtil.getContext().getBean(FeignTenantOrganizationService.class).findMenuId();
        if (menuId == null) {
            throw new BizException("", "未找到当前食堂");
        }
        return menuId;
    }
}
