package com.iotinall.canteen.dto.face;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 库比对信息
 *
 * @author loki
 * @date 2020/06/02 17:48
 */
@Data
public class TerminalReportRecordLibMatDTO {
    /**
     * 记录ID
     */
    @JSONField(name = "ID")
    private Long id;

    /**
     * 库ID
     */
    @JSONField(name = "LibID")
    private Integer libId;

    /**
     * 库类型，可选字段
     * 0: 默认无效值; 1：黑名单
     * 2: 灰名单/陌生人
     * 3：员工
     * 4: 访客
     */
    @JSONField(name = "LibType")
    private Integer libType;

    /**
     * 匹配状态
     * 0：无核验状态
     * 1：核验成功，
     * 2：核验失败（比对失败），
     * 3：核验失败（对比成功，
     * 不在布控时间）
     * 4：核验失败（证件已过
     * 期）
     * 5：强制停止
     * 6：非活体
     * 7：刷脸开门模式下，刷脸
     * 成功；
     * 8：刷脸开门模式下，刷脸
     * 失败；
     * 9：安全帽识别失败；
     * 10：核验失败（对比成功，
     * 人脸属性异常）；
     * 11：测温异常；
     * 21：底图录入成功-新增，
     * 22：底图录入成功-更新；
     * 23：底图录入成功-人脸采
     * 集；
     * 24：录入失败；
     * 25：录入失败-证件已过期；
     * 26：录入失败-非活体；
     * 27：无效值；
     * 说明：当 MatchStatus 为 10
     * 时，需要读取 FaceInfoList
     * 中的 Temperature 和
     * MaskFlag 字段获取具体的
     * 异常信息
     */
    @JSONField(name = "MatchStatus")
    private Integer matchStatus;

    /**
     * 匹配人员ID
     */
    @JSONField(name = "MatchPersonID")
    private Integer matchPersonId;

    /**
     * 匹配人脸 ID
     */
    @JSONField(name = "MatchFaceID")
    private Long matchFaceID;

    /**
     * 匹配人员信息，可选字段
     */
    @JSONField(name = "MatchPersonInfo")
    private String matchPersonInfo;
}
