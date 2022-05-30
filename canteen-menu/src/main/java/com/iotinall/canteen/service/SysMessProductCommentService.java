package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.dto.messprod.MessProductSimpleDTO;
import com.iotinall.canteen.dto.messprod.SysMessProductCommentDTO;
import com.iotinall.canteen.dto.recommend.MessProductCommentAddReq;
import com.iotinall.canteen.entity.MessDailyMenu;
import com.iotinall.canteen.entity.MessDailyMenuItem;
import com.iotinall.canteen.entity.MessProduct;
import com.iotinall.canteen.entity.MessProductComment;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.repository.MessDailyMenuRepository;
import com.iotinall.canteen.repository.MessProductCommentRepository;
import com.iotinall.canteen.repository.MessProductRepository;
import com.iotinall.canteen.utils.BigDecimalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 菜品评论 ServiceImpl
 *
 * @author xin-bing
 * @date 2019-10-21 16:07:57
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SysMessProductCommentService {
    @Resource
    private MessDailyMenuRepository messDailyMenuRepository;
    @Resource
    private MessProductCommentRepository commentRepository;
    @Resource
    private MessProductRepository messProductRepository;

    @Resource
    private FeignEmployeeService feignEmployeeService;

    public Object page(Long productId, Pageable page) {
        SpecificationBuilder spec = SpecificationBuilder.builder()
                .where(Criterion.eq("productId", productId));

        Page<MessProductComment> result = this.commentRepository.findAll(spec.build(), page);
        if (CollectionUtils.isEmpty(result.getContent())) {
            return PageUtil.toPageDTO(result);
        }

        List<SysMessProductCommentDTO> commentList = new ArrayList<>();
        SysMessProductCommentDTO commentDTO;
        for (MessProductComment comment : result.getContent()) {
            commentDTO = new SysMessProductCommentDTO();
            commentList.add(commentDTO);
            BeanUtils.copyProperties(comment, commentDTO);
        }

        return PageUtil.toPageDTO(commentList, result);
    }

    /**
     * 菜品详情
     *
     * @date 2021/01/11 10:58
     */
    public Object detail(Long productId, Integer mealType) {
        MessProduct messProduct = this.messProductRepository.findById(productId).orElseThrow(() -> new BizException("", "菜品不存在"));

        MessProductSimpleDTO messProductSimpleDTO = new MessProductSimpleDTO();
        messProductSimpleDTO.setId(messProduct.getId());
        messProductSimpleDTO.setName(messProduct.getName());
        messProductSimpleDTO.setImg(messProduct.getImg());

        //统计该菜谱的平均得分
        Long star = this.commentRepository.messProductAvgStar(productId);
        messProductSimpleDTO.setScore(null == star ? 0 : star);

        //厨师
        MessDailyMenu menu = messDailyMenuRepository.findByMenuDate(LocalDate.now());
        if (null != menu && !CollectionUtils.isEmpty(menu.getMenuItems())) {
            Optional<MessDailyMenuItem> result = menu.getMenuItems().stream().filter(item -> item.getProduct().getId().equals(productId)
                    && item.getMealType().getCode() == mealType).findFirst();
            if (result.isPresent() && result.get().getCookId() != null) {
                FeignEmployeeDTO feignEmployeeDTO = feignEmployeeService.findById(result.get().getCookId());
                messProductSimpleDTO.setCookName(feignEmployeeDTO.getName());
                messProductSimpleDTO.setCookImg(feignEmployeeDTO.getAvatar());
            }
        }
        return messProductSimpleDTO;
    }

    /**
     * 添加评论
     *
     * @date 2021/01/11 9:56
     */
    public Object addComment(MessProductCommentAddReq req) {
        MessProduct messProduct = this.messProductRepository.findById(req.getProductId()).orElseThrow(() -> new BizException("", "菜品不存在"));

        MessProductComment comment = new MessProductComment();
        comment.setProductId(req.getProductId());
        comment.setAnonymous(true);
        comment.setContent(req.getContent());
        comment.setFavorCount(0);
        comment.setOppositeCount(0);
        comment.setCreateTime(LocalDateTime.now());
        comment.setScore(req.getScore());
        comment.setTags(req.getTags());

        //平均分
        Long score = commentRepository.messProductAvgStar(messProduct.getId());
        messProduct.setAvgScore(null==score ? BigDecimal.ZERO:BigDecimal.valueOf(score));

        //好评率
        messProduct.setFavorRate(calculateFavorRate(messProduct.getId()));
        messProductRepository.save(messProduct);

        return this.commentRepository.save(comment);
    }

    /**
     * 计算好评率
     * 评分>=3的评分数/总评分数
     *
     * @date 2020/04/24 13:41
     */
    private BigDecimal calculateFavorRate(Long messProductId) {
        Long totalCount = this.commentRepository.countAllByProductId(messProductId);
        if (0 == totalCount) {
            return BigDecimal.ZERO;
        }

        Long favourCount = this.commentRepository.countAllByProductIdAndScoreGreaterThanEqual(messProductId, new BigDecimal(3));

        return BigDecimalUtil.divide(new BigDecimal(favourCount), new BigDecimal(totalCount), 1);
    }

    /**
     * 点击赞
     *
     * @date 2021/01/11 10:11
     */
    public void addFavor(Long id) {
        Optional<MessProductComment> optinalComment = this.commentRepository.findById(id);
        if (!optinalComment.isPresent()) {
            throw new BizException("", "操作失败，评论不存在");
        }

        MessProductComment comment = optinalComment.get();
        comment.setFavorCount(comment.getFavorCount() + 1);
        this.commentRepository.save(comment);
    }

    /**
     * 取消赞
     *
     * @date 2021/01/11 10:11
     */
    public void cancelFavor(Long id) {
        log.info("评论ID：{}", id);
        Optional<MessProductComment> optinalComment = this.commentRepository.findById(id);
        if (!optinalComment.isPresent()) {
            throw new BizException("", "操作失败，评论不存在");
        }

        MessProductComment comment = optinalComment.get();
        comment.setFavorCount(comment.getFavorCount() - 1);
        this.commentRepository.save(comment);
    }

    /**
     * 点击赞
     *
     * @date 2021/01/11 10:11
     */
    public void addOpposite(Long id) {
        MessProductComment comment = this.commentRepository.findById(id).orElseThrow(() -> new BizException("", "操作失败，评论不存在"));
        comment.setOppositeCount(comment.getOppositeCount() + 1);
        this.commentRepository.save(comment);
    }

    /**
     * 取消赞
     *
     * @date 2021/01/11 10:11
     */
    public void cancelOpposite(Long id) {
        MessProductComment comment = this.commentRepository.findById(id).orElseThrow(() -> new BizException("", "操作失败，评论不存在"));
        comment.setOppositeCount(comment.getOppositeCount() - 1);
        this.commentRepository.save(comment);
    }
}