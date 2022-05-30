package com.iotinall.canteen.dto.menubrief;

import lombok.Data;

import java.util.List;

@Data
public class SearchDto {

    private Boolean has_more;

    private Long log_id;

    private Integer result_num;

    private List<Dish> result;



}
