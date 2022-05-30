package com.iotinall.canteen.dto.menubrief;

import lombok.Data;

import java.util.List;

@Data
public class AllSearchDto {

    private Long log_id;

    private Integer result_num;

    private List<AllDishesDto> result;

}
