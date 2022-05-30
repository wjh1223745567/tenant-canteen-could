package com.iotinall.canteen.common.datasource;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.iotinall.canteen.common.constant.TenantOrganizationTypeEnum;
import com.iotinall.canteen.common.property.DefaultMysqlConfigProperty;
import com.iotinall.canteen.common.tenant.TenantUser;
import com.iotinall.canteen.common.tenant.TenantUserService;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.schema.TargetType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MultiDataSourceInit {

    public static final String MASTER = "master";

    @Resource
    private TenantUserService tenantUserService;

    @Resource
    private DefaultMysqlConfigProperty mysqlConfigProperty;

    @Resource
    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Value("${spring.application.name}")
    private String systemName;

    @Resource
    private LocalContainerEntityManagerFactoryBean fb;

    @PostConstruct
    public void InitDataSource() {
        TenantOrganizationTypeEnum tenantOrganizationTypeEnum = tenantUserService.nowSysType();
        List<TenantUser> tenantList = tenantUserService.findAll(tenantOrganizationTypeEnum != null ? tenantOrganizationTypeEnum.getCode() : null);
        log.info("=====初始化动态数据源{}个=====", tenantList.size());
        for (TenantUser tenantInfo : tenantList) {
            log.info(tenantInfo.toString());
            //添加key
            HikariDataSource devDataSource = new HikariDataSource();
            devDataSource.setJdbcUrl(DefaultMysqlConfigProperty.urlPrefix + tenantInfo.getSqlUrl() + DefaultMysqlConfigProperty.urlSuffix);
            devDataSource.setUsername(tenantInfo.getSqlUsername());
            devDataSource.setPassword(tenantInfo.getSqlPassword());
            devDataSource.setPoolName(tenantInfo.getCode());
            devDataSource.setMinimumIdle(mysqlConfigProperty.getMinimumIdle());
            devDataSource.setMaximumPoolSize(mysqlConfigProperty.getMaximumPoolSize());
            devDataSource.setAutoCommit(mysqlConfigProperty.getAutoCommit());
            devDataSource.setIdleTimeout(mysqlConfigProperty.getIdleTimeout());
            devDataSource.setMaxLifetime(mysqlConfigProperty.getMaxLifetime());
            devDataSource.setConnectionTimeout(mysqlConfigProperty.getConnectionTimeout());
            devDataSource.setConnectionTestQuery(mysqlConfigProperty.getConnectionTestQuery());
            dynamicRoutingDataSource.addDataSource(tenantInfo.getCode(), devDataSource);
            updateTable(devDataSource);
        }

        /**
         * 必须执行此操作，才会重新初始化AbstractRoutingDataSource 中的 resolvedDataSources，也只有这样，动态切换才会起效
         */
        try {
            if (!tenantList.isEmpty()) {
                dynamicRoutingDataSource.afterPropertiesSet();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新并创建表结构
     *
     * @param dataSource
     */
    public void updateTable(HikariDataSource dataSource) {
        log.info("更新数据库结构：{}", dataSource.getJdbcUrl());
        Map<String, Object> applySettings = new HashMap<>();
        applySettings.put("hibernate.connection.url", dataSource.getJdbcUrl());
        applySettings.put("hibernate.connection.username", dataSource.getUsername());
        applySettings.put("hibernate.connection.password", dataSource.getPassword());
        //是否输出sql语句
        applySettings.put("hibernate.show_sql", "false");

        Map<String, Object> jpaProperty = fb.getJpaPropertyMap();
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(jpaProperty).applySettings(applySettings)
                .build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        List<String> managedClassNames = fb.getPersistenceUnitInfo().getManagedClassNames();
        for (String managedClassName : managedClassNames) {
            metadataSources.addAnnotatedClassName(managedClassName);
        }

        // 生成Metadata构建元信息
        Metadata metadata = metadataSources.buildMetadata();
        SchemaUpdate schemaUpdate = new SchemaUpdate();
        schemaUpdate.execute(EnumSet.of(TargetType.DATABASE), metadata);
    }

}
