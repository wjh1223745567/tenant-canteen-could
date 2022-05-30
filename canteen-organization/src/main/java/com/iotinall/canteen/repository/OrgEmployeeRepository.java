package com.iotinall.canteen.repository;


import com.iotinall.canteen.common.jpa.enhance.JpaRepositoryEnhance;
import com.iotinall.canteen.entity.Org;
import com.iotinall.canteen.entity.OrgEmployee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 组织员工 Repository
 *
 * @author xin-bing
 * @date 2019-10-24 13:55:41
 */
public interface OrgEmployeeRepository extends JpaRepositoryEnhance<OrgEmployee, Long>, JpaSpecificationExecutor<OrgEmployee> {

    List<OrgEmployee> findAllByOrgIn(List<Org> orgs);

    /**
     * 根据身份证查询用户信息
     *
     * @author loki
     * @date 2020/04/24 14:40
     */
    OrgEmployee queryByIdNoAndDeletedIsFalse(String idNo);

    OrgEmployee queryByIdNo(String idNo);

    /**
     * 根据手机号查询用户信息
     *
     * @author loki
     * @date 2020/04/24 14:40
     */
    OrgEmployee queryByMobileAndDeletedIsFalse(String mobile);

    /**
     * 查询所有
     *
     * @param mobile
     * @return
     */
    OrgEmployee queryByMobile(String mobile);

    /**
     * 根据卡号查询用户信息
     *
     * @author loki
     * @date 2020/04/24 14:40
     */
    OrgEmployee queryByCardNoAndDeletedIsFalse(String cardNo);

    OrgEmployee queryByCardNo(String cardNo);

    /**
     * 根据类型查询人员信息
     *
     * @param type
     * @return
     */
    List<OrgEmployee> findAllByPersonnelTypeAndDeleted(Integer type, Boolean deleted);

    OrgEmployee findByIdNoAndDeleted(String idCard, Boolean deleted);

    Optional<OrgEmployee> findById(Long id);

    /**
     * 根据openid获取用户信息
     *
     * @author loki
     * @date 2020/04/28 14:31
     */
    OrgEmployee queryByOpenidAndDeletedIsFalse(String openId);

    /**
     * 根据工号查询用户信息
     */
    OrgEmployee queryByPersonCodeAndDeletedFalse(String personCode);

    /**
     * @return long
     * @Author JoeLau
     * @Description 根据部门ids查总人数
     * @Date 2021/7/15  15:53
     * @Param orgId
     */
    @Query(value = "select count(e.id) from org_employee e where e.org_id in (:ids)", nativeQuery = true)
    Integer countEmployee(@Param("ids") List<Long> ids);

    /**
     * 获取所有后厨人员
     */
    @Query(value = "select distinct e.* " +
            " from sys_emp_roles ser " +
            " join org_employee e on(e.id=ser.emp_id)" +
            " where deleted = false" +
            " and ser.role_id in (:roleIds) " +
            " and e.personnel_type = 1 " +
            " and if(:keywords!= null and :keywords!='',e.name like concat('%',:keywords,'%'),1=1)", nativeQuery = true)
    List<OrgEmployee> queryBackKitchenAll(@Param("roleIds") Set<Long> roleIds,
                                          @Param("keywords") String keywords);

    /**
     * 分页获取后厨人员
     */
    @Query(value = "select distinct e.* " +
            " from sys_emp_roles ser " +
            " join org_employee e on(e.id=ser.emp_id)" +
            " where deleted = false" +
            " and ser.role_id in (:roleIds) " +
            " and e.personnel_type = 1 " +
            " and if(:keywords!= null and :keywords!='',e.name like concat('%',:keywords,'%'),1=1)", nativeQuery = true)
    Page<OrgEmployee> queryBackKitchenPage(@Param("roleIds") Set<Long> roleIds,
                                           @Param("keywords") String keywords,
                                           Pageable page);

    /**
     * 分页员工关怀列表
     */
    @Query(value = "select distinct e.* " +
            " from sys_emp_roles ser " +
            " join org_employee e on(e.id=ser.emp_id)" +
            " where deleted = false" +
            " and ser.role_id in (:roleIds) " +
            " and e.personnel_type = 1 " +
            " and e.birthday_month_day>=:begin" +
            " and e.birthday_month_day<=:end", nativeQuery = true)
    Page<OrgEmployee> queryBackKitchenCare(@Param("roleIds") Set<Long> roleIds,
                                           @Param("begin") String begin,
                                           @Param("end") String end,
                                           Pageable page);

    /**
     * 分页员工入职周年列表
     */
    @Query(value = "select distinct e.* " +
            " from sys_emp_roles ser " +
            " join org_employee e on(e.id=ser.emp_id)" +
            " where deleted = false" +
            " and ser.role_id in (:roleIds) " +
            " and e.personnel_type = 1 " +
            " and e.entry_month_day>=:begin" +
            " and e.entry_month_day<=:end", nativeQuery = true)
    Page<OrgEmployee> queryBackKitchenAnniversary(@Param("roleIds") Set<Long> roleIds,
                                           @Param("begin") String begin,
                                           @Param("end") String end,
                                           Pageable page);
}