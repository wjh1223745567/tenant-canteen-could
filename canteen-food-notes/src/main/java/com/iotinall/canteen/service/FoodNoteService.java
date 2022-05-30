package com.iotinall.canteen.service;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.dto.foodnote.FoodNoteAddReq;
import com.iotinall.canteen.dto.foodnote.FoodNoteAppQueryCriteria;
import com.iotinall.canteen.dto.foodnote.FoodNoteDTO;
import com.iotinall.canteen.dto.foodnote.FoodNoteDetailDTO;
import com.iotinall.canteen.dto.foodnote.FoodNoteEditReq;
import com.iotinall.canteen.dto.foodnote.FoodNoteQueryCriteria;
import com.iotinall.canteen.dto.foodnotecommentrecord.FoodNoteCommentRecordDTO;
import com.iotinall.canteen.entity.FoodNote;
import com.iotinall.canteen.entity.FoodNoteCollectRecord;
import com.iotinall.canteen.entity.FoodNoteCommentRecord;
import com.iotinall.canteen.entity.FoodNoteFollowRecord;
import com.iotinall.canteen.entity.FoodNoteLikeRecord;
import com.iotinall.canteen.entity.FoodNoteType;
import com.iotinall.canteen.repository.FoodNoteCollectRecordRepository;
import com.iotinall.canteen.repository.FoodNoteCommentLikeRecordRepository;
import com.iotinall.canteen.repository.FoodNoteCommentRecordRepository;
import com.iotinall.canteen.repository.FoodNoteFollowRecordRepository;
import com.iotinall.canteen.repository.FoodNoteLikeRecordRepository;
import com.iotinall.canteen.repository.FoodNoteRepository;
import com.iotinall.canteen.repository.FoodNoteTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 美食笔记Service
 * @author: JoeLau
 * @time: 2021年07月06日 19:46:47
 */

@Service
@Slf4j
public class FoodNoteService {
    @Resource
    private FoodNoteRepository foodNoteRepository;
    @Resource
    private FoodNoteTypeRepository foodNoteTypeRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;
    @Resource
    private FoodNoteFollowRecordRepository foodNoteFollowRecordRepository;
    @Resource
    private FoodNoteCollectRecordRepository foodNoteCollectRecordRepository;
    @Resource
    private FoodNoteLikeRecordRepository foodNoteLikeRecordRepository;
    @Resource
    private FoodNoteCommentRecordRepository foodNoteCommentRecordRepository;
    @Resource
    private FoodNoteCommentService foodNoteCommentService;
    @Resource
    private FoodNoteCommentLikeRecordRepository foodNoteCommentLikeRecordRepository;

