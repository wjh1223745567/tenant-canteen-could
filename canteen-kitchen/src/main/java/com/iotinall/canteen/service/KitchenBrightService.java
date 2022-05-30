package com.iotinall.canteen.service;

import com.alibaba.fastjson.JSON;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.util.FileHandler;
import com.iotinall.canteen.constant.KitchenBrightAlarmTypeEnum;
import com.iotinall.canteen.dto.brightkitchen.KitchenBrightQueryCriteria;
import com.iotinall.canteen.dto.brightkitchen.nvr.NvrAlarmPictureInfo;
import com.iotinall.canteen.dto.brightkitchen.nvr.NvrTerminalRequest;
import com.iotinall.canteen.entity.KitchenBrightRecord;
import com.iotinall.canteen.repository.KitchenBrightRepository;
import com.iotinall.canteen.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.io.File;
import java.time.LocalDateTime;

/**
 * @author bingo
 * @date 1/4/2020 18:38
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class KitchenBrightService {
    @Resource
    private KitchenBrightRepository kitchenBrightRepository;
    @Resource
    private FileHandler fileHandler;
    @Value("${iotinall.aibox.record:false}")
    private Boolean record;

    public PageDTO<KitchenBrightRecord> findPage(KitchenBrightQueryCriteria query, Pageable pageable) {
        Specification<KitchenBrightRecord> spec = SpecificationBuilder.builder()
                .whereByOr(Criterion.like("areaName", query.getKeyword())
                        , Criterion.like("alarmType", query.getKeyword()))
                .where(Criterion.eq("type", query.getType()))
                .where(
                        Criterion.gte("detectTime", query.getBeginTime()),
                        Criterion.lte("detectTime", query.getEndTime() == null ? null : query.getEndTime().plusDays(1))
                ).build();
        Page<KitchenBrightRecord> records = kitchenBrightRepository.findAll(spec, pageable);
        return PageUtil.toPageDTO(records);
    }

    public void handleNvrRequest(String data) {
        if (StringUtils.isBlank(data) || !record) {
            return;
        }

        data = data.replace("\\t", "").replace("\\n", "").replace("\\", "");

        NvrTerminalRequest req = JSON.parseObject(data.substring(1, data.length() - 1), NvrTerminalRequest.class);

        KitchenBrightRecord record = new KitchenBrightRecord();
        record.setType(req.getAlarmType());
        record.setDetectTime(LocalDateTime.now());
        record.setCreateTime(LocalDateTime.now());
        record.setAreaName("厨房");
        record.setAlarmType(KitchenBrightAlarmTypeEnum.getNameFindByCode(record.getType()));
        //处理图片
        String imgUrl = "";
        if (req.getAlarmPicture() != null && !CollectionUtils.isEmpty(req.getAlarmPicture().getImageInfoList())) {
            NvrAlarmPictureInfo pictureInfo = req.getAlarmPicture().getImageInfoList().get(0);
            if (StringUtils.isNotBlank(pictureInfo.getData())) {
                File file = null;
                try {
                    file = FileUtil.base64ToTempFile(pictureInfo.getData(), System.currentTimeMillis() + "");
                    imgUrl = fileHandler.saveImage("group1", ImageIO.read(file));
                } catch (Exception ex) {
                    log.info("内网上传图片处理失败");
                } finally {
                    if (null != file && file.exists()) {
                        file.delete();
                    }
                }
            }
        }
        record.setImageFile(imgUrl);
        record.setOriginalFile(imgUrl);
        this.kitchenBrightRepository.save(record);
    }

    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(Long[] ids) {
        KitchenBrightRecord record;
        for (Long id : ids) {
            record = this.kitchenBrightRepository.findById(id).orElseThrow(() -> new BizException("", "未找到要删除的记录"));
            this.kitchenBrightRepository.delete(record);
        }
    }

}
