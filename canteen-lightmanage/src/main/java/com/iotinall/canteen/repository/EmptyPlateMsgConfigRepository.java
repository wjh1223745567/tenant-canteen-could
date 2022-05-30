package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.EmptyPlateMsgConfig;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 光盘行消息推送配置 Repository
 *
 * @author loki
 * @date 2019-10-30 10:20:51
 */
public interface EmptyPlateMsgConfigRepository extends JpaRepositoryEnhance<EmptyPlateMsgConfig, Long>, JpaSpecificationExecutor<EmptyPlateMsgConfig> {
    /**
     * 根据摄像头获取需要推送的客户端
     */
    List<EmptyPlateMsgConfig> findByCameraId(Long cameraId);

    /**
     * 根据摄像头和客户端ID获取配置
     */
    EmptyPlateMsgConfig findByCameraIdAndAndClientId(Long cameraId, Long clientId);
}
