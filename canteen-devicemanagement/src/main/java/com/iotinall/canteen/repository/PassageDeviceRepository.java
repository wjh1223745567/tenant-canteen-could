package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.DevicePassage;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 通行设备持久化类
 *
 * @author loki
 * @date 2021/7/9 10:54
 **/
public interface PassageDeviceRepository extends JpaRepositoryEnhance<DevicePassage, Long>, JpaSpecificationExecutor<DevicePassage> {
    /**
     * 根据设备编号查询
     */
    DevicePassage findByDeviceNo(String deviceNo);

    /**
     * 判断设备编号是否已存在
     */
    Long countByDeviceNoAndIdNot(String deviceNo, Long id);
}
