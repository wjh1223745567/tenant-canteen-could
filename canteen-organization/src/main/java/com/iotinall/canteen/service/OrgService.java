package com.iotinall.canteen.service;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.dto.org.OrgAddReq;
import com.iotinall.canteen.dto.org.OrgEditReq;
import com.iotinall.canteen.dto.org.OrgSelectTreeDTO;
import com.iotinall.canteen.dto.org.OrgTreeDTO;
import com.iotinall.canteen.entity.EmployeeWallet;
import com.iotinall.canteen.entity.Org;
import com.iotinall.canteen.entity.OrgEmployee;
import com.iotinall.canteen.repository.OrgEmployeeRepository;
import com.iotinall.canteen.repository.OrgRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 组织 ServiceImpl
 *
 * @author xin-bing
 * @date 2019-10-24 13:55:41
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
@CacheConfig(cacheNames = "org")
public class OrgService {
    @Resource
    private OrgRepository orgRepository;
    @Resource
    private OrgEmployeeRepository orgEmployeeRepository;
    @Resource
    private FeignTenantOrganizationService feignTenantOrganizationService;

    @Transactional(rollbackFor = Exception.class)
    public Org add(OrgAddReq req) {
        Org org = new Org();

        if (req.getParentId() != null) {
            Optional<Org> byId = orgRepository.findById(req.getParentId());
            if (!byId.isPresent()) {
                throw new BizException("", "父级组织不存在");
            }
        }

        Set<Long> menuIds = new HashSet<>();
        menuIds.add(feignTenantOrganizationService.findMenuId());
        menuIds.addAll(feignTenantOrganizationService.findAllChildren(menuIds));
        List<Org> orgs = orgRepository.findByNameAndTenantIdIn(req.getName(), menuIds);

        //刘俊 修改于2020-04-27 BUG 653 不同机构下可以存在重复的部门名称
        if (!CollectionUtils.isEmpty(orgs)) {
            Org existOrg = orgs.stream().filter(item -> item.getParentId().equals(req.getParentId())).findAny().orElse(null);
            if (null != existOrg) {
                throw new BizException("", "组织名重复了");
            }
        }

        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, org);
        LocalDateTime now = LocalDateTime.now();
        org.setEmpCount(0);
        org.setSort(0);
        org.setCreateTime(now);
        org.setDeleted(Boolean.FALSE);
        org.setParentOrgFullId(this.buildOrg(org.getParentId()));
        org.setTenantId(req.getTenantId() != null ? req.getTenantId() : feignTenantOrganizationService.findTenantHighest());
        return orgRepository.save(org);
    }

    @Transactional(rollbackFor = Exception.class)
    public Object update(OrgEditReq req) {
        Optional<Org> optional = orgRepository.findById(req.getId());
        if (!optional.isPresent()) {
            throw new BizException("", "记录不存在");
        }
        Org org = optional.get();
        if (org.getParentId() != null && req.getParentId() == null) {
            throw new BizException("", "请选择父级组织");
        }
        if (org.getParentId() != null && !org.getParentId().equals(req.getParentId())) { // 更改了父组织
            Optional<Org> byId = orgRepository.findById(req.getParentId());
            if (!byId.isPresent()) {
                throw new BizException("", "父级组织不存在");
            }
        }

        Set<Long> menuIds = new HashSet<>();
        menuIds.add(feignTenantOrganizationService.findMenuId());
        menuIds.addAll(feignTenantOrganizationService.findAllChildren(menuIds));
        List<Org> orgs = orgRepository.findByNameAndTenantIdIn(req.getName(), menuIds);
        if (!CollectionUtils.isEmpty(orgs)) {
            Org existOrg = orgs.stream().filter(item -> Objects.equals(item.getParentId(), (req.getParentId())) && !Objects.equals(req.getId(), (item.getId()))).findAny().orElse(null);
            if (null != existOrg) {
                throw new BizException("", "组织名重复了");
            }
        }

        // 设置属性值，例如使用BeanUtils，或者自己写转换方法，推荐自己手写
        BeanUtils.copyProperties(req, org);
        org.setTenantId(req.getTenantId() != null ? req.getTenantId() : feignTenantOrganizationService.findTenantHighest());
        return orgRepository.save(org);
    }

    @Transactional(rollbackFor = Exception.class)
    public Object delete(Long id) {
        Org org = orgRepository.findById(id).orElseThrow(() -> new BizException("", "组织机构不存在"));

        if (org.getParentId() == null) {
            throw new BizException("", "根节点部门无法删除");
        }

        if (!CollectionUtils.isEmpty(org.getChildren()) && org.getChildren().stream().noneMatch(Org::getDeleted)) {
            throw new BizException("", "存在子组织，不允许删除");
        }

        if (!CollectionUtils.isEmpty(org.getEmployees()) && org.getEmployees().stream().noneMatch(OrgEmployee::getDeleted)) {
            throw new BizException("", "该组织该有员工，不允许删除");
        }

        org.setDeleted(Boolean.TRUE);
        this.orgRepository.save(org);
        return org;
    }

    public List<OrgTreeDTO> findTree() {
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.ne("deleted", Boolean.TRUE));

        if (!SecurityUtils.getCurrentUser().isSupperAdmin()) {
            Set<Long> ids = feignTenantOrganizationService.findTenantHighestChildren();
            if (!CollectionUtils.isEmpty(ids)) {
                builder.where(Criterion.in("tenantId", ids));
            } else {
                return Collections.emptyList();
            }
        }
        List<Org> topOrgs = orgRepository.findAll(builder.build());

        Map<Long, OrgTreeDTO> map = new HashMap<>();
        for (Org topOrg : topOrgs) {
            OrgTreeDTO orgTreeDTO = new OrgTreeDTO()
                    .setId(topOrg.getId())
                    .setName(topOrg.getName())
                    .setParentId(topOrg.getParentId())
                    .setTenantId(topOrg.getTenantId())
                    .setChildren(new ArrayList<>());
            map.put(topOrg.getId(), orgTreeDTO);
        }

        List<OrgTreeDTO> result = new ArrayList<>();
        for (OrgTreeDTO value : map.values()) {
            if (value.getParentId() != null && map.containsKey(value.getParentId())) {
                OrgTreeDTO orgTreeDTO = map.get(value.getParentId());
                if (orgTreeDTO.getChildren() == null) {
                    orgTreeDTO.setChildren(new ArrayList<>());
                }
                orgTreeDTO.getChildren().add(value);
            } else {
                result.add(value);
            }
        }
        return result;
    }

    public OrgTreeDTO copyOrgTree(Org org) {
        OrgTreeDTO treeDTO = new OrgTreeDTO()
                .setId(org.getId())
                .setName(org.getName())
                .setTenantId(org.getTenantId())
                .setParentId(org.getParentId());
        List<OrgTreeDTO> children = org.getChildren().stream().filter(item -> !item.getDeleted()).map(this::copyOrgTree).collect(Collectors.toList());
        treeDTO.setChildren(children);
        return treeDTO;
    }

    public Object findUserTree() {
        String userOrgId = this.buildOrg(SecurityUtils.getCurrentUser().getOrgId());
        String jpql = "from Org p where p.parentOrgFullId like :userOrgId and p.deleted = 0 order by id asc";
        Map params = Maps.newHashMap();
        params.put("userOrgId", userOrgId + "%");
        List<Org> orgList = orgRepository.list(jpql, params);
        if (CollectionUtils.isEmpty(orgList)) {
            return null;
        }
        Org tree = this.getParentOrg(orgList.get(0).getParentId(), new HashSet<>(orgList));
        return new OrgTreeDTO()
                .setId(tree.getId())
                .setName(tree.getName())
                .setParentId(tree.getParentId())
                .setChildren(tree.getChildren().stream().map(this::copyOrgTree).collect(Collectors.toList()));
    }

    private Org getParentOrg(Long parentId, Set<Org> org) {
        Org parentOrg = this.orgRepository.findById(parentId).orElse(null);
        if (parentOrg == null) {
            return null;
        }

        parentOrg.setChildren(org);
        if (null != parentOrg.getParentId()) {
            this.getParentOrg(parentOrg.getParentId(), Sets.newHashSet(parentOrg));
        }
        return parentOrg;
    }

    public void addEmpCount(Long orgId, int count) {
        Org org = orgRepository.findById(orgId).orElse(null);
        if (org == null) {
            throw new BizException("", "部门不存在！");
        }
        String parentOrgId = org.getParentOrgFullId();

        List<Long> ids = null;
        if (!StringUtils.isBlank(parentOrgId)) {
            ids = Arrays.stream(parentOrgId.split("\\|")).map(Long::valueOf).collect(Collectors.toList());
            ids.add(orgId);
        } else {
            ids = new ArrayList<>(1);
            ids.add(orgId);
        }
        orgRepository.addEmpCount(count, ids);
    }

    public void amendEmpCount(Long oldOrgId, Long newOrgId, int count) {
        Org oldOrg = orgRepository.findById(oldOrgId).orElse(null);
        Org newOrg = orgRepository.findById(newOrgId).orElse(null);
        if (oldOrg == null || newOrg == null) {
            throw new BizException("", "部门不存在");
        }
        String oldParentPath = oldOrg.getParentOrgFullId() == null ? "" : oldOrg.getParentOrgFullId();
        String newParentPath = newOrg.getParentOrgFullId() == null ? "" : newOrg.getParentOrgFullId();
        // oldPath上的org及oldOrg减
        // newPath上的org及newOrg增加
        Set<Long> oldPath = Arrays.stream(oldParentPath.split("\\|")).filter(StringUtils::isNoneBlank).map(Long::valueOf).collect(Collectors.toSet());
        oldPath.add(oldOrgId);
        Set<Long> newPath = Arrays.stream(newParentPath.split("\\|")).filter(StringUtils::isNoneBlank).map(Long::valueOf).collect(Collectors.toSet());
        newPath.add(newOrgId);
        // 旧的中除去新的不包含的都要减少
        List<Long> decreaseOrgs = oldPath.stream().filter(id -> !newPath.contains(id)).collect(Collectors.toList());
        List<Long> increaseOrgs = newPath.stream().filter(id -> !oldPath.contains(id)).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(decreaseOrgs)) {
            orgRepository.addEmpCount(-count, decreaseOrgs);
        }
        if (!CollectionUtils.isEmpty(increaseOrgs)) {
            orgRepository.addEmpCount(count, increaseOrgs);
        }
    }

    public String buildOrg(Long id) {
        if (id == null) {
            return null;
        }
        Org org = this.orgRepository.findById(id).orElse(null);
        if (org == null) {
            throw new BizException("", "组织不存在");
        }
        return StringUtils.isBlank(org.getParentOrgFullId()) ? id + "" : org.getParentOrgFullId() + "|" + id;
    }

    /**
     * 查询所有部门不包括自己
     * @param orgIds
     * @return
     */
    public Set<Long> getAllChildOrg(List<Long> orgIds) {
        if (CollectionUtils.isEmpty(orgIds)) {
            return Collections.emptySet();
        }
        Specification<Org> specification = SpecificationBuilder.builder()
                .whereByOr(orgIds.stream().map(id -> Criterion.like("parentOrgFullId", id)).collect(Collectors.toList())).build();

        return this.orgRepository.findAll(specification).stream().map(Org::getId).collect(Collectors.toSet());
    }

    public void updateWallet() {
        List<OrgEmployee> employeeList = this.orgEmployeeRepository.findAll();
        for (OrgEmployee employee : employeeList) {
            if (null == employee.getWallet()) {
                EmployeeWallet wallet = new EmployeeWallet(BigDecimal.ZERO);
                employee.setWallet(wallet);
                this.orgEmployeeRepository.save(employee);
            }
        }
    }

    public String getOrgFullName(Org org) {
        if (null == org) {
            return "";
        }

        if (null != org.getParentId()) {
            return this.getOrgFullName(org.getParentId(), org.getName());
        }

        return org.getName();
    }

    private String getOrgFullName(Long parentId, String orgName) {
        Org org = this.orgRepository.findById(parentId).orElse(null);
        if (null == org) {
            return orgName;
        }

        orgName = org.getName() + "/" + orgName;

        if (null != org.getParentId()) {
            return this.getOrgFullName(org.getParentId(), orgName);
        }

        return orgName;
    }

    /**
     * 设备选择可见范围或者使用范围
     * 查询逻辑：
     * 1、pid为空，查询第一级部门和人员
     * 2、pid不为空，查询pid下面的部门和人员
     *
     * @param pid     用户点击的组织部门
     * @param keyword 组织部门
     * @return
     */
    public Object getOrgTree(Long pid, String keyword) {

        //组织机构
        List<Org> orgList = this.getOrg(pid, keyword);

        //人员
        List<OrgEmployee> employeeList = this.getEmployee(orgList, pid, keyword);

        return this.buildOrgTree(orgList, employeeList);
    }

    /**
     * 获取人员
     *
     * @author loki
     * @date 2020/11/03 20:02
     */
    private List<OrgEmployee> getEmployee(List<Org> orgList, Long pid, String keyword) {
        //组织机构
        List<OrgEmployee> employeeList;
        if (null == pid) {
            if (CollectionUtils.isEmpty(orgList)) {
                return null;
            }
            employeeList = this.orgEmployeeRepository.findAllByOrgIn(orgList);
        } else {
            employeeList = this.orgEmployeeRepository.findAllByOrgIn(Collections.singletonList(this.orgRepository.findById(pid).get()));
        }

        //通过关键过滤
        if (StringUtils.isNotBlank(keyword) && !CollectionUtils.isEmpty(employeeList)) {
            return employeeList.stream().filter(item -> item.getName().contains(keyword)).collect(Collectors.toList());
        }
        return employeeList;
    }

    /**
     * 获取组织机构
     *
     * @author loki
     * @date 2020/11/03 20:02
     */
    private List<Org> getOrg(Long pid, String keyword) {
        //组织机构
        List<Org> orgList;
        if (null == pid) {
            orgList = this.orgRepository.findOrgTree();
        } else {
            orgList = this.orgRepository.findByParentId(pid);
        }
        orgList = orgList.stream().filter(item -> !item.getDeleted()).collect(Collectors.toList());
        //通过关键过滤
        if (StringUtils.isNotBlank(keyword) && !CollectionUtils.isEmpty(orgList)) {
            return orgList.stream().filter(item -> item.getName().contains(keyword)).collect(Collectors.toList());
        }
        return orgList;
    }

    /**
     * 构建组织部门树
     *
     * @author loki
     * @date 2020/11/03 11:14
     */
    public Object buildOrgTree(List<Org> orgList, List<OrgEmployee> employeeList) {
        List<OrgSelectTreeDTO> trees = new ArrayList<>();
        if (!CollectionUtils.isEmpty(orgList)) {
            for (Org org : orgList) {
                trees.add(OrgSelectTreeDTO.builder().id(org.getId())
                        .name(org.getName())
                        .isOrg(true)
                        .haveChildren(CollectionUtils.isEmpty(org.getChildren().stream().filter(item -> !item.getDeleted()).collect(Collectors.toList()))).build());
            }
        }

        if (!CollectionUtils.isEmpty(employeeList)) {
            for (OrgEmployee employee : employeeList) {
                trees.add(OrgSelectTreeDTO.builder().id(employee.getId())
                        .name(employee.getName())
                        .isOrg(false)
                        .haveChildren(false).build());
            }
        }
        return trees;
    }

    /**
     * 根据名称获取部门ID
     *
     * @return
     */
    public Org getOrgByName(String... orgName) {
        Org org = this.orgRepository.findFirstByParentIdIsNull();
        if (org == null) {
            throw new BizException("", "未找到根部门，请先建立根部门");
        }
        for (String s : orgName) {
            Org nowOrg = this.orgRepository.findFirstByNameAndParentId(s, org.getId());
            if (nowOrg == null) {
                OrgAddReq addReq = new OrgAddReq();
                addReq.setName(s);
                addReq.setParentId(org.getId());
                nowOrg = this.add(addReq);
                this.orgRepository.saveAndFlush(nowOrg);
            }
            org = nowOrg;
        }
        return org;
    }
}