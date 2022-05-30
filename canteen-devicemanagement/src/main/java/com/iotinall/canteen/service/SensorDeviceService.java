package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.dto.sensor.SensorAddReq;
import com.iotinall.canteen.dto.sensor.SensorDTO;
import com.iotinall.canteen.dto.sensor.updata.DevicePayload;
import com.iotinall.canteen.dto.sensor.updata.SensorDataDTO;
import com.iotinall.canteen.entity.DeviceSensor;
import com.iotinall.canteen.entity.DeviceSensorData;
import com.iotinall.canteen.repository.SensorDataRepository;
import com.iotinall.canteen.repository.SensorRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 传感器逻辑处理类
 *
 * @author loki
 * @date 2021/01/09 15:57
 */
@Service
public class SensorDeviceService {
    @Resource
    private SensorRepository sensorRepository;
    @Resource
    private SensorDataRepository sensorDataRepository;

    public Object page(String keywords, Pageable page) {
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .whereByOr(Criterion.like("name", keywords));
        Page<DeviceSensor> result = this.sensorRepository.findAll(builder.build(), page);
        if (CollectionUtils.isEmpty(result.getContent())) {
            return PageUtil.toPageDTO(result);
        }

        List<SensorDTO> sensorDTOList = new ArrayList<>();
        SensorDTO sensorDTO;
        for (DeviceSensor sensor : result.getContent()) {
            sensorDTO = new SensorDTO();
            BeanUtils.copyProperties(sensor, sensorDTO);
            sensorDTOList.add(sensorDTO);
        }
        return PageUtil.toPageDTO(sensorDTOList, result);
    }

    public Object create(SensorAddReq req) {
        DeviceSensor sensor = this.sensorRepository.queryByDeviceNo(req.getDeviceNo());
        if (null != sensor) {
            throw new BizException("", "设备编号已存在");
        } else {
            sensor = new DeviceSensor();
        }
        BeanUtils.copyProperties(req, sensor);
        sensor.setCreateTime(LocalDateTime.now());
        this.sensorRepository.save(sensor);
        return sensor;
    }

    public Object update(SensorAddReq req) {
        DeviceSensor sensor = this.sensorRepository.findById(req.getId()).orElseThrow(() -> new BizException("", "设备不存在"));

        DeviceSensor sensor2 = this.sensorRepository.queryByDeviceNo(req.getDeviceNo());
        if (null != sensor2 && !sensor2.getId().equals(req.getId())) {
            throw new BizException("", "设备编号已存在");
        }

        BeanUtils.copyProperties(req, sensor, "code");
        sensor.setUpdateTime(LocalDateTime.now());
        this.sensorRepository.save(sensor);
        return sensor;
    }

    public Object batchDelete(Set<Long> ids) {
        List<DeviceSensor> result = this.sensorRepository.findAllById(ids);
        if (ids.size() != result.size()) {
            throw new BizException("", "请求参数错误");
        }

        this.sensorRepository.deleteAll(result);
        return result;
    }

    public Object getInfo(Integer type) {
        Map<String, Object> result;
        if (null != type) {
            List<DeviceSensorData> sensorData = this.sensorDataRepository.queryByTypeOrderByName(type);

            result = new HashMap<>(2);
            result.put("key", type);
            result.put("value", sensorData);
        } else {
            //1-餐厅环境 2-后厨环境 3-仓库环境
            result = new HashMap<>(3);
            List<DeviceSensorData> canteenSensorData = this.sensorDataRepository.queryByTypeOrderByName(1);

            result.put("canteen", canteenSensorData);

            List<DeviceSensorData> kitchenSensorData = this.sensorDataRepository.queryByTypeOrderByName(2);

            result.put("kitchen", kitchenSensorData);

            List<DeviceSensorData> warehouseSensorData = this.sensorDataRepository.queryByTypeOrderByName(3);

            result.put("warehouse", warehouseSensorData);
        }

        return result;
    }

    public Object getInfoById(Long id) {
        DeviceSensor sensor = this.sensorRepository.findById(id).orElseThrow(() -> new BizException("设备不存在"));
        return this.sensorDataRepository.queryByDevEUI(sensor.getDeviceNo());
    }

    /**
     * 设备上报数据接口
     *
     * @author loki
     * @date 2021/7/9 17:08
     **/
    public void event(DevicePayload data, String event) {
        DeviceSensor sensor = this.sensorRepository.queryByDeviceNo(data.getDevEUI());
        if (null == sensor) {
            sensor = new DeviceSensor();
            sensor.setStatus(1);
            sensor.setDeviceNo(data.getDevEUI());
            sensor.setName(data.getDeviceName());
            sensor.setData(data.getObjectJSON());
        } else {
            sensor.setData(data.getObjectJSON());
        }
        this.sensorRepository.save(sensor);

        LocalDateTime version = LocalDateTime.now();
        if (StringUtils.isNotBlank(event) && event.equals("up") && StringUtils.isNotBlank(data.getObjectJSON())) {
            SensorDataDTO sensorData = JSON.parseObject(data.getObjectJSON(), SensorDataDTO.class);

            if (null == sensorData || CollectionUtils.isEmpty(sensorData.getAtt())) {
                return;
            }

            DeviceSensorData sd;
            List<DeviceSensorData> sdList = new ArrayList<>();
            for (SensorDataDTO.Att att : sensorData.getAtt()) {
                sd = this.sensorDataRepository.queryByNameAndType(att.getName(), sensor.getType());
                if (sd == null) {
                    sd = new DeviceSensorData();
                    sd.setName(att.getName());
                    sd.setValue(sensorData.getStatus() == 1 ? att.getData() : 0 + "");
                    sd.setType(sensor.getType());
                } else {
                    //异常不更新
                    if (sensorData.getStatus() == 1) {
                        sd.setValue(att.getData());
                    }
                }
                sd.setVersion(version);
                sd.setUnit(att.getUnit());
                sdList.add(sd);
            }
            this.sensorDataRepository.saveAll(sdList);
        }

        //删除以前历史的
        List<DeviceSensorData> result = this.sensorDataRepository.queryByDevEUIAndVersionBefore(sensor.getDeviceNo(), version);
        if (!CollectionUtils.isEmpty(result)) {
            this.sensorDataRepository.deleteAll(result);
        }
    }
}
