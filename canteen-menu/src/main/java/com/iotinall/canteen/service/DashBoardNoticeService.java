package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.dto.notice.DashboardNoticeAdd;
import com.iotinall.canteen.dto.notice.DashboardNoticeEdit;
import com.iotinall.canteen.dto.notice.FeignDashboardNoticeDTO;
import com.iotinall.canteen.entity.DashBoardNotice;
import com.iotinall.canteen.repository.DashboardNoticeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DashBoardNoticeService {
    @Resource
    private DashboardNoticeRepository dashboardNoticeRepository;

    public List<FeignDashboardNoticeDTO> list() {
        List<DashBoardNotice> list = this.dashboardNoticeRepository.findAll();
        return list.stream().map(notice -> {
            FeignDashboardNoticeDTO dashboardNoticeDTO = new FeignDashboardNoticeDTO();
            BeanUtils.copyProperties(notice, dashboardNoticeDTO);
            return dashboardNoticeDTO;
        }).collect(Collectors.toList());
    }

    public Object add(DashboardNoticeAdd add) {
        DashBoardNotice dashBoardNotice = new DashBoardNotice();
        BeanUtils.copyProperties(add, dashBoardNotice);
        return this.dashboardNoticeRepository.save(dashBoardNotice);
    }

    public Object edit(DashboardNoticeEdit edit) {
        DashBoardNotice dashBoardNotice = this.dashboardNoticeRepository.findById(edit.getId()).orElseThrow(
                () -> new BizException("通知信息不存在")
        );
        BeanUtils.copyProperties(edit, dashBoardNotice);
        return this.dashboardNoticeRepository.save(dashBoardNotice);
    }

    public Object delete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("请选择需要删除的通知信息");
        }
        List<DashBoardNotice> noticeList = new ArrayList<>();
        DashBoardNotice dashBoardNotice;
        for (Long id : ids) {
            dashBoardNotice = this.dashboardNoticeRepository.findById(id).orElseThrow(() -> new BizException("", "删除内容不存在"));
            noticeList.add(dashBoardNotice);
        }
        dashboardNoticeRepository.deleteAll(noticeList);
        return noticeList;
    }
}
