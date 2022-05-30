package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.DeviceAIBox;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * AIBox
 */
public interface AIBoxDeviceRepository extends JpaRepositoryEnhance<DeviceAIBox, Long>, JpaSpecificationExecutor<DeviceAIBox> {

}
