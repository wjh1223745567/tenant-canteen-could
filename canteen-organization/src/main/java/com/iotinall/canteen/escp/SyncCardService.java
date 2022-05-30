package com.iotinall.canteen.escp;

import com.alibaba.fastjson.JSONArray;
import com.iotinall.canteen.dto.escp.CardListDTO;
import com.iotinall.canteen.dto.escp.CardListReq;
import com.iotinall.canteen.dto.escp.RespDTO;
import com.iotinall.canteen.entity.OrgEmployee;
import com.iotinall.canteen.entity.OrgEmployeeCard;
import com.iotinall.canteen.escp.base.BaseSyncService;
import com.iotinall.canteen.repository.OrgEmployeeCardRepository;
import com.iotinall.canteen.repository.OrgEmployeeRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 新开普同步卡列表
 *
 * @author loki
 * @date 2021/6/21 10:05
 **/
@Service
public class SyncCardService extends BaseSyncService {
    @Resource
    private OrgEmployeeCardRepository orgEmployeeCardRepository;

    /**
     * 同步开卡信息
     *
     * @author loki
     * @date 2021/6/21 11:01
     **/
    private void sync() {
        CardListReq req = new CardListReq()
                .setCount(10000)
                .setVer(1000L);

        RespDTO result = execute(escpProperty.getApiUrl(API_OPEN_CARD_LIST), req);

        String data = decode(result.getData());
        if (StringUtils.isNotBlank(data)) {
            List<CardListDTO> escpCardList = JSONArray.parseArray(data, CardListDTO.class);
            if (!CollectionUtils.isEmpty(escpCardList)) {
                List<OrgEmployeeCard> cardList = new ArrayList<>();
                OrgEmployeeCard card;
                OrgEmployee employee;
                for (CardListDTO escpCard : escpCardList) {
                    //判断用户是否存在
                    employee = orgEmployeeRepository.queryByPersonCodeAndDeletedFalse(escpCard.getIdSerial2());
                    if (null == employee) {
                        continue;
                    }

                    //判断卡片是否存在
                    card = orgEmployeeCardRepository.findByCardNo(escpCard.getCardNo());
                    if (null == card) {
                        card = new OrgEmployeeCard();
                    }

                    card.setEmployee(employee);

                    BeanUtils.copyProperties(escpCard, card);
                    cardList.add(card);
                }
                this.orgEmployeeCardRepository.saveAll(cardList);
            }
        }
    }
}
