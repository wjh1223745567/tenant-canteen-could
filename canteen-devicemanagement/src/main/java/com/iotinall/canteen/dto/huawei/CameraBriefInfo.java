package com.iotinall.canteen.dto.huawei;

import lombok.Data;

import java.io.Serializable;

@Data
public class CameraBriefInfo implements Serializable {
    /**
     * 是否为影子摄像机 0：否 1：是
     */
    private Integer isShadowDev;
    /**
     * 原始设备编码，长度限制64字节
     */
    private String origDevCode;

    /**
     * 原始父设备编码，长度限制64字节
     */
    private String origParentDevCode;
    /**
     * 原始域编码，长度限制32字节
     */
    private String oriDevDomainCode;
    /**
     * 是否配置告警联动计划0：否1：是
     */
    private Integer haveAlarmLinkage;
    /**
     * 删除状态 0：待彻底删除 1：设备正常
     */
    private Integer deleteStatus;
    /**
     * 设备编码长度限制64字节
     */
    private String code;
    /**
     * 摄像机名称键盘可见字符和中文，长度限制192字节
     */
    private String name;
    /**
     * 所属设备组编码长度限制128字节
     */
    private String deviceGroupCode;
    /**
     * 父设备编码一般为主设备编码，例如：32010300100201030000#6bdacabae9c546e9ab5b8688ccd85a59，长度限制64字节，
     */
    private String parentCode;
    /**
     * 设备归属域的域编码
     */
    private String domainCode;
    /**
     * 主设备型号由各设备厂家提供，长度限制32字 节
     */
    private String deviceModelType;
    /**
     * 设备提供商类型：（长度限制32字节）
     * HUAWEI
     * HIK
     * DAHUA
     * SUNELL
     * CANON
     * CHANGHONG
     * TIANDY
     * PANASONIC
     * AXIS
     */
    private String vendorType;
    /**
     * 主设备类型：
     * 1：IPC
     * 2：DVS
     * 3：DVR
     * 4：eNVR
     */
    private Integer deviceFormType;
    /**
     * 摄像机类型：
     * 0：固定枪机
     * 1：有云台枪机
     * 2：球机
     * 3：半球-固定摄像机
     * 4：筒机
     */
    private Integer type;
    /**
     * 摄像机安装位置描述键盘可见字符和中文，长度限制256字节
     */
    private String cameraLocation;
    /**
     * 摄像机扩展状态：1：正常 2：视频丢失
     */
    private Integer cameraStatus;
    /**
     * 设备状态：0：离线 1：在线 2：休眠
     */
    private Integer status;
    /**
     * 网络类型：0：有线 1：无线
     */
    private Integer netType;
    /**
     * 是否支持智能分析： 0：不支持 1：支持
     */
    private Integer isSupportIntelligent;
    /**
     * 是否启用随路音频： 0：停用 1：启用
     */
    private Integer enableVoice;
    /**
     * 设备所属NVR编码表示该设备被该NVR管理，例如：9145a3f7c4164d3ab9e161fcb4221426，长度限制32字节
     */
    private String nvrCode;
    /**
     * 设备创建时间 格式为 yyyyMMddHHmmss，如20121207102035，长度限制20字节
     */
    private String deviceCreateTime;
    /**
     * 是否为外域：0：否1：是
     */
    private Integer isExDomain;
    /**
     * 前端IP点分十进制格式，
     * 例如：10.166.166.126，长度限制64字节
     */
    private String deviceIp;
    /**
     * 经度，长度限制20字节
     */
    private String longitude;
    /**
     * 纬度，长度限制20字节
     */
    private String latitude;
    /**
     * 摄像机安装高度
     */
    private Float height;
    /**
     * 互联编码，长度限制64字节
     */
    private String connectCode;
    /**
     * 1400支持能力：
     * 0：不支持1400协议，即作为视频子设备
     * 1：仅支持1400协议，即作为视图子设备
     * 2：同时支持1400协议和视频能力
     */
    private Integer supportGA1400;
    /**
     * 保留字段长度限制252字节，必须保留该字段，字段内容可以置空
     */
    private String reserve;
    /**
     * 透明字段
     */
    private String customFields;
    /**
     * 1400协议使用，功能类型，
     * 1、车辆卡口；
     * 2、人员卡口；
     * 3、微卡口；
     * 4、特征摄像机；
     * 5、普通安防；
     * 6、高空瞭望摄像机；
     * 99 其他，多选各选参数以“/”分割
     */
    private String functionType;
    /**
     * 1400协议使用，监视方向
     */
    private String monitorDirection;
    /**
     * VCN作为采集系统时的采集系统的编码
     */
    private String ownerApsId;
}
