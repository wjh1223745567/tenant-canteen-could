package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.dto.client.TcpClientAddReq;
import com.iotinall.canteen.dto.client.TcpClientEditReq;
import com.iotinall.canteen.dto.tcpclient.FeignTcpClientDTO;
import com.iotinall.canteen.service.TcpClientService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * tcp client controller
 *
 * @author loki
 * @date 2021/6/25 11:04
 **/
@RestController
@RequestMapping("tcp-client")
public class TcpClientController {
    @Resource
    private TcpClientService tcpClientService;

    @GetMapping
    public ResultDTO page(@RequestParam(value = "keywords", required = false) String keywords,
                          @RequestParam(value = "type", required = false) String type,
                          Pageable pageable) {
        return ResultDTO.success(this.tcpClientService.page(keywords, type, pageable));
    }

    @GetMapping(value = "all")
    public ResultDTO page() {
        return ResultDTO.success(this.tcpClientService.all());
    }

    @PostMapping
    public ResultDTO create(@Valid @RequestBody TcpClientAddReq req) {
        this.tcpClientService.create(req);
        return ResultDTO.success();
    }

    @PutMapping
    public ResultDTO update(@Valid @RequestBody TcpClientEditReq req) {
        this.tcpClientService.update(req);
        return ResultDTO.success();
    }

    @DeleteMapping
    public ResultDTO delete(@RequestParam("batch") Long[] ids) {
        this.tcpClientService.delete(ids);
        return ResultDTO.success();
    }

    @GetMapping(value = "feign/findById/{id}")
    public FeignTcpClientDTO findById(@PathVariable("id") Long id) {
        return this.tcpClientService.findById(id);
    }
}
