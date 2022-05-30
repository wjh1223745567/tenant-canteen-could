package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.WorkOvertimeApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface WorkOvertimeApplyRepository extends JpaRepository<WorkOvertimeApply, Long>, JpaSpecificationExecutor<WorkOvertimeApply> {

    /**
     * 审核申请
     *
     * @param id
     * @return
     */
    @Modifying
    @Query("update WorkOvertimeApply o SET o.state = ?1,o.auditOpinion = ?3 where o.id = ?2 and o.state = 0 ")
    Integer applyData(Integer state, Long id, String auditOpinion);

    @Query("select o from WorkOvertimeApply o where o.empId = ?1 and o.thisDay = ?2 and o.state = 1")
    List<WorkOvertimeApply> findApply(Long empId, LocalDate date);

    @Query("select o from WorkOvertimeApply o where o.empId = ?1 and o.thisDay = ?2")
    WorkOvertimeApply findApplyAll(Long empId, LocalDate date);
}
