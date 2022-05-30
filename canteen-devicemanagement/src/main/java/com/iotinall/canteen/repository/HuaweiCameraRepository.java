package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.DeviceHuaweiCamera;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 华为摄像机 Repository
 *
 * @author xin-bing
 * @date 2019-11-26 20:31:06
 */
public interface HuaweiCameraRepository extends JpaRepositoryEnhance<DeviceHuaweiCamera, Long>, JpaSpecificationExecutor<DeviceHuaweiCamera> {
    /**
     * 通过code获取华为摄像机
     */
    DeviceHuaweiCamera findByCode(String code);
}