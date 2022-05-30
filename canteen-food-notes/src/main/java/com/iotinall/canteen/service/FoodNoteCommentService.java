package com.iotinall.canteen.service;

import cn.hutool.core.collection.CollectionUtil;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.foodnotecommentrecord.FoodNoteCommentRecordAddReq;
import com.iotinall.canteen.entity.FoodNote;
import com.iotinall.canteen.entity.FoodNoteCommentLikeRecord;
import com.iotinall.canteen.entity.FoodNoteCommentRecord;
import com.iotinall.canteen.repository.FoodNoteCommentLikeRecordRepository;
import com.iotinall.canteen.repository.FoodNoteCommentRecordRepository;
import com.iotinall.canteen.repository.FoodNoteRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 美食笔记评论Service
 * @author: JoeLau
 * @time: 2021年07月08日 11:29:46
 */

@Service
@Slf4j
public class FoodNoteCommentService {
    @Resource
    private FoodNoteCommentRecordRepository foodNoteCommentRecordRepository;
    @Resource
    private FoodNoteRepository foodNoteRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private FoodNoteCommentLikeRecordRepository foodNoteCommentLikeRecordRepository;

    /**
     * @return
     * @Author JoeLau
     * @Description 添加一个美食笔记评论
     * @Date 2021/7/8  14:06
     * @Param
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(FoodNoteCommentRecordAddReq req) {
        FoodNote foodNote = this.foodNoteRepository.findById(req.getFoodNoteId()).orElseThrow(() -> new BizException("该美食笔记不存在"));
        FoodNoteCommentRecord foodNoteCommentRecord = new FoodNoteCommentRecord();
        foodNoteCommentRecord.setFoodNote(foodNote);
        foodNoteCommentRecord.setUserId(SecurityUtils.getUserId());
        foodNoteCommentRecord.setUserName(SecurityUtils.getUserName());
        String avatar = feignEmployeeService.findById(SecurityUtils.getUserId()).getAvatar();
        if (StringUtils.isNotBlank(avatar)) {
            foodNoteCommentRecord.setUserAvatar(avatar);
        }
        foodNoteCommentRecord.setCreateTime(LocalDateTime.now());
        foodNoteCommentRecord.setComment(req.getComment());
        if (null != req.getLastId()) {
            FoodNoteCommentRecord commentRecord = this.foodNoteCommentRecordRepository.findById(req.getLastId()).orElseThrow(() -> new BizException("该评论不存在"));
            if (!commentRecord.getFoodNote().getId().equals(foodNote.getId())) {
                throw new BizException("该评论不在这篇笔记下，无法回复");
            }
            foodNoteCommentRecord.setLastId(req.getLastId());
        }
        this.foodNoteCommentRecordRepository.save(foodNoteCommentRecord);
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 删除
     * @Date 2021/7/8  15:03
     * @Param
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            FoodNoteCommentRecord foodNoteCommentRecord = this.foodNoteCommentRecordRepository.findById(id).orElseThrow(() -> new BizException("该评论不存在"));
            List<Long> idList = this.foodNoteCommentRecordRepository.findAllByLastId(id);
            if (!CollectionUtil.isEmpty(idList)) {
                Long[] nextIds = idList.toArray(new Long[ids.length]);
                delete(nextIds);
            }
            //删除对应评论的点赞记录
            this.foodNoteCommentLikeRecordRepository.deleteAllByFoodNoteCommentRecord(foodNoteCommentRecord);
            //删除评论
            this.foodNoteCommentRecordRepository.delete(foodNoteCommentRecord);
        }
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 取消点赞/点赞该美食笔记评论
     * @Date 2021/7/8  11:08
     * @Param
     */
    @Transactional(rollbackFor = Exception.class)
    public void like(Long id, Boolean status) {
        FoodNoteCommentRecord foodNoteCommentRecord = this.foodNoteCommentRecordRepository.findById(id).orElseThrow(() -> new BizException("未找到该评论"));
        Long userId = SecurityUtils.getUserId();
        if (status) {
            if (null != this.foodNoteCommentLikeRecordRepository.findByUserIdAndAndFoodNoteCommentRecord(userId, foodNoteCommentRecord)) {
                throw new BizException("已点赞过该评论了");
            }
            FoodNoteCommentLikeRecord foodNoteCommentLikeRecord = new FoodNoteCommentLikeRecord();
            foodNoteCommentLikeRecord.setFoodNoteCommentRecord(foodNoteCommentRecord);
            foodNoteCommentLikeRecord.setUserId(userId);
            this.foodNoteCommentLikeRecordRepository.save(foodNoteCommentLikeRecord);
        } else {
            FoodNoteCommentLikeRecord foodNoteCommentLikeRecord = this.foodNoteCommentLikeRecordRepository.findByUserIdAndAndFoodNoteCommentRecord(userId, foodNoteCommentRecord);
            if (null == foodNoteCommentLikeRecord) {
                throw new BizException("该评论未点赞");
            }
            this.foodNoteCommentLikeRecordRepository.delete(foodNoteCommentLikeRecord);
        }
    }

}