    /**
     * @return
     * @Author JoeLau
     * @Description 查询美食笔记分页
     * @Date 2021/7/7  14:55
     * @Param
     */
    public PageDTO<FoodNoteDTO> page(FoodNoteQueryCriteria criteria, Pageable pageable) {
        LocalDateTime begin = null, end = null;
        if (StringUtils.isNotBlank(criteria.getStartTime()) && StringUtils.isNotBlank(criteria.getEndTime())) {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            begin = LocalDateTime.parse(criteria.getStartTime() + " 00:00:00", df);
            end = LocalDateTime.parse(criteria.getEndTime() + " 23:59:59", df);
            if (begin.isAfter(end)) {
                throw new BizException("开始时间不能在结束时间之后");
            }
        }
        Specification<FoodNote> specification = SpecificationBuilder.builder()
                .where(Criterion.eq("status", true),
                        Criterion.eq("foodNoteType.id", criteria.getTypeId()),
                        Criterion.gte("createTime", begin),
                        Criterion.lte("createTime", end))
                .whereByOr(Criterion.like("authorName", criteria.getKeywords()),
                        Criterion.like("title", criteria.getKeywords()))
                .build();
        Page<FoodNote> page = foodNoteRepository.findAll(specification, pageable);
        List<FoodNoteDTO> list = page.getContent().stream().map(this::transform).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    /**
     * @return
     * @Author JoeLau
     * @Description APP查询美食笔记分页
     * @Date 2021/7/7  14:55
     * @Param
     */
    public PageDTO<FoodNoteDTO> appPage(FoodNoteAppQueryCriteria criteria, Pageable pageable) {
        if (null != criteria.getFollowStatus() && criteria.getFollowStatus()) {
            List<Long> authorIds = this.foodNoteFollowRecordRepository.findAllByUserId(SecurityUtils.getUserId());
            Page<FoodNote> page = this.foodNoteRepository.findAllByStatusAndAuthorIdIn(true, authorIds, pageable);
            List<FoodNoteDTO> list = page.getContent().stream().map(this::transform).collect(Collectors.toList());
            return PageUtil.toPageDTO(list, page);
        }
        Specification<FoodNote> specification = SpecificationBuilder.builder()
                .where(Criterion.eq("status", true),
                        Criterion.eq("foodNoteType.id", criteria.getTypeId()))
                .whereByOr(Criterion.like("authorName", criteria.getKeywords()),
                        Criterion.like("title", criteria.getKeywords()))
                .build();
        Page<FoodNote> page = foodNoteRepository.findAll(specification, pageable);
        List<FoodNoteDTO> list = page.getContent().stream().map(this::transform).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 查看自己的美食笔记 包括发布的和在草稿箱的
     * @Date 2021/7/8  19:45
     * @Param
     */
    public PageDTO<FoodNoteDTO> authorPage(Boolean status, Pageable pageable) {
        if (null == status) {
            status = true;
        }
        Page<FoodNote> page = this.foodNoteRepository.findAllByStatusAndAuthorId(status, SecurityUtils.getUserId(), pageable);
        List<FoodNoteDTO> list = page.getContent().stream().map(this::transform).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 查看美食笔记详情
     * @Date 2021/7/8  9:37
     * @Param
     */
    public FoodNoteDetailDTO detail(Long id) {
        FoodNote foodNote = this.foodNoteRepository.findById(id).orElseThrow(() -> new BizException("该笔记不存在"));
        FoodNoteDetailDTO foodNoteDetailDTO = new FoodNoteDetailDTO();
        foodNoteDetailDTO.setId(foodNote.getId());
        foodNoteDetailDTO.setCreateTime(foodNote.getCreateTime());
        foodNoteDetailDTO.setTitle(foodNote.getTitle());
        foodNoteDetailDTO.setAuthorId(foodNote.getAuthorId());
        foodNoteDetailDTO.setAuthorName(foodNote.getAuthorName());
        foodNoteDetailDTO.setAuthorAvatar(foodNote.getAuthorAvatar());
        foodNoteDetailDTO.setFoodNoteTypeName(foodNote.getFoodNoteType().getName());
        foodNoteDetailDTO.setFoodNoteTypeId(foodNote.getFoodNoteType().getId());
        foodNoteDetailDTO.setFoodNoteLikeNum(this.foodNoteLikeRecordRepository.countAllByFoodNote(foodNote));
        foodNoteDetailDTO.setFoodNoteCommentNum(this.foodNoteCommentRecordRepository.countAllByFoodNote(foodNote));
        foodNoteDetailDTO.setFoodNoteCollectNum(this.foodNoteCollectRecordRepository.countAllByFoodNote(foodNote));
        foodNoteDetailDTO.setFoodNoteReadingNum(foodNote.getFoodNoteReadingNum());
        foodNoteDetailDTO.setStatus(foodNote.getStatus());

        //手动Json解析
        List<String> pictureList = new ArrayList<>();
        String[] pictures = StringUtils.split(foodNote.getPictures(), ",");
        for (String picture : pictures) {
            pictureList.add(ImgPair.getFileServer() + picture);
        }
//        List<String> pictureList = Arrays.asList(pictures);
        foodNoteDetailDTO.setPictureList(pictureList);

        foodNoteDetailDTO.setContent(foodNote.getContent());
        //查找该笔记所有根评论
        List<FoodNoteCommentRecord> list = this.foodNoteCommentRecordRepository.findAllByFoodNoteAndAndLastId(foodNote, null);
        //调用返回评论DTO方法,并赋值
        foodNoteDetailDTO.setCommentRecordDTOList(this.getComments(list));
        //判断是否关注
        foodNoteDetailDTO.setFollowStatus(null != this.foodNoteFollowRecordRepository
                .findByAuthorIdAndUserId(foodNote.getAuthorId(), SecurityUtils.getUserId()));
        //判断是否收藏
        foodNoteDetailDTO.setCollectStatus(null != this.foodNoteCollectRecordRepository
                .findByUserIdAndFoodNote(SecurityUtils.getUserId(), foodNote));
        //判断是否点赞
        foodNoteDetailDTO.setLikeStatus(null != this.foodNoteLikeRecordRepository
                .findByUserIdAndFoodNote(SecurityUtils.getUserId(), foodNote));
        return foodNoteDetailDTO;
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 返回美食评论DTO
     * @Date 2021/7/9  11:24
     * @Param
     */
    private List<FoodNoteCommentRecordDTO> getComments(List<FoodNoteCommentRecord> commentRecordList) {
        List<FoodNoteCommentRecordDTO> commentRecordDTOList = new ArrayList<>();
        FoodNoteCommentRecordDTO commentRecordDTO;
        for (FoodNoteCommentRecord commentRecord : commentRecordList) {
            commentRecordDTO = new FoodNoteCommentRecordDTO();
            commentRecordDTO.setId(commentRecord.getId());
            commentRecordDTO.setCreateTime(commentRecord.getCreateTime());
            commentRecordDTO.setUserId(commentRecord.getUserId());
            commentRecordDTO.setUserName(commentRecord.getUserName());
            commentRecordDTO.setUserAvatar(commentRecord.getUserAvatar());
            commentRecordDTO.setComment(commentRecord.getComment());
            if (null != commentRecord.getLastId()) {
                commentRecordDTO.setLastId(commentRecord.getLastId());
            }
            //查该评论总的点赞数
            commentRecordDTO.setCommentLikeNum(this.foodNoteCommentLikeRecordRepository.countAllByFoodNoteCommentRecord(commentRecord));
            //查看该评论下的子评论
            List<FoodNoteCommentRecord> subCommentRecord = this.foodNoteCommentRecordRepository.findAllByFoodNoteAndAndLastId(commentRecord.getFoodNote(), commentRecord.getId());
            if (!CollectionUtils.isEmpty(subCommentRecord)) {
                commentRecordDTO.setCommentRecordDTOList(getComments(subCommentRecord));
            }
            commentRecordDTOList.add(commentRecordDTO);
        }
        return commentRecordDTOList;
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 实体转FoodNoteDTO
     * @Date 2021/7/8  16:23
     * @Param
     */
    public FoodNoteDTO transform(FoodNote foodNote) {
        FoodNoteDTO foodNoteDTO = new FoodNoteDTO();
        foodNoteDTO.setId(foodNote.getId());
        foodNoteDTO.setCreateTime(foodNote.getCreateTime());
        foodNoteDTO.setTitle(foodNote.getTitle());
        foodNoteDTO.setAuthorId(foodNote.getAuthorId());
        foodNoteDTO.setAuthorName(foodNote.getAuthorName());
        foodNoteDTO.setAuthorAvatar(foodNote.getAuthorAvatar());
        foodNoteDTO.setFoodNoteTypeName(foodNote.getFoodNoteType().getName());
        foodNoteDTO.setFoodNoteTypeId(foodNote.getFoodNoteType().getId());
        foodNoteDTO.setFoodNoteLikeNum(this.foodNoteLikeRecordRepository.countAllByFoodNote(foodNote));
        foodNoteDTO.setFoodNoteCommentNum(this.foodNoteCommentRecordRepository.countAllByFoodNote(foodNote));
        foodNoteDTO.setFoodNoteCollectNum(this.foodNoteCollectRecordRepository.countAllByFoodNote(foodNote));
        foodNoteDTO.setFoodNoteReadingNum(foodNote.getFoodNoteReadingNum());
        foodNoteDTO.setStatus(foodNote.getStatus());
        String[] pictures = StringUtils.split(foodNote.getPictures(), ",");
        foodNoteDTO.setCoverImg(pictures[0]);
        return foodNoteDTO;
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 添加美食笔记
     * @Date 2021/7/7  15:24
     * @Param
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(FoodNoteAddReq req) {
        FoodNoteType type = this.foodNoteTypeRepository.findById(req.getFoodNoteTypeId()).orElseThrow(() -> new BizException("该笔记类型不存在"));
        if (req.getPictureList().size() > 9) {
            throw new BizException("图片不能超过九张");
        }
        //手动Json序列化
        List<String> imgList = new ArrayList<>();
        for (String imgPath : req.getPictureList()) {
            if (imgPath.startsWith(ImgPair.getFileServer())) {
                imgPath = imgPath.replaceFirst(ImgPair.getFileServer(), "");
            }
            imgList.add(imgPath);
        }
        String picture = StringUtils.join(imgList, ",");

        FoodNote foodNote = new FoodNote();
        foodNote.setTitle(req.getTitle());
        foodNote.setContent(req.getContent());
        foodNote.setStatus(req.getStatus());
        foodNote.setFoodNoteType(type);
        foodNote.setPictures(picture);
        foodNote.setAuthorId(SecurityUtils.getUserId());
        String authorAvatar = feignEmployeeService.findById(SecurityUtils.getUserId()).getAvatar();
        if (StringUtils.isNotBlank(authorAvatar)) {
            foodNote.setAuthorAvatar(authorAvatar);
        }
        foodNote.setAuthorName(SecurityUtils.getUserName());
        //初始化浏览阅读量
        foodNote.setFoodNoteReadingNum(0);
        this.foodNoteRepository.save(foodNote);
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 编辑美食笔记
     * @Date 2021/7/8  9:24
     * @Param
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(FoodNoteEditReq req) {
        FoodNote foodNote = this.foodNoteRepository.findById(req.getId()).orElseThrow(() -> new BizException("未找到该美食笔记"));
        FoodNoteType type = this.foodNoteTypeRepository.findById(req.getFoodNoteTypeId()).orElseThrow(() -> new BizException("该笔记类型不存在"));
        if (req.getPictureList().size() > 9) {
            throw new BizException("图片不能超过九张");
        }
        //手动Json序列化
        List<String> imgList = new ArrayList<>();
        for (String imgPath : req.getPictureList()) {
            if (imgPath.startsWith(ImgPair.getFileServer())) {
                imgPath = imgPath.replaceFirst(ImgPair.getFileServer(), "");
            }
            imgList.add(imgPath);
        }
        String picture = StringUtils.join(imgList, ",");

        foodNote.setTitle(req.getTitle());
        foodNote.setContent(req.getContent());
        foodNote.setFoodNoteType(type);
        foodNote.setPictures(picture);
        //已发布的笔记编辑时不能改变已发布的状态
        if (foodNote.getStatus()) {
            this.foodNoteRepository.save(foodNote);
        } else {
            //草稿箱的笔记
            if (null != req.getStatus()) {
                foodNote.setStatus(req.getStatus());
            }
        }
        this.foodNoteRepository.save(foodNote);
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 批量删除笔记
     * @Date 2021/7/8  9:52
     * @Param
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long[] ids) {
        for (Long id : ids) {
            FoodNote foodNote = this.foodNoteRepository.findById(id).orElseThrow(() -> new BizException("未找到该美食笔记"));
            //删除对应收藏记录
            this.foodNoteCollectRecordRepository.deleteAllByFoodNote(foodNote);
            //删除对应点赞记录
            this.foodNoteLikeRecordRepository.deleteAllByFoodNote(foodNote);
            //删除对应评论记录
            List<Long> idList = this.foodNoteCommentRecordRepository.findAllByFoodNote(foodNote);
            if (!CollectionUtils.isEmpty(idList)) {
                Long[] commentIds = (Long[]) idList.toArray();
                this.foodNoteCommentService.delete(commentIds);
            }
            //最后删除美食笔记
            this.foodNoteRepository.delete(foodNote);
        }
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 阅读浏览量加1
     * @Date 2021/7/8  9:38
     * @Param
     */
    @Transactional(rollbackFor = Exception.class)
    public FoodNoteDetailDTO read(Long id) {
        FoodNote foodNote = this.foodNoteRepository.findById(id).orElseThrow(() -> new BizException("未找到该美食笔记"));
        foodNote.setFoodNoteReadingNum(foodNote.getFoodNoteReadingNum() + 1);
        return this.detail(id);
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 取消关注/关注笔记作者
     * @Date 2021/7/8  10:14
     * @Param id为该笔记的id
     */
    @Transactional(rollbackFor = Exception.class)
    public void follow(Long id, Boolean status) {
        FoodNote foodNote = this.foodNoteRepository.findById(id).orElseThrow(() -> new BizException("未找到该美食笔记"));
        Long authorId = foodNote.getAuthorId();
        Long userId = SecurityUtils.getUserId();
        if(null != authorId && authorId.equals(userId)){
            throw new BizException("不能关注自己");
        }
        if (status) {
            if (null != this.foodNoteFollowRecordRepository.findByAuthorIdAndUserId(authorId, userId)) {
                throw new BizException("已关注过该博主了");
            }
            FoodNoteFollowRecord foodNoteFollowRecord = new FoodNoteFollowRecord();
            foodNoteFollowRecord.setAuthorId(authorId);
            foodNoteFollowRecord.setUserId(userId);
            this.foodNoteFollowRecordRepository.save(foodNoteFollowRecord);
        } else {
            FoodNoteFollowRecord foodNoteFollowRecord = this.foodNoteFollowRecordRepository.findByAuthorIdAndUserId(authorId, userId);
            if (null == foodNoteFollowRecord) {
                throw new BizException("未关注该博主");
            }
            this.foodNoteFollowRecordRepository.delete(foodNoteFollowRecord);
        }
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 取消收藏/收藏该美食笔记
     * @Date 2021/7/8  10:53
     * @Param
     */
    @Transactional(rollbackFor = Exception.class)
    public void collect(Long id, Boolean status) {
        FoodNote foodNote = this.foodNoteRepository.findById(id).orElseThrow(() -> new BizException("未找到该美食笔记"));
        Long userId = SecurityUtils.getUserId();
        if (status) {
            if (null != this.foodNoteCollectRecordRepository.findByUserIdAndFoodNote(userId, foodNote)) {
                throw new BizException("已收藏过该笔记了");
            }
            FoodNoteCollectRecord foodNoteCollectRecord = new FoodNoteCollectRecord();
            foodNoteCollectRecord.setFoodNote(foodNote);
            foodNoteCollectRecord.setUserId(userId);
            this.foodNoteCollectRecordRepository.save(foodNoteCollectRecord);
        } else {
            FoodNoteCollectRecord foodNoteCollectRecord = this.foodNoteCollectRecordRepository.findByUserIdAndFoodNote(userId, foodNote);
            if (null == foodNoteCollectRecord) {
                throw new BizException("该笔记未收藏");
            }
            this.foodNoteCollectRecordRepository.delete(foodNoteCollectRecord);
        }
    }

    /**
     * @return
     * @Author JoeLau
     * @Description 取消点赞/点赞该美食笔记
     * @Date 2021/7/8  11:08
     * @Param
     */
    @Transactional(rollbackFor = Exception.class)
    public void like(Long id, Boolean status) {
        FoodNote foodNote = this.foodNoteRepository.findById(id).orElseThrow(() -> new BizException("未找到该美食笔记"));
        Long userId = SecurityUtils.getUserId();
        if (status) {
            if (null != this.foodNoteLikeRecordRepository.findByUserIdAndFoodNote(userId, foodNote)) {
                throw new BizException("已点赞过该笔记了");
            }
            FoodNoteLikeRecord foodNoteLikeRecord = new FoodNoteLikeRecord();
            foodNoteLikeRecord.setFoodNote(foodNote);
            foodNoteLikeRecord.setUserId(userId);
            this.foodNoteLikeRecordRepository.save(foodNoteLikeRecord);
        } else {
            FoodNoteLikeRecord foodNoteLikeRecord = this.foodNoteLikeRecordRepository.findByUserIdAndFoodNote(userId, foodNote);
            if (null == foodNoteLikeRecord) {
                throw new BizException("该笔记未点赞");
            }
            this.foodNoteLikeRecordRepository.delete(foodNoteLikeRecord);
        }
    }
}
