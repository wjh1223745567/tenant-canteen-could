package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.DeviceSensorData;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 传感器上报数据持久化类
 */
public interface SensorDataRepository extends JpaRepositoryEnhance<DeviceSensorData, Long>, JpaSpecificationExecutor<DeviceSensorData> {
    /**
     * 根据类型获取传感器设备数据
     */
    List<DeviceSensorData> queryByTypeOrderByName(Integer type);

    /**
     * 根据设备devEUI获取传感器设备数据
     */
    List<DeviceSensorData> queryByDevEUI(String devEUI);

    DeviceSensorData queryByNameAndType(String name, Integer type);

    List<DeviceSensorData> queryByDevEUIAndVersionBefore(String devEUI, LocalDateTime version);
}
