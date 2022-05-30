package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.attendance.face.FaceTerminalAddResultResponse;
import com.iotinall.canteen.dto.attendance.face.FaceTerminalPersonDetail;
import com.iotinall.canteen.dto.attendance.face.FaceTerminalPersonList;
import com.iotinall.canteen.dto.attendance.face.FaceTerminalQryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * HTTP 请求服务类
 */
@Slf4j
@Service
public class FaceTerminalFullHttpRequestServiceImpl extends BaseService implements FaceTerminalFullHttpRequestService {

    @Override
    public FaceTerminalAddResultResponse addToTerminal(String serialNo, FaceTerminalPersonDetail person, Long lib) {
//        Map<String, String> result = HttpRequestHelper.syncToTerminal(buildUrl(FaceTerminalApi.ADD_PERSON_INFO_TCP, lib + ""), serialNo, this.buildPersonData(person), HttpMethod.POST);
//        return JSONObject.parseObject(JSONObject.toJSONString(result), FaceTerminalAddResultResponse.class);
        return null;
    }

    @Override
    public FaceTerminalAddResultResponse updateToTerminal(String serialNo, FaceTerminalPersonDetail person, Long lib) {
//        Map<String, String> result = HttpRequestHelper.syncToTerminal(buildUrl(FaceTerminalApi.ADD_PERSON_INFO_TCP, lib + ""), serialNo, this.buildPersonData(person), HttpMethod.PUT);
//        return JSONObject.parseObject(JSONObject.toJSONString(result), FaceTerminalAddResultResponse.class);
        return null;
    }

    @Override
    public FaceTerminalQryResponse delFromTerminal(String serialNo, Long lib, Long personId) {
//        Map<String, String> result = HttpRequestHelper.syncToTerminal(buildUrl(FaceTerminalApi.DEL_PERSON_INFO_TCP, lib + "", personId + ""), serialNo, "", HttpMethod.DELETE);
//
//        return JSONObject.parseObject(JSONObject.toJSONString(result).replace("\"Data\":\"null\"", "\"Data\":{}"), FaceTerminalQryResponse.class);
        return null;
    }

    @Override
    public FaceTerminalQryResponse getPersonFromTerminal(String serialNo, Long lib, String identityNo) {
//        FaceTerminalQuery query = this.buildQueryParam(TerminalConstants.FACE_TERMINAL_QRY_TYPE_EMP_IDENTITY_NO, 0L, identityNo);
//        Map<String, String> result = HttpRequestHelper.syncToTerminal(buildUrl(FaceTerminalApi.QRY_PERSON_INFO_TCP, lib + ""), serialNo, query, HttpMethod.POST);
//        return JSONObject.parseObject(JSONObject.toJSONString(result), FaceTerminalQryResponse.class);
        return null;
    }

    /**
     * 构建添加或者编辑请求对象
     */
    private FaceTerminalPersonList buildPersonData(FaceTerminalPersonDetail personDetail) {
        FaceTerminalPersonList personList = new FaceTerminalPersonList();
        List<FaceTerminalPersonDetail> persons = new ArrayList<>();

        persons.add(personDetail);
        personList.setPersonInfoList(persons);
        personList.setNum(persons.size());
        return personList;
    }
}
