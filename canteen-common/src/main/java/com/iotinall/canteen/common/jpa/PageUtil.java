package com.iotinall.canteen.common.jpa;

import com.iotinall.canteen.common.protocol.CursorPageDTO;
import com.iotinall.canteen.common.protocol.PageDTO;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author xin-bing
 * @date 10/16/2019 16:33
 */
public class PageUtil {
    @SuppressWarnings("unchecked")
    public static <T> PageDTO<T> toPageDTO(Page<T> page) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setContent(page.getContent());
        pageDTO.setTotal(page.getTotalElements());
        return pageDTO;
    }

    /**
     * @param data 实际的list data
     * @param page 分页的数据
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> PageDTO<T> toPageDTO(List<T> data, Page<?> page) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotal(page.getTotalElements());
        pageDTO.setContent(data);
        return pageDTO;
    }

    /**
     * @param data  实际的list data
     * @param total 总数目
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> PageDTO<T> toPageDTO(List<T> data, Long total) {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotal(total);
        pageDTO.setContent(data);
        return pageDTO;
    }

    /**
     * 空分页
     * @param <T>
     * @return
     */
    public static <T> PageDTO<T> empPage(){
        PageDTO pageDTO = new PageDTO();
        pageDTO.setTotal(0L);
        pageDTO.setContent(Collections.emptyList());
        return pageDTO;
    }

    public static <T> CursorPageDTO<T> toCursorPageDTO(List<T> data, Serializable cursor) {
        CursorPageDTO pageDTO = new CursorPageDTO();
        pageDTO.setContent(data);
        pageDTO.setCursor(cursor);
        return pageDTO;
    }

    public static <T> CursorPageDTO<T> toCursorPageDTO(List<T> data, Serializable cursor, Long total) {
        CursorPageDTO pageDTO = new CursorPageDTO();
        pageDTO.setContent(data);
        pageDTO.setCursor(cursor);
        pageDTO.setTotal(total);
        return pageDTO;
    }
}
