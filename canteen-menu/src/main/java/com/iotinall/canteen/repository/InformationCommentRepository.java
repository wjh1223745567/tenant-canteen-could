package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.InformationComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author WJH
 * @date 2019/11/1518:06
 */
public interface InformationCommentRepository extends JpaRepository<InformationComment, Long>, JpaSpecificationExecutor<InformationComment> {

    /**
     * 赞
     * @param empId
     */
    @Modifying
    @Query("update InformationComment o set o.praise = o.praise+1,o.praiseEmp = concat(coalesce(o.praiseEmp,''), ',', :empId) where o.id=:commId ")
    void addPraise(@Param("commId") Long infocommentid, @Param("empId") Long empId);

    /**
     * 评论
     * @param infoId
     * @return
     */
    Long countAllByInformationId(Long infoId);
}
