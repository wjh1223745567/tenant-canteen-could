package com.iotinall.canteen.escp;

import com.alibaba.fastjson.JSONArray;
import com.iotinall.canteen.dto.escp.OrgListDTO;
import com.iotinall.canteen.dto.escp.OrgListReq;
import com.iotinall.canteen.dto.escp.RespDTO;
import com.iotinall.canteen.entity.Org;
import com.iotinall.canteen.escp.base.BaseSyncService;
import com.iotinall.canteen.repository.OrgRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 新开普同步部门
 *
 * @author loki
 * @date 2021/6/21 10:05
 **/
@Service
public class SyncOrgService extends BaseSyncService {
    @Resource
    private OrgRepository orgRepository;

    /**
     * 同步部门
     *
     * @author loki
     * @date 2021/6/21 11:01
     **/
    private void sync() {
        OrgListReq req = new OrgListReq()
                .setCount(10000)
                .setType(0)
                .setVer(1000L);

        RespDTO result = execute(escpProperty.getApiUrl(API_ORG), req);

        String data = decode(result.getData());

        if (StringUtils.isNotBlank(data)) {
            List<OrgListDTO> escpOrgList = JSONArray.parseArray(data, OrgListDTO.class);
            if (!CollectionUtils.isEmpty(escpOrgList)) {
                Org org;
                List<Org> orgList = new ArrayList<>();
                for (OrgListDTO escpOrg : escpOrgList) {
                    org = new Org();
                    orgList.add(org);
                    org.setOrgCode(escpOrg.getOrganCode() + "");
                    org.setName(escpOrg.getDeptName());
                    org.setParentId(escpOrg.getParentId());
                }

                this.orgRepository.saveAll(orgList);
            }
        }
    }
}
