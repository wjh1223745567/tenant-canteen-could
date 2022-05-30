package com.iotinall.canteen.dto.sysdictdetail;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author bingo
 * @date 12/27/2019 17:51
 */
@ApiModel(description = "餐厅标签")
@Data
public class MessTagAddReq {
    @NotBlank(message = "请输入标签内容")
    @Length(max = 8, message = "标签长度最多8个字符")
    private String label;
    @NotBlank(message = "请输入标签类型")
    private String tagType;
}
