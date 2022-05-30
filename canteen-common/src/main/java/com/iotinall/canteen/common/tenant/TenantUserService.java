package com.iotinall.canteen.common.tenant;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.iotinall.canteen.common.constant.TenantOrganizationTypeEnum;
import com.iotinall.canteen.common.datasource.MultiDataSourceInit;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.DataSourceInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TenantUserService {

    @Resource
    private TenantUserRepository tenantUserRepository;

    @Value("${systemSingleStore}")
    private Boolean systemSingleStore;

    @Value("${spring.application.name}")
    private String systemName;


    public List<TenantUser> findAll(Integer type) {
        if (type == null) {
            return new ArrayList<>(0);
        }
        //切换到数据源库
        DynamicDataSourceContextHolder.push("storehouse");

        SpecificationBuilder builder = SpecificationBuilder.builder().where(
                Criterion.eq("type", type)
        );
        List<TenantUser> tenantUsers = this.tenantUserRepository.findAll(builder.build());

        DynamicDataSourceContextHolder.push(MultiDataSourceInit.MASTER);
        return tenantUsers;
    }

    /**
     * 切换数据源。多库时需要切换
     *
     * @param sourceInfo
     */
    public void changeDataSource(DataSourceInfoDTO sourceInfo) {
        if (!systemSingleStore) {
            TenantOrganizationTypeEnum typeEnum = nowSysType();
            switch (typeEnum) {
                case BACK_KITCHEN: {
                    //后厨
                    if (StringUtils.isNotBlank(sourceInfo.getKitchen())) {
                        DynamicDataSourceContextHolder.push(sourceInfo.getKitchen());
                    } else {
                        log.error("用户未配置访问后厨库");
                        DynamicDataSourceContextHolder.push(MultiDataSourceInit.MASTER);
                    }
                    break;
                }
                case INVENTORY: {
                    //库存
                    if (StringUtils.isNotBlank(sourceInfo.getStock())) {
                        DynamicDataSourceContextHolder.push(sourceInfo.getStock());
                    } else {
                        log.error("用户未配置访问库存库");
                        DynamicDataSourceContextHolder.push(MultiDataSourceInit.MASTER);
                    }
                    break;
                }
                case DINING_HALL: {
                    //食堂
                    if (StringUtils.isNotBlank(sourceInfo.getMenu())) {
                        DynamicDataSourceContextHolder.push(sourceInfo.getMenu());
                    } else {
                        log.error("用户未配置访问餐厅库");
                        DynamicDataSourceContextHolder.push(MultiDataSourceInit.MASTER);
                    }
                    break;
                }
            }
        }
    }

    /**
     * 获取当前服务对应的数据源类型
     *
     * @return
     */
    public TenantOrganizationTypeEnum nowSysType() {
        return TenantOrganizationTypeEnum.findBySysName(systemName);
    }

}
