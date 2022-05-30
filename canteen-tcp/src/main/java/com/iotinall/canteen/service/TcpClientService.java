package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.constant.TcpConstants;
import com.iotinall.canteen.dto.client.TcpClientAddReq;
import com.iotinall.canteen.dto.client.TcpClientDTO;
import com.iotinall.canteen.dto.client.TcpClientEditReq;
import com.iotinall.canteen.dto.tcpclient.FeignTcpClientDTO;
import com.iotinall.canteen.entity.TcpClient;
import com.iotinall.canteen.repository.TcpClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 客户端 service
 *
 * @author loki
 * @date 2021/04/14 14:34
 */
@Slf4j
@Service
public class TcpClientService {
    @Resource
    private TcpClientRepository tcpClientRepository;

    /**
     * 分页
     *
     * @author loki
     * @date 2021/6/25 11:14
     **/
    public Object page(String keywords, String type, Pageable pageable) {
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .whereByOr(Criterion.like("clientKey", keywords), Criterion.like("clientName", keywords))
                .where(Criterion.eq("type", type));
        Page<TcpClient> pageResult = this.tcpClientRepository.findAll(builder.build(), pageable);
        if (CollectionUtils.isEmpty(pageResult.getContent())) {
            return PageUtil.toPageDTO(Collections.EMPTY_LIST, pageResult);
        }

        List<TcpClientDTO> result = pageResult.getContent().stream().map(this::convert).collect(Collectors.toList());

        return PageUtil.toPageDTO(result, pageResult);
    }

    /**
     * 转换
     *
     * @author loki
     * @date 2021/7/21 9:20
     **/
    private TcpClientDTO convert(TcpClient tcpClient) {
        TcpClientDTO tcpClientDTO = new TcpClientDTO();
        BeanUtils.copyProperties(tcpClient, tcpClientDTO);
        return tcpClientDTO;
    }

    /**
     * 所有客户端
     *
     * @author loki
     * @date 2021/7/20 20:33
     **/
    public Object all() {
        return this.tcpClientRepository.findAll().stream().map(this::convert).collect(Collectors.toList());
    }

    /**
     * 创建
     *
     * @author loki
     * @date 2021/6/25 11:14
     **/
    public void create(TcpClientAddReq req) {
        TcpClient tcpClient = this.tcpClientRepository.findByClientKey(req.getClientKey());
        if (tcpClient != null) {
            throw new BizException("客户端key已存在");
        }

        //校验key是否存在
        tcpClient = new TcpClient();
        BeanUtils.copyProperties(req, tcpClient);

        this.tcpClientRepository.save(tcpClient);
    }

    /**
     * 更新
     *
     * @author loki
     * @date 2021/6/25 11:15
     **/
    public void update(TcpClientEditReq req) {
        TcpClient tcpClient = this.tcpClientRepository.findById(req.getId()).orElseThrow(() -> new BizException("记录不存在"));

        TcpClient tcpClient1 = this.tcpClientRepository.findByClientKey(req.getClientKey());
        if (tcpClient1 != null && !tcpClient1.getId().equals(req.getId())) {
            throw new BizException("客户端key已存在");
        }

        BeanUtils.copyProperties(req, tcpClient);

        this.tcpClientRepository.save(tcpClient);
    }

    /**
     * 删除
     *
     * @author loki
     * @date 2021/6/25 11:15
     **/
    public void delete(Long[] ids) {
        if (ids.length <= 0) {
            throw new BizException("请选择需要删除的内容");
        }

        for (Long id : ids) {
            this.tcpClientRepository.deleteById(id);
        }
    }

    /**
     * 校验客户端是否已注册
     *
     * @author loki
     * @date 2021/6/28 14:09
     **/
    public TcpClient getTcpClient(String clientId) {
        return this.tcpClientRepository.findByClientKey(clientId);
    }

    /**
     * 客户端心跳
     *
     * @author loki
     * @date 2021/7/8 10:26
     **/
    public void heartbeat(TcpClient tcpClient) {
        if (null != tcpClient) {
            tcpClient.setStatus(TcpConstants.CLIENT_STATUS_ONLINE);
            tcpClient.setLastSeen(LocalDateTime.now());
            this.tcpClientRepository.save(tcpClient);
        }
    }

    /**
     * 其他服务获取客户端
     *
     * @author loki
     * @date 2021/7/7 11:52
     **/
    public FeignTcpClientDTO findById(Long id) {
        Optional<TcpClient> client = this.tcpClientRepository.findById(id);
        return client.isPresent() ?
                new FeignTcpClientDTO()
                        .setClientId(client.get().getId())
                        .setClientName(client.get().getClientName()) :
                null;
    }
}
