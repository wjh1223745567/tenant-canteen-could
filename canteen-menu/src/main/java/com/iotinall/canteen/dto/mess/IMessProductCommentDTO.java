package com.iotinall.canteen.dto.mess;

/**
 * 推荐菜谱列表
 *
 * @author loki
 * @date 2021/02/26 9:17
 */
public interface IMessProductCommentDTO {
    Long getProductId();

    Long getCount();

    String getName();

    String getImg();
}
