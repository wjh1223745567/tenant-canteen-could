package com.iotinall.canteen.service;


import com.iotinall.canteen.dto.attendance.face.FaceTerminalAddResultResponse;
import com.iotinall.canteen.dto.attendance.face.FaceTerminalPersonDetail;
import com.iotinall.canteen.dto.attendance.face.FaceTerminalQryResponse;

/**
 * HTTP 请求服务类
 */
public interface FaceTerminalFullHttpRequestService {

    /**
     * 添加到人员识别终端
     *
     * @param serialNo 设备编号
     * @param person   人员信息
     * @param lib      人员库
     * @return
     */
    FaceTerminalAddResultResponse addToTerminal(String serialNo, FaceTerminalPersonDetail person, Long lib);

    /**
     * 编辑到人员识别终端
     *
     * @param serialNo 设备编号
     * @param person   人员信息
     * @param lib      人员库
     * @return
     */
    FaceTerminalAddResultResponse updateToTerminal(String serialNo, FaceTerminalPersonDetail person, Long lib);


    /**
     * 从人脸识别终端删除
     *
     * @param serialNo 设备编号
     * @param lib      设备人员库
     * @param personId 设备人员唯一ID
     * @return
     */
    FaceTerminalQryResponse delFromTerminal(String serialNo, Long lib, Long personId);

    /**
     * 根据身份证获取人员信息
     *
     * @param serialNo   设备编号
     * @param lib        设备人员库
     * @param identityNo 人员身份证号
     * @return
     */
    FaceTerminalQryResponse getPersonFromTerminal(String serialNo, Long lib, String identityNo);
}
