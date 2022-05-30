package com.iotinall.canteen.escp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.util.UUIDUtil;
import com.iotinall.canteen.constant.DateTimeFormatters;
import com.iotinall.canteen.constant.EscpErrorCodeEnum;
import com.iotinall.canteen.constant.EscpRechargeErrorCodeEnum;
import com.iotinall.canteen.dto.escp.*;
import com.iotinall.canteen.escp.base.BaseSyncService;
import com.iotinall.canteen.utils.LocalDateTimeUtil;
import com.iotinall.canteen.utils.MoneyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * 虚拟卡消费
 *
 * @author loki
 * @date 2021/6/21 10:05
 **/
@Slf4j
@Service
public class EscpRechargeService extends BaseSyncService {

    public Boolean recharge(String cardNo,
                            String studentId,
                            String studentName,
                            BigDecimal amount,
                            String tradeNo) {
        try {
            //1
            RechargeEmployeeCheckResp checkResult = check(cardNo, studentId, studentName);

            //2
            RechargeEmployeeApplyResp applyResult = apply(cardNo, studentId, studentName, amount, tradeNo);

            //3
            submit(cardNo, studentId, amount, tradeNo, applyResult.getOutId());
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 充值身份校验
     *
     * @author loki
     * @date 2021/6/21 11:01
     **/
    private RechargeEmployeeCheckResp check(String cardNo, String studentId, String studentName) {
        RechargeReq req = new RechargeReq();
        req.setAppId(escpProperty.getAppid())
                .setClientId(escpProperty.getClientId())
                .setData(buildCheckData(cardNo, studentId, studentName))
                .setNorceStr(System.currentTimeMillis() + UUIDUtil.generateUuid(16))
                .setPayCode(escpProperty.getPayCode());

        RespDTO result = execute(escpProperty.getApiUrl(API_RECHARGE_CHECK), req);
        log.info("请求返回结果：{}", JSON.toJSONString(result));
        if (result.getRetCode() != EscpErrorCodeEnum.SUCCESS_200.getCode()) {
            throw new BizException("充值失败");
        }

        String data = decode(result.getData());
        log.info("解密结果：{}", data);
        if (StringUtils.isNotBlank(data)) {
            try {
                RechargeResp resp = JSONObject.parseObject(data, RechargeResp.class);

                List<RechargeEmployeeCheckResp> checkRespList = (List<RechargeEmployeeCheckResp>) resp.getData();
                RechargeEmployeeCheckResp checkResp = checkRespList.get(0);
                if (!checkResp.getReturnCode().equals("SUCCESS")) {
                    log.info("请求错误码[CHECK]{}", EscpRechargeErrorCodeEnum.getByCode(checkResp.getReturnCode()));
                    throw new BizException("充值失败");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        throw new BizException("充值失败");
    }

    /**
     * 构建身份校验请求参数
     *
     * @author loki
     * @date 2021/6/24 15:11
     **/
    private List<RechargeEmployeeCheckReq> buildCheckData(String cardNo, String studentId, String studentName) {
        String acceptTime = LocalDateTimeUtil.localDatetime2Str(LocalDateTime.now(), DateTimeFormatters.YYYYMMDDHHMMSS);
        RechargeEmployeeCheckReq rechargeEmployeeCheckDTO = new RechargeEmployeeCheckReq();
        rechargeEmployeeCheckDTO.setCardNo(cardNo);
        rechargeEmployeeCheckDTO.setStudentId(studentId);
        rechargeEmployeeCheckDTO.setStudentName(studentName);
        rechargeEmployeeCheckDTO.setAcceptTime(acceptTime);
        return Collections.singletonList(rechargeEmployeeCheckDTO);
    }

    /**
     * 充值申请
     *
     * @author loki
     * @date 2021/6/23 17:34
     **/
    public RechargeEmployeeApplyResp apply(String cardNo,
                                           String studentId,
                                           String studentName,
                                           BigDecimal amount,
                                           String tradeNo) {
        RechargeReq req = new RechargeReq();
        req.setAppId(escpProperty.getAppid())
                .setClientId(escpProperty.getClientId())
                .setData(buildApplyData(cardNo, studentId, studentName, amount, tradeNo))
                .setNorceStr(System.currentTimeMillis() + UUIDUtil.generateUuid(16))
                .setPayCode(escpProperty.getPayCode());

        RespDTO result = execute(escpProperty.getApiUrl(API_RECHARGE_APPLY), req);
        log.info("请求返回结果：{}", JSON.toJSONString(result));

        if (result.getRetCode() != EscpErrorCodeEnum.SUCCESS_200.getCode()) {
            throw new BizException("充值失败");
        }

        String data = decode(result.getData());
        log.info("解密结果：{}", data);
        if (StringUtils.isNotBlank(data)) {
            try {
                RechargeResp resp = JSONObject.parseObject(data, RechargeResp.class);

                List<RechargeEmployeeApplyResp> checkRespList = (List<RechargeEmployeeApplyResp>) resp.getData();
                RechargeEmployeeApplyResp checkResp = checkRespList.get(0);
                if (!checkResp.getReturnCode().equals("SUCCESS")) {
                    log.info("请求错误码[APPLY]{}", EscpRechargeErrorCodeEnum.getByCode(checkResp.getReturnCode()));
                    throw new BizException("充值失败");
                }

                return checkResp;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        throw new BizException("充值失败");
    }

    /**
     * 构建身份校验请求参数
     *
     * @author loki
     * @date 2021/6/24 15:11
     **/
    private List<RechargeEmployeeApplyReq> buildApplyData(String cardNo,
                                                          String studentId,
                                                          String studentName,
                                                          BigDecimal amount,
                                                          String tradeNo) {
        RechargeEmployeeApplyReq param = new RechargeEmployeeApplyReq();
        param.setCardNo(cardNo);
        param.setStudentId(studentId);
        param.setStudentName(studentName);
        param.setApplyId(tradeNo);
        param.setApplyAmount(MoneyUtil.yuan2Fen(amount));
        param.setType(10);
        return Collections.singletonList(param);
    }

    /**
     * 获取充值结果
     *
     * @author loki
     * @date 2021/6/23 17:35
     **/
    private void submit(String cardNo,
                        String studentId,
                        BigDecimal amount,
                        String tradeNo,
                        String outId) {
        RechargeReq req = new RechargeReq();
        req.setAppId(escpProperty.getAppid())
                .setClientId(escpProperty.getClientId())
                .setData(buildSubmitData(cardNo, studentId, amount, tradeNo, outId))
                .setNorceStr(System.currentTimeMillis() + UUIDUtil.generateUuid(16))
                .setPayCode(escpProperty.getPayCode())
                .setSchoolId(escpProperty.getSchoolId());

        RespDTO result = execute(escpProperty.getApiUrl(API_RECHARGE_SUBMIT), req);
        log.info("请求返回结果：{}", JSON.toJSONString(result));
        if (result.getRetCode() != EscpErrorCodeEnum.SUCCESS_200.getCode()) {
            throw new BizException("充值失败");
        }

        String data = decode(result.getData());
        log.info("解密结果：{}", data);
        if (StringUtils.isNotBlank(data)) {
            try {
                RechargeResp resp = JSONObject.parseObject(data, RechargeResp.class);
                List<RechargeEmployeeSubmitResp> checkRespList = (List<RechargeEmployeeSubmitResp>) resp.getData();
                RechargeEmployeeSubmitResp checkResp = checkRespList.get(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        throw new BizException("充值失败");
    }

    /**
     * 构建充值提交请求参数
     *
     * @author loki
     * @date 2021/6/24 15:11
     **/
    private List<RechargeEmployeeSubmitReq> buildSubmitData(String cardNo,
                                                            String studentId,
                                                            BigDecimal amount,
                                                            String tradeNo,
                                                            String outId) {
        RechargeEmployeeSubmitReq param = new RechargeEmployeeSubmitReq();
        param.setCardNo(cardNo);
        param.setStudentId(studentId);
        param.setApplyId(tradeNo);
        param.setPayTime(LocalDateTimeUtil.localDatetime2Str(LocalDateTime.now(), DateTimeFormatters.YYYYMMDDHHMMSS));
        param.setOutId(outId);
        param.setRechargeOrderNo(tradeNo);
        param.setRechargeAmount(MoneyUtil.yuan2Fen(amount));
        return Collections.singletonList(param);
    }

    /**
     * 获取充值状态
     *
     * @author loki
     * @date 2021/6/24 19:59
     **/
    public void getRechargeStatus(String cardNo, String studentId, String outId) {
        RechargeReq req = new RechargeReq();
        req.setAppId(escpProperty.getAppid())
                .setClientId(escpProperty.getClientId())
                .setData(buildRechargeStatus(cardNo, studentId, outId))
                .setNorceStr(System.currentTimeMillis() + UUIDUtil.generateUuid(16))
                .setPayCode(escpProperty.getPayCode())
                .setSchoolId(escpProperty.getSchoolId());

        RespDTO result = execute(escpProperty.getApiUrl(API_RECHARGE_STATUS), req);
        log.info("请求返回结果：{}", JSON.toJSONString(result));

        String data = decode(result.getData());
        log.info("解密结果：{}", data);
        if (StringUtils.isNotBlank(data)) {
            RechargeResp resp = JSONObject.parseObject(data, RechargeResp.class);
        }
    }

    /**
     * 构建获取充值状态请求参数
     *
     * @author loki
     * @date 2021/6/24 20:14
     **/
    private List<RechargeStatusReq> buildRechargeStatus(String cardNo, String studentId, String outId) {
        RechargeStatusReq param = new RechargeStatusReq();
        param.setCardNo(cardNo);
        param.setStudentId(studentId);
        param.setOutId(outId);
        return Collections.singletonList(param);
    }
}
