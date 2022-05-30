package com.iotinall.canteen.escp;

import com.alibaba.fastjson.JSONObject;
import com.iotinall.canteen.dto.escp.ConsumeQueryReq;
import com.iotinall.canteen.dto.escp.ConsumeReq;
import com.iotinall.canteen.dto.escp.ConsumeResp;
import com.iotinall.canteen.dto.escp.RespDTO;
import com.iotinall.canteen.entity.OrgEmployee;
import com.iotinall.canteen.escp.base.BaseSyncService;
import com.iotinall.canteen.utils.LocalDateTimeUtil;
import com.iotinall.canteen.utils.MoneyUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 虚拟卡消费
 *
 * @author loki
 * @date 2021/6/21 10:05
 **/
@Service
public class EscpConsumeService extends BaseSyncService {
    /**
     * 虚拟卡消费
     *
     * @author loki
     * @date 2021/6/21 11:01
     **/
    public ConsumeResp consume(OrgEmployee employee,
                               LocalDateTime consumeDateTime,
                               String tradeNo,
                               BigDecimal amount) {
        ConsumeReq req = new ConsumeReq()
                .setPosCode(escpProperty.getPosCode())
                .setIdSerial(employee.getPersonCode())
                .setUsername(employee.getName())
                .setOpFare(MoneyUtil.yuan2Fen(amount))
                .setThirdNo(tradeNo)
                .setOpDt(LocalDateTimeUtil.localDatetime2Str(consumeDateTime));


        RespDTO result = execute(escpProperty.getApiUrl(API_CONSUME), req);
        String data = decode(result.getData());
        return JSONObject.parseObject(data, ConsumeResp.class);
    }

    /**
     * 虚拟卡消费详情
     *
     * @author loki
     * @date 2021/6/22 20:15
     **/
    public void detail(String termTradeNo) {
        ConsumeQueryReq req = new ConsumeQueryReq()
                .setPosCode(escpProperty.getPosCode())
                .setTermTradeNo(termTradeNo);
        RespDTO result = execute(escpProperty.getApiUrl(API_CONSUME), req);

        String data = decode(result.getData());
//         JSONObject.parseObject(data, .class);
    }
}
