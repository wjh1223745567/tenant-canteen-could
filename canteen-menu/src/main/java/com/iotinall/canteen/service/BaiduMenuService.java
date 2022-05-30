package com.iotinall.canteen.service;

import com.iotinall.canteen.dto.menubrief.AllSearchDto;
import com.iotinall.canteen.dto.menubrief.SearchDto;
import com.iotinall.canteen.entity.MessProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class BaiduMenuService {

    @Resource
    private BaiduMenuApi api;

    /**
     * 添加百度自定义菜谱
     * @param messProduct
     */
    public void addMenu(MessProduct messProduct){
//        api.dishAdd(messProduct);
    }

    /**
     * 删除百度菜谱
     * @param messProduct
     */
    public void deletedMenu(MessProduct messProduct){
//        api.dishDelete(messProduct);
    }

    /**
     * 搜索自定义菜谱
     */
    public SearchDto searchSelfMenu(String base64){
//        return api.dishSearch(base64);
        return null;
    }

    /**
     * 搜索所有菜谱
     * @param base64
     * @return
     */
    public AllSearchDto searchMenu(String base64){
//        return api.dishSearchAll(base64);
        return null;
    }
}
