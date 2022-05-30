package com.iotinall.canteen.repository;

import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.TcpClient;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * TCP客户端
 *
 * @author loki
 * @date 2021/04/14 14:37
 */
public interface TcpClientRepository extends JpaRepositoryEnhance<TcpClient, Long>, JpaSpecificationExecutor<TcpClient> {
    /**
     * 通过tcp客户端key获取客户端信息
     */
    TcpClient findByClientKey(String key);
}
