package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.DeviceCamera;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;

/**
 * equ_face_device Repository
 *
 * @author xin-bing
 * @date 2019-11-26 20:31:06
 */
public interface CameraRepository extends JpaRepositoryEnhance<DeviceCamera, Long>, JpaSpecificationExecutor<DeviceCamera> {

    /**
     * 批量删除
     *
     * @param ids
     * @return int
     */
    int deleteByIdIn(@Param(value = "ids") Long[] ids);

    /**
     * 根据deviceNo查找
     */
    DeviceCamera findByDeviceNo(String deviceNo);

    Long countByDeviceNoAndIdNot(String deviceNo,Long id);
}