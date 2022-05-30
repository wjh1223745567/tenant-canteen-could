package com.iotinall.canteen.constants;

/**
 * 宇视人脸终端接口
 */
public class FaceTerminalApi {
    public static final String HTTP = "http://";

    /**
     * 查询人员信息
     */
    public static final String QRY_PERSON_INFO = HTTP + "%s/LAPI/V1.0/PeopleLibraries/%s/People/Info";

    /**
     * 查询人员信息
     */
    public static final String QRY_PERSON_INFO_TCP = "/LAPI/V1.0/PeopleLibraries/%s/People/Info";

    /**
     * 新增人员信息
     */
    public static final String ADD_PERSON_INFO = HTTP + "%s/LAPI/V1.0/PeopleLibraries/%s/People";

    /**
     * 新增人员信息
     */
    public static final String ADD_PERSON_INFO_TCP = "/LAPI/V1.0/PeopleLibraries/%s/People";

    /**
     * 删除人员信息
     */
    public static final String DEL_PERSON_INFO = HTTP + "%s/LAPI/V1.0/PeopleLibraries/%s/People/%s";

    /**
     * 删除人员信息
     */
    public static final String DEL_PERSON_INFO_TCP = "/LAPI/V1.0/PeopleLibraries/%s/People/%s";

    /**
     * 智能分析设备
     */
    public static final String UP_REPORT_RESULTS = "/V1.0/Method/ReportResults";
    public static final String UP_GET_IMG = "/V1.0/Method/GetImage";
}
