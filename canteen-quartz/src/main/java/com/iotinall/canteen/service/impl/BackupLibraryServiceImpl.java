package com.iotinall.canteen.service.impl;

import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.protocol.PageDTO;
import com.iotinall.canteen.common.util.FileHandler;
import com.iotinall.canteen.dto.backuplibraryrecord.BackupLibraryRecordDto;
import com.iotinall.canteen.entity.BackupLibraryRecord;
import com.iotinall.canteen.property.ExportSqlProperty;
import com.iotinall.canteen.repository.BackupLibraryRecordRepository;
import com.iotinall.canteen.service.BackupLibraryService;
import com.iotinall.canteen.service.CreateSqlFileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
public class BackupLibraryServiceImpl implements BackupLibraryService {

    @Resource
    private ExportSqlProperty exportSqlProperty;

    @Resource
    private CreateSqlFileService createSqlFileService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private FileHandler fileHandler;

    @Resource
    private BackupLibraryRecordRepository backupLibraryRecordRepository;


    @Override
    public PageDTO<BackupLibraryRecordDto> page(Pageable pageable) {
        Page<BackupLibraryRecordDto> page = this.backupLibraryRecordRepository.findAll(pageable).map(item -> {
            BackupLibraryRecordDto recordDto = new BackupLibraryRecordDto()
                    .setId(item.getId())
                    .setLibraryName(item.getLibraryName())
                    .setFile(item.getFile())
                    .setSize(item.getSize())
                    .setCreateTime(item.getCreateTime());
            return recordDto;
        });
        return PageUtil.toPageDTO(page);
    }

    /**
     * 删除过期数据
     */
    @Override
    public void expired() {
        if (exportSqlProperty.getKeepDays() != null) {
            SpecificationBuilder specificationBuilder = SpecificationBuilder.builder()
                    .where(
                            Criterion.lt("createTime", LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(exportSqlProperty.getKeepDays()))
                    );
            List<BackupLibraryRecord> libraryRecords = this.backupLibraryRecordRepository.findAll(specificationBuilder.build());
            if (!libraryRecords.isEmpty()) {
                for (BackupLibraryRecord libraryRecord : libraryRecords) {
                    if (StringUtils.isNotBlank(libraryRecord.getFile())) {
                        fileHandler.deleteFile(libraryRecord.getFile());
                    }
                }
                this.backupLibraryRecordRepository.deleteAll(libraryRecords);
            }
        }
    }

    @Override
    public void executeBackupLibrary() {
        long startTime = System.currentTimeMillis();

        String sql = "show databases";
        List<String> dataBases = new ArrayList<>(jdbcTemplate.queryForList(sql, String.class));

        //筛选包含的库
        if (StringUtils.isNotBlank(exportSqlProperty.getIncluded())) {
            List<String> included = Arrays.asList(exportSqlProperty.getIncluded().split(","));
            //和已有的数据库求交集
            dataBases = dataBases.stream().filter(included::contains).collect(Collectors.toList());
        }

        //去除不包括的库
        if (StringUtils.isNotBlank(exportSqlProperty.getExcluded())) {
            List<String> excluded = Arrays.asList(exportSqlProperty.getExcluded().split(","));
            dataBases = dataBases.stream().filter(item -> !excluded.contains(item)).collect(Collectors.toList());
        }

        File filedir = new File(CreateSqlFileService.filePath);
        //清理历史文件
        if (filedir.exists()) {
            createSqlFileService.deleteFile(filedir);
        }
        filedir.mkdirs();

        Map<String, List<String>> allMap = new HashMap<>();

        for (String dataBase : dataBases) {
            String tablessql = "select table_name from information_schema.tables where table_schema=?";
            List<String> tables = jdbcTemplate.queryForList(tablessql, new Object[]{dataBase}, String.class);

            List<String> createTableSqls = new ArrayList<>();
            List<String> stockTables = new ArrayList<>();

            for (String table : tables) {
                String tableName = "`" + dataBase + "`.`" + table + "`";
                String createSql = "SHOW CREATE TABLE " + tableName;
                Map<String, Object> map = jdbcTemplate.queryForMap(createSql);
                stockTables.add(tableName);
                createTableSqls.add(String.valueOf(map.get("Create Table")));
            }

            createSqlFileService.createFile(dataBase, CreateSqlFileService.filePath, createTableSqls);

            allMap.put(dataBase, stockTables);
        }

        List<Future<Boolean>> futures = new ArrayList<>();
        for (String str : allMap.keySet()) {
            List<String> list = allMap.get(str);
            for (String table : list) {
                //输出insert sql
                futures.add(createSqlFileService.exportInsertSql(table, str, exportSqlProperty.getInsertSize()));
            }
        }

        while (true) {
            Optional<Future<Boolean>> future = futures.stream().filter(item -> !item.isDone()).findAny();
            if (future.isPresent()) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ignored) {
                }
            } else {
                break;
            }
        }

        //创建zip文件
        createSqlFileService.compressionZip(exportSqlProperty.getZippassword());

        log.info("生成sql文件完毕：{}", System.currentTimeMillis() - startTime);
    }
}
