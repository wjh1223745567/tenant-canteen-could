package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.DeviceAttendance;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 人脸识别终端
 */
public interface AttendanceDeviceRepository extends JpaRepositoryEnhance<DeviceAttendance, Long>, JpaSpecificationExecutor<DeviceAttendance> {

    /**
     * 根据类型查询设备列表
     */
    List<DeviceAttendance> findByType(Integer type);

    DeviceAttendance findByDeviceNo(String deviceNo);
}
