package com.iotinall.canteen.escp;

import com.alibaba.fastjson.JSONArray;
import com.iotinall.canteen.dto.escp.RespDTO;
import com.iotinall.canteen.dto.escp.VersionReq;
import com.iotinall.canteen.escp.base.BaseSyncService;
import com.iotinall.canteen.utils.LocalDateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 新开普同步版本号
 *
 * @author loki
 * @date 2021/6/21 10:05
 **/
@Service
public class SyncVersionService extends BaseSyncService {
    /**
     * 同步版本号
     *
     * @author loki
     * @date 2021/6/21 11:01
     **/
    private void sync() {
        VersionReq req = new VersionReq()
                .setType(1)
                .setStartDate(LocalDateTimeUtil.localDatetime2Str(LocalDateTime.now()));

        RespDTO result = execute(escpProperty.getApiUrl(API_VERSION_LIST), req);

        String data = decode(result.getData());
        if (StringUtils.isNotBlank(data)) {
            List<String> escpVersionList = JSONArray.parseArray(data, String.class);
            if (!CollectionUtils.isEmpty(escpVersionList)) {
            }
        }
    }
}
