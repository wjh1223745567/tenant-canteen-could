package com.iotinall.canteen.netty.manager.factory;

import com.iotinall.canteen.constant.NettyConstants;
import com.iotinall.canteen.netty.manager.server.ServerChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * netty 初始化
 *
 * @author loki
 * @date 2020/06/02 16:15
 */
@Slf4j
public class NettyFactory {

    private Integer iKeepHttpConnectionPort;

    public NettyFactory(Integer iKeepHttpConnectionPort) {
        this.iKeepHttpConnectionPort = iKeepHttpConnectionPort;
    }

    public void createNetty() {
        EventLoopGroup bossGroup;
        EventLoopGroup workerGroup;

        bossGroup = new NioEventLoopGroup(NettyConstants.BOSS_GROUP_THREAD_NUM);
        workerGroup = new NioEventLoopGroup(NettyConstants.WORK_GROUP_THREAD_NUM);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup);
            b.channel(NioServerSocketChannel.class);

            //服务初始化通道处理
            b.childHandler(new ServerChannelInitializer());

            //等待处理的队列大小
            b.option(ChannelOption.SO_BACKLOG, NettyConstants.DEAL_BLOCK_SIZE);

            //Boss线程内存池配置.
            b.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    //Work线程内存池配置.
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, AdaptiveRecvByteBufAllocator.DEFAULT);
            //是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活。
            b.childOption(ChannelOption.SO_KEEPALIVE, true);

            // 绑定端口
            ChannelFuture f = b.bind(iKeepHttpConnectionPort).sync();
            //sync()会同步等待连接操作结果，用户线程将在此wait()，直到连接操作完成之后，线程被notify(),用户代码继续执行
            //closeFuture()当Channel关闭时返回一个ChannelFuture,用于链路检测
            log.info("[NETTY][CREATE][PORT = {}]  ...... START ......", iKeepHttpConnectionPort);
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            //资源优雅释放
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
