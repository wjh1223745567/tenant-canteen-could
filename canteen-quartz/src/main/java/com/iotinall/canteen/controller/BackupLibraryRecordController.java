package com.iotinall.canteen.controller;

import com.iotinall.canteen.common.protocol.ResultDTO;
import com.iotinall.canteen.service.BackupLibraryService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 查询数据库备份情况
 */
@RestController
@RequestMapping(value = "backup_library_record")
public class BackupLibraryRecordController {

    @Resource
    private BackupLibraryService backupLibraryService;

    @GetMapping(value = "page")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResultDTO<?> page(@PageableDefault(sort = {"createTime"}, direction = Sort.Direction.DESC) Pageable pageable){
        return ResultDTO.success(backupLibraryService.page(pageable));
    }

}
