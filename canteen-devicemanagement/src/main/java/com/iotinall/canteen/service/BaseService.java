package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.attendance.face.FaceTerminalQuery;
import com.iotinall.canteen.dto.attendance.face.FaceTerminalQueryDetail;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * @author loki
 * @date 2020/06/10 18:38
 */
@Service
public class BaseService {
    protected final int SYNC_SLICE = 2000;

    /**
     * 组装请求参数
     *
     * @author loki
     * @date 2020/06/09 17:15
     */
    protected String buildUrl(String uri, Object... args) {
        return String.format(uri, args);
    }

    /**
     * 构建请求参数
     *
     * @param qryType      查询类型
     * @param qryCondition 查询条件名称
     * @param qryData      条件值
     * @return 返回请求参数对象
     */
    protected FaceTerminalQuery buildQueryParam(Long qryType, Long qryCondition, String qryData) {
        FaceTerminalQueryDetail queryDetail = new FaceTerminalQueryDetail()
                .setQryType(qryType)
                .setQryCondition(qryCondition)
                .setQryData(qryData);
        FaceTerminalQuery query = new FaceTerminalQuery();
        query.setQueryDetailList(Collections.singletonList(queryDetail));
        query.setNum(query.getQueryDetailList().size());
        query.setOffset(0);
        query.setLimit(2);
        return query;
    }
}
