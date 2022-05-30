package com.iotinall.canteen.service;


import com.iotinall.canteen.common.constant.FileConstant;
import com.iotinall.canteen.common.constant.TenantOrganizationTypeEnum;
import com.iotinall.canteen.common.entity.BaseEntity;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.common.util.FileHandler;
import com.iotinall.canteen.common.util.ImgPair;
import com.iotinall.canteen.common.util.RedisCacheUtil;
import com.iotinall.canteen.constant.DateTimeFormatters;
import com.iotinall.canteen.constant.PersonalType;
import com.iotinall.canteen.dto.employee.KitchenCookDTO;
import com.iotinall.canteen.dto.kitchen.KitchenMorningInspectRecordDTO;
import com.iotinall.canteen.dto.organization.*;
import com.iotinall.canteen.dto.orgemployee.*;
import com.iotinall.canteen.dto.storehouse.FeignTenantOrganizationDto;
import com.iotinall.canteen.entity.*;
import com.iotinall.canteen.repository.OrgEmployeeRepository;
import com.iotinall.canteen.repository.OrgRepository;
import com.iotinall.canteen.repository.SysRoleRepository;
import com.iotinall.canteen.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.persistence.criteria.JoinType;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 组织员工 ServiceImpl
 *
 * @author xin-bing
 * @date 2019-10-24 13:55:41
 */
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class OrgEmployeeService {
    @Resource
    private OrgEmployeeRepository orgEmployeeRepository;
    @Resource
    private OrgRepository orgRepository;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private OrgService orgService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysRoleRepository sysRoleRepository;
    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private FileHandler fileHandler;
    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;
    @Resource
    private FeignKitchenService feignKitchenService;


    final Base64.Decoder decoder = Base64.getDecoder();
    final Base64.Encoder encoder = Base64.getEncoder();
    private static final Double MAX_NORMOR_TEMPRATURE = 37.3;

    @SuppressWarnings("unchecked")
    // @Cacheable(value = RedisCacheUtil.SYS_ORG_EMP)
    public PageDTO<OrgEmployeeDTO> pageOrgEmployee(OrgEmployeeQueryCriteria criteria, Pageable pageable) {
        Page<OrgEmployee> page = this.findOrgEmployeePage(criteria, pageable);
        List<OrgEmployeeDTO> list = page.getContent().stream().map(this::convert).collect(Collectors.toList());
        return PageUtil.toPageDTO(list, page);
    }

    public Page<OrgEmployee> findOrgEmployeePage(OrgEmployeeQueryCriteria criteria, Pageable pageable) {
        Set<Long> orgChildrenIds = new HashSet<>(this.orgService.getAllChildOrg(Collections.singletonList(criteria.getOrgId())));
        if (criteria.getOrgId() != null) {
            orgChildrenIds.add(criteria.getOrgId());
        }

        SpecificationBuilder spec = SpecificationBuilder.builder()
                .fetch("org")
                .where(Criterion.in("org.id", orgChildrenIds))
                .where(Criterion.eq("personnelType", criteria.getType()))
                .where(Criterion.eq("deleted", 0),
                        Criterion.ne("mobile", "admin")
                )
                .whereByOr(
                        Criterion.like("name", criteria.getKeyword()),
                        Criterion.like("idNo", criteria.getKeyword()),
                        Criterion.like("mobile", criteria.getKeyword()),
                        Criterion.like("cardNo", criteria.getKeyword())
                );

        if (!SecurityUtils.getCurrentUser().isSupperAdmin()) {
            Set<Long> userTenantOrgIds = SecurityUtils.getCurrentUser().getRoleTenantOrgIds();
            Set<Long> childrenIds = feignTenantOrganizationService.findAllChildren(userTenantOrgIds);
            Set<Long> all = new HashSet<>();
            all.addAll(userTenantOrgIds);
            all.addAll(childrenIds);
            spec.fetch("roles", JoinType.INNER)
                    .where(
                            Criterion.in("roles.tenantOrgId", all)
                    );
            if (all.isEmpty()) {
                return Page.empty();
            }
        }
        return orgEmployeeRepository.findAll(spec.build(), pageable);
    }

    public OrgEmployeeDTO convert(OrgEmployee employee) {
        OrgEmployeeDTO orgEmployeeDTO = new OrgEmployeeDTO();
        BeanUtils.copyProperties(employee, orgEmployeeDTO);
        orgEmployeeDTO.setBalance(null != employee.getWallet() ? employee.getWallet().getBalance() : BigDecimal.ZERO);
        orgEmployeeDTO.setRoleIds(this.sysRoleService.findByEmpId(employee.getId()));
        if (employee.getOrg() != null) {
            orgEmployeeDTO.setOrgName(employee.getOrg().getName());
            orgEmployeeDTO.setOrgFullName(orgService.getOrgFullName(employee.getOrg()));
            orgEmployeeDTO.setOrgId(employee.getOrg().getId());
        }
        orgEmployeeDTO.setPersonnelTypeName(PersonalType.getByCode(orgEmployeeDTO.getPersonnelType()) != null ? Objects.requireNonNull(PersonalType.getByCode(orgEmployeeDTO.getPersonnelType())).getName() : null);
        return orgEmployeeDTO;
    }

    public OrgEmployeeDTO detail(Long id) {
        Optional<OrgEmployee> optional = orgEmployeeRepository.findById(id);
        if (!optional.isPresent()) {
            throw new BizException("", "记录不存在");
        }
        return convert(optional.get());
    }

    /**
     * 更新真实姓名
     *
     * @param name
     */
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisCacheUtil.APP_USER_INFO}, allEntries = true)
    public void updateName(String name) {
        Optional<OrgEmployee> orgEmployeeOptional = this.orgEmployeeRepository.findById(SecurityUtils.getUserId());
        if (orgEmployeeOptional.isPresent()) {
            OrgEmployee orgEmployee = orgEmployeeOptional.get();
            orgEmployee.setName(name);
            this.orgEmployeeRepository.save(orgEmployee);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisCacheUtil.SYS_ORG_EMP, RedisCacheUtil.SYS_ORG}, allEntries = true)
    public OrgEmployee add(OrgEmployeeAddReq req) {
        //校验员工身份证号是否已被使用
        this.checkIdNoExist(req.getIdNo());

        //校验员工手机号是否已被使用
        this.checkMobileExist(req.getMobile());

        //校验卡号是否存在
        this.checkCardNoExist(req.getCardNo());

        Optional<Org> orgById = orgRepository.findById(req.getOrgId());
        if (!orgById.isPresent()) {
            throw new BizException("", "组织不存在");
        }

        OrgEmployee orgEmployee = new OrgEmployee();
        BeanUtils.copyProperties(req, orgEmployee);

        LocalDateTime now = LocalDateTime.now();
        LocalDate birthday = StringUtils.getBirthday(req.getIdNo());
        orgEmployee.setBirthday(birthday);
        orgEmployee.setBirthdayMonthDay(birthday != null ? birthday.format(DateTimeFormatters.MMDD) : null);
        if (null != orgEmployee.getEntryDate()) {
            orgEmployee.setEntryMonthDay(orgEmployee.getEntryDate().format(DateTimeFormatters.MMDD));
        }
        orgEmployee.setOrg(orgById.get());
        orgEmployee.setAvatar(req.getAvatar());
        orgEmployee.setCreateTime(now);
        orgEmployee.setUpdateTime(now);
        orgEmployee.setDeleted(Boolean.FALSE);
        orgEmployee.setCardNo(req.getCardNo());
        orgEmployee.setPwd(passwordEncoder.encode(req.getPwd()));
        orgEmployee.setWallet(new EmployeeWallet(BigDecimal.ZERO));

        orgEmployee = orgEmployeeRepository.saveAndFlush(orgEmployee);

        orgService.addEmpCount(orgEmployee.getOrg().getId(), 1);

        //添加默认角色
        if (!CollectionUtils.isEmpty(req.getAddRoleIds())) {
            List<SysRole> roleList = sysRoleRepository.findAllById(req.getAddRoleIds());
            orgEmployee.setRoles(new HashSet<>(roleList));
        }

        //同步到门禁
        if (orgEmployee.getPersonnelType().equals(PersonalType.BACKKITCHEN.getCode())) {
            //TODO
//            this.createRedisTask(RedisConstants.SYNC_TYPE_ADD, orgEmployee.getId());
        }

        return orgEmployee;
    }

    @Transactional(rollbackFor = Exception.class)
    @Caching(evict = {
            @CacheEvict(value = {RedisCacheUtil.APP_USER_INFO}, key = "#req.id"),
            @CacheEvict(value = {RedisCacheUtil.SYS_ORG_EMP, RedisCacheUtil.SYS_ORG}, allEntries = true)
    })
    public Object update(OrgEmployeeEditReq req) {
        Optional<OrgEmployee> optional = orgEmployeeRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("", "记录不存在");
        }

        //校验员工身份证号是否已被使用
        this.checkIdNoExist(req.getIdNo(), req.getId());

        //校验员工手机号是否已被使用
        this.checkMobileExist(req.getMobile(), req.getId());

        //校验卡号是否已被使用
        this.checkCardNoExist(req.getCardNo(), req.getId());

        LocalDateTime now = LocalDateTime.now();
        OrgEmployee employee = optional.get();
        BeanUtils.copyProperties(req, employee);
        Optional<Org> byId = orgRepository.findById(req.getOrgId());
        if (!byId.isPresent()) {
            throw new BizException("", "组织不存在");
        }
        Long oldOrgId = employee.getOrg().getId();
        boolean orgChanged = !req.getOrgId().equals(oldOrgId);
        if (orgChanged) {
            employee.setOrg(byId.get());
        }
        employee.setUpdateTime(now);
        employee.setAvatar(req.getAvatar());

        LocalDate birthday = StringUtils.getBirthday(req.getIdNo());
        employee.setBirthday(birthday);
        employee.setBirthdayMonthDay(birthday != null ? birthday.format(DateTimeFormatters.MMDD) : null);
        if (null != employee.getEntryDate()) {
            employee.setEntryMonthDay(employee.getEntryDate().format(DateTimeFormatters.MMDD));
        }

        this.orgEmployeeRepository.saveAndFlush(employee);

        //添加角色
        if (req.getAddRoleIds() != null && !req.getAddRoleIds().isEmpty()) {
            List<SysRole> roleList = sysRoleRepository.findAllById(req.getAddRoleIds());
            employee.setRoles(new HashSet<>(roleList));
        } else if (req.getAddRoleIds() != null) {
            throw new BizException("", "请选择用户角色");
        }

        if (orgChanged) {
            orgService.amendEmpCount(oldOrgId, req.getOrgId(), 1);
        }

        return employee;
    }

    /**
     * 校验身份证号是否重复
     *
     * @author loki
     * @date 2020/04/24 14:38
     */
    private void checkIdNoExist(String idNo) {
        if (StringUtils.isNotBlank(idNo)) {
            OrgEmployee employee = this.orgEmployeeRepository.queryByIdNoAndDeletedIsFalse(idNo);
            if (null != employee) {
                throw new BizException("", "身份证号已被使用：" + idNo);
            }
        }
    }

    /**
     * 校验身份证号是否重复
     *
     * @author loki
     * @date 2020/04/24 14:38
     */
    private void checkIdNoExist(String idNo, Long employeeId) {
        if (org.apache.commons.lang3.StringUtils.isBlank(idNo)) {
            return;
        }

        OrgEmployee employee = this.orgEmployeeRepository.queryByIdNoAndDeletedIsFalse(idNo);
        if (null != employee) {
            if (employee.getId().longValue() != employeeId.longValue()) {
                throw new BizException("", "身份证号已被使用");
            }
        }
    }

    /**
     * 校验手机号是否重复
     *
     * @author loki
     * @date 2020/04/24 14:38
     */
    private void checkMobileExist(String mobile) {
        OrgEmployee employee = this.orgEmployeeRepository.queryByMobileAndDeletedIsFalse(mobile);
        if (null != employee) {
            throw new BizException("", "手机号已被使用：" + mobile);
        }
    }

    /**
     * 校验手机号是否重复
     *
     * @author loki
     * @date 2020/04/24 14:38
     */
    private void checkMobileExist(String mobile, Long employeeId) {
        OrgEmployee employee = this.orgEmployeeRepository.queryByMobile(mobile);
        if (null != employee) {
            if (employee.getId().longValue() != employeeId.longValue()) {
                throw new BizException("", "手机号已被使用：" + mobile);
            }
        }
    }

    /**
     * 校验卡号是否重复
     *
     * @author loki
     * @date 2020/04/24 14:38
     */
    private void checkCardNoExist(String cardNo) {
        OrgEmployee employee = this.orgEmployeeRepository.queryByCardNo(cardNo);
        if (null != employee) {
            throw new BizException("", "工号已存在");
        }
    }

    /**
     * 校验卡号是否重复
     *
     * @author loki
     * @date 2020/04/24 14:38
     */
    private void checkCardNoExist(String cardNo, Long employeeId) {
        OrgEmployee employee = this.orgEmployeeRepository.queryByCardNo(cardNo);
        if (null != employee) {
            if (employee.getId().longValue() != employeeId.longValue()) {
                throw new BizException("", "工号已存在");
            }
        }
    }

    /**
     * 查询所有厨师
     *
     * @return
     */
    public List<FeignCookDto> findAllCook(String keywords, Integer type) {
        return this.orgEmployeeRepository.queryBackKitchenAll(getRoleIdsList(TenantOrganizationTypeEnum.findByCode(type)),
                keywords)
                .stream().map(item -> new FeignCookDto()
                        .setId(item.getId())
                        .setName(item.getName())).collect(Collectors.toList());
    }

    /**
     * 分页获取后厨人员
     *
     * @author loki
     * @date 2021/7/22 9:50
     **/
    public Object findBackKitchenPage(String keywords, Pageable pageable) {
        Page<OrgEmployee> pageResult = this.orgEmployeeRepository.queryBackKitchenPage(getRoleIdsList(TenantOrganizationTypeEnum.DINING_HALL),
                keywords,
                pageable);

        return PageUtil.toPageDTO(pageResult.getContent().stream().map(this::convert).collect(Collectors.toList()), pageResult);
    }

    /**
     * 分页获取员工关怀列表
     *
     * @author loki
     * @date 2021/7/22 9:50
     **/
    public Object findBackKitchenCare(LocalDate month, Pageable pageable) {
        LocalDate firstDay = LocalDate.of(month.getYear(), month.getMonthValue(), 1);
        LocalDate lastDay = month.with(TemporalAdjusters.lastDayOfMonth());

        String begin = firstDay.format(DateTimeFormatters.MMDD);
        String end = lastDay.format(DateTimeFormatters.MMDD);

        Page<OrgEmployee> pageResult = this.orgEmployeeRepository.queryBackKitchenCare(
                getRoleIdsList(TenantOrganizationTypeEnum.DINING_HALL),
                begin,
                end,
                pageable);

        return PageUtil.toPageDTO(pageResult.getContent().stream().map(this::convert).collect(Collectors.toList()), pageResult);
    }

    /**
     * 分页获取员入职周年列表
     *
     * @author loki
     * @date 2021/7/22 9:50
     **/
    public Object findBackKitchenAnniversary(LocalDate month, Pageable pageable) {
        LocalDate firstDay = LocalDate.of(month.getYear(), month.getMonthValue(), 1);
        LocalDate lastDay = month.with(TemporalAdjusters.lastDayOfMonth());

        String begin = firstDay.format(DateTimeFormatters.MMDD);
        String end = lastDay.format(DateTimeFormatters.MMDD);

        Page<OrgEmployee> pageResult = this.orgEmployeeRepository.queryBackKitchenAnniversary(
                getRoleIdsList(TenantOrganizationTypeEnum.DINING_HALL),
                begin,
                end,
                pageable);

        return PageUtil.toPageDTO(pageResult.getContent().stream().map(this::convert).collect(Collectors.toList()), pageResult);
    }

    /**
     * type=1 食堂
     * type=2 后厨
     * type= 3 库存
     * <p>
     * 1、获取当前数据源对应的租户组织ID；
     * 2、根据步骤1租户组织获取食堂对应的租户组织ID；
     * 3、获取步骤2食堂下面所有的角色
     * 4、获取步骤3角色的所有人员
     *
     * @author loki
     * @date 2021/7/22 10:30
     **/
    private Set<Long> getRoleIdsList(TenantOrganizationTypeEnum type) {
        Long tenantOrgId = SecurityUtils.getCurrentUser().getTenantOrgIdByType(type);
        log.info("类型：{}，租户组织ID：{}", type, tenantOrgId);
        Map<Long, FeignTenantOrganizationDto> map = feignTenantOrganizationService.findByIds(Collections.singleton(tenantOrgId));
        Long parentTenantOrgId = map.get(tenantOrgId).getPid();
        log.info("食堂租户组织ID：{}", parentTenantOrgId);
        Set<Long> allTenantOrgIds = feignTenantOrganizationService.findAllChildren(Collections.singleton(parentTenantOrgId));
        log.info("所有租户ID：{}", allTenantOrgIds.stream().map(Object::toString).collect(Collectors.joining(",")));
        List<SysRole> roleList = sysRoleRepository.findByTenantOrgIdIn(allTenantOrgIds);
        log.info("角色：{}", roleList.stream().map(SysRole::getName).collect(Collectors.joining(",")));
        return roleList.stream().map(BaseEntity::getId).collect(Collectors.toSet());
    }

    public Object editPwd(OrgEmpEditPwdReq req) {
        OrgEmployee employee = orgEmployeeRepository.findById(req.getId()).orElse(null);
        if (employee == null) {
            throw new BizException("", "修改的账号不存在");
        }
        employee.setPwd(passwordEncoder.encode(req.getPwd()));
        employee.setUpdateTime(LocalDateTime.now());
        return this.orgEmployeeRepository.save(employee);
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisCacheUtil.SYS_ORG_EMP, RedisCacheUtil.SYS_ORG}, allEntries = true)
    public Object delete(Long id) {
        Optional<OrgEmployee> optional = orgEmployeeRepository.findById(id);
        if (optional.isPresent()) {
            orgService.addEmpCount(optional.get().getId(), -1);
            orgEmployeeRepository.deleteById(id);
        }

        return optional.get();
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {RedisCacheUtil.SYS_ORG_EMP, RedisCacheUtil.SYS_ORG}, allEntries = true)
    public void batchDelete(Long[] ids) {
        if (ids.length == 0) {
            throw new BizException("", "请选择需要删除的员工");
        }

        List<OrgEmployee> employeeList = new ArrayList<>();
        OrgEmployee employee;
        for (Long id : ids) {
            employee = orgEmployeeRepository.findById(id).orElseThrow(() -> new BizException("", "员工不存在"));
            employeeList.add(employee);

            orgEmployeeRepository.delete(employee);
            orgService.addEmpCount(employee.getOrg().getId(), -1);
        }
    }

    public List<OrgEmployeeDTO> searchByName(String name) {
        Specification<OrgEmployee> specification = SpecificationBuilder.builder()
                .where(Criterion.eq("deleted", Boolean.FALSE))
                .whereByOr(
                        Criterion.like("name", name),
                        Criterion.like("org.name", name)).build();
        List<OrgEmployee> page = orgEmployeeRepository.findAll(specification);
        return page.stream().map(this::convert).collect(Collectors.toList());
    }

    @Cacheable(value = RedisCacheUtil.SYS_ORG_EMP)
    public List<OrgEmployeeDTO> findByOrgIds(List<Long> orgIds) {
        List<Org> orgs = new ArrayList<>();
        orgIds.forEach(item -> this.orgRepository.findById(item).ifPresent(orgs::add));
        return this.orgEmployeeRepository.findAllByOrgIn(orgs).stream().map(this::convert).collect(Collectors.toList());
    }

    public Object uploadEmpPhoto(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BizException("", "请上传头像");
        }

        String key = System.currentTimeMillis() + "";
        Map<String, String> result = new HashMap<>();
        result.put("key", key);
        try {
            String photo = new String(encoder.encode(file.getBytes()), StandardCharsets.UTF_8);
            result.put("value", photo);

            redisTemplate.opsForValue().set(key, photo);
            redisTemplate.expire(key, 2, TimeUnit.HOURS);

        } catch (Exception ex) {
            throw new BizException("", "上传头像失败");
        }
        return result;
    }

    public void convertEmpPhoto() {
        List<OrgEmployee> result = this.orgEmployeeRepository.findAll();
        if (CollectionUtils.isEmpty(result)) {
            return;
        }

        List<OrgEmployee> employeeList = new ArrayList<>();
        for (OrgEmployee employee : result) {
            if (StringUtils.isBlank(employee.getAvatarFace())) {
                continue;
            }

            try {
                File file = File.createTempFile(employee.getId() + "", ".jpg", FileConstant.tmpDir);
                FileOutputStream out = new FileOutputStream(file);

                out.write(decoder.decode(employee.getAvatarFace()));
                String imgPath = fileHandler.saveImage("", ImageIO.read(file));
                log.info("图片访问路径：{}", imgPath);
                employee.setAvatar(imgPath);
                employeeList.add(employee);
            } catch (Exception ex) {
                ex.printStackTrace();
                log.info("上传图片失败");
            }
        }

        this.orgEmployeeRepository.saveAll(employeeList);
    }

    /**
     * Feign获取用户信息
     *
     * @author loki
     * @date 2021/06/03 15:33
     */
    public FeignEmployeeDTO getEmployeeInfoByFeign(Long id) {
        OrgEmployee employee = orgEmployeeRepository.findById(id).orElseThrow(
                () -> new BizException("未找到员工")
        );

        FeignEmployeeDTO employeeDTO = new FeignEmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setOpenid(employee.getOpenid());
        employeeDTO.setPosition(employee.getRole());
        employeeDTO.setMobile(employee.getMobile());
        employeeDTO.setAvatar(employee.getAvatar());
        //部门
        if (null != employee.getOrg()) {
            employeeDTO.setOrgId(employee.getOrg().getId());
            employeeDTO.setOrgName(employee.getOrg().getName());
        }

        //角色
        if (!CollectionUtils.isEmpty(employee.getRoles())) {
            employeeDTO.setRoleIds(employee.getRoles().stream().map(BaseEntity::getId).collect(Collectors.toList()));
        }

        return employeeDTO;
    }

    public FeignMessProductCookView findCookView(Long id) {
        OrgEmployee employee = orgEmployeeRepository.findById(id).orElseThrow(
                () -> new BizException("厨师不存在")
        );
        return new FeignMessProductCookView()
                .setId(employee.getId())
                .setName(employee.getName())
                .setGender(employee.getGender())
                .setEntryDate(employee.getEntryDate())
                .setIdNo(employee.getIdNo())
                .setMobile(employee.getMobile())
                .setOrgFullName(this.orgService.getOrgFullName(employee.getOrg()))
                .setOrgName(employee.getOrg() != null ? employee.getOrg().getName() : null)
                .setOrgId(employee.getOrg() != null ? employee.getOrg().getId() : null)
                .setRole(employee.getRole())
                .setVerifyImg(employee.getAvatar())
                .setFilesViewList(employee.getPersonalRecords().stream().map(item ->
                        new FeignMessProductCookFilesView().setName(item.getName())
                                .setHaveDate(item.getHaveDate())
                                .setUrl(item.getUrl())
                ).collect(Collectors.toList()));
    }

    public Map<Long, FeignEmployeeDTO> findByIds(Set<Long> ids) {
        Map<Long, FeignEmployeeDTO> result = new HashMap<>();
        if (CollectionUtils.isEmpty(ids)) {
            return result;
        }
        List<OrgEmployee> list = this.orgEmployeeRepository.findAllById(ids);
        for (OrgEmployee employee : list) {
            FeignEmployeeDTO employeeDTO = new FeignEmployeeDTO()
                    .setId(employee.getId())
                    .setAvatar(employee.getAvatar())
                    .setName(employee.getName());
            result.put(employee.getId(), employeeDTO);
        }
        return result;
    }

    public FeignSimEmployeeDto findSimById(Long id) {
        return this.orgEmployeeRepository.findById(id).map(item -> {
            FeignSimEmployeeDto simEmployeeDto = new FeignSimEmployeeDto()
                    .setId(item.getId())
                    .setIdNo(item.getIdNo())
                    .setName(item.getName())
                    .setImg(item.getAvatar());
            return simEmployeeDto;
        }).orElse(null);
    }

    public List<FeignSimEmployeeDto> findAllByType(Integer type, Boolean deleted) {
        return this.orgEmployeeRepository.findAllByPersonnelTypeAndDeleted(type, deleted).stream().map(item -> {
            FeignSimEmployeeDto simEmployeeDto = new FeignSimEmployeeDto()
                    .setId(item.getId())
                    .setName(item.getName())
                    .setImg(item.getAvatar())
                    .setIdNo(item.getIdNo());
            return simEmployeeDto;
        }).collect(Collectors.toList());
    }

    public List<FeignSimEmployeeDto> findSimAll() {
        Specification<OrgEmployee> specification = SpecificationBuilder.builder()
                .where(Criterion.eq("deleted", false))
                .build();
        return this.orgEmployeeRepository.findAll(specification).stream().map(item -> {
            FeignSimEmployeeDto simEmployeeDto = new FeignSimEmployeeDto()
                    .setId(item.getId())
                    .setName(item.getName())
                    .setImg(item.getAvatar())
                    .setIdNo(item.getIdNo());
            return simEmployeeDto;
        }).collect(Collectors.toList());
    }

    /**
     * 通过身份证查询人员信息
     *
     * @author loki
     * @date 2021/6/15 10:11
     **/
    public FeignSimEmployeeDto findByIdNo(String idNo) {
        OrgEmployee orgEmployee = this.orgEmployeeRepository.findByIdNoAndDeleted(idNo, false);
        return null == orgEmployee ? null :
                new FeignSimEmployeeDto()
                        .setId(orgEmployee.getId())
                        .setName(orgEmployee.getName())
                        .setImg(orgEmployee.getAvatar())
                        .setIdNo(orgEmployee.getIdNo());
    }

    /**
     * 获取后厨人员列表
     *
     * @author loki
     * @date 2021/7/13 18:19
     **/
    public List<KitchenCookDTO> getKitchenCookList() {
        List<KitchenCookDTO> result = this.kitchenCook();
        if (!CollectionUtils.isEmpty(result)) {
            for (KitchenCookDTO r : result) {
                r.setAvatar(org.apache.commons.lang3.StringUtils.isNotBlank(r.getAvatar()) ? ImgPair.getFileServer() + r.getAvatar() : "");
            }
        }

        return result;
    }

    /**
     * 看板厨师
     *
     * @author loki
     * @date 2021/01/27 17:14
     */
    public List<KitchenCookDTO> kitchenCook() {
        //获取所有厨师
        List<OrgEmployee> result = this.orgEmployeeRepository.queryBackKitchenAll(getRoleIdsList(TenantOrganizationTypeEnum.BACK_KITCHEN),
                null);

        List<KitchenCookDTO> cookList = new ArrayList<>();
        KitchenCookDTO cook;
        List<KitchenMorningInspectRecordDTO> recordList;
        KitchenMorningInspectRecordDTO record;
        Float temperature;
        for (OrgEmployee employee : result) {
            cook = new KitchenCookDTO();
            cookList.add(cook);
            cook.setAvatar(employee.getAvatar());
            cook.setName(employee.getName());
            cook.setRole(employee.getRole());

            //获取晨检记录
            recordList = feignKitchenService.getEmployeeMorningInspect(employee.getId(), LocalDateTime.now());
            if (CollectionUtils.isEmpty(recordList)) {
                cook.setTemperatureStatus(0);
                temperature = 0.0f;
            } else {
                record = recordList.get(0);
                if (record.getTemperature().doubleValue() == 0) {
                    cook.setTemperatureStatus(0);
                    temperature = 0.0f;
                } else if (record.getTemperature().doubleValue() < MAX_NORMOR_TEMPRATURE) {
                    cook.setTemperatureStatus(1);
                    temperature = record.getTemperature();
                } else {
                    cook.setTemperatureStatus(2);
                    temperature = record.getTemperature();
                }
            }
            cook.setTemperature(temperature);

            cook.setCertList(employee.getPersonalRecords().stream().map(OrgEmployeePersonalRecords::getUrl).map(item -> ImgPair.getFileServer() + item).collect(Collectors.toList()));
        }
        return cookList;
    }

    /**
     * @return long
     * @Author JoeLau
     * @Description 根据string部门id(逗号分隔)查总人数
     * @Date 2021/7/15  15:53
     * @Param orgId
     */
    public Integer countEmployee(String orgIdString) {
        if (StringUtils.isNotBlank(orgIdString)) {
            List<Long> idSet = Arrays.stream(orgIdString.split(",")).map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            return this.orgEmployeeRepository.countEmployee(idSet);
        }
        return null;
    }
}