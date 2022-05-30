package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.DeviceSensor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 传感器持久化类
 */
public interface SensorRepository extends JpaRepositoryEnhance<DeviceSensor, Long>, JpaSpecificationExecutor<DeviceSensor> {
    DeviceSensor queryByDeviceNo(String deviceNo);

    List<DeviceSensor> queryByType(Integer type);
}
