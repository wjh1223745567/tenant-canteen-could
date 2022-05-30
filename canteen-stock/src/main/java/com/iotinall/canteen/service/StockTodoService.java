package com.iotinall.canteen.service;

import com.iotinall.canteen.common.exception.BizException;
import com.iotinall.canteen.common.jpa.PageUtil;
import com.iotinall.canteen.common.jpa.criteria.Criterion;
import com.iotinall.canteen.common.jpa.criteria.SpecificationBuilder;
import com.iotinall.canteen.common.security.SecurityUtils;
import com.iotinall.canteen.constant.Constants;
import com.iotinall.canteen.dto.organization.FeignEmployeeDTO;
import com.iotinall.canteen.dto.todolist.StockTodoListDTO;
import com.iotinall.canteen.entity.StockTodo;
import com.iotinall.canteen.repository.StockTodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 待办逻辑处理类
 *
 * @author loki
 * @date 2021/6/9 16:25
 **/
@Slf4j
@Service
public class StockTodoService {
    @Resource
    private StockTodoRepository stockTodoRepository;
    @Resource
    private FeignEmployeeService feignEmployeeService;

    /**
     * 待办列表
     *
     * @author loki
     * @date 2020/08/25 19:42
     */
    public Object page(String billType, String optType, String keyword, Pageable page) {
        SpecificationBuilder builder = SpecificationBuilder.builder()
                .where(Criterion.eq("stockBill.billType", billType))
                .where(Criterion.eq("optType", optType))
                .where(Criterion.like("applyUserName", keyword))
                .where(Criterion.eq("status", Constants.TODO_STATUS_INIT));

        //获取当前用户权限
        List<Criterion> authList = new ArrayList<>();
        Criterion criterion;
        FeignEmployeeDTO employee = feignEmployeeService.findById(SecurityUtils.getUserId());
        for (Long roleId : employee.getRoleIds()) {
            criterion = Criterion.eq("authorities.roleId", roleId);
            authList.add(criterion);
        }

        builder.whereByOr(authList);
        Page<StockTodo> result = this.stockTodoRepository.findAll(builder.build(), page);

        List<StockTodo> todoList = result.getContent();
        if (CollectionUtils.isEmpty(todoList)) {
            return PageUtil.toPageDTO(result);
        }

        List<StockTodoListDTO> todoListDTOList = new ArrayList<>();
        StockTodoListDTO todoListDTO;
        for (StockTodo td : todoList) {
            todoListDTO = new StockTodoListDTO();
            BeanUtils.copyProperties(td, todoListDTO);
            if (null != td.getStockBill()) {
                todoListDTO.setBillDate(td.getStockBill().getBillDate());
                todoListDTO.setBillId(td.getStockBill().getId());
                todoListDTO.setBillNo(td.getStockBill().getBillNo());
                todoListDTO.setBillType(td.getStockBill().getBillType());
            }
            todoListDTOList.add(todoListDTO);
        }

        return PageUtil.toPageDTO(todoListDTOList, result);
    }

    /**
     * 详情
     *
     * @author loki
     * @date 2020/09/04 17:17
     */
    public void update(Long id) {
        StockTodo todoList = this.stockTodoRepository.findById(id).orElse(null);
        if (null == todoList) {
            throw new BizException("", "待办不存在");
        }

        todoList.setStatus(Constants.TODO_STATUS_DONE);
        this.stockTodoRepository.save(todoList);
    }
}
