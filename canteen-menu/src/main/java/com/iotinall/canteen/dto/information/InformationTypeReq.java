package com.iotinall.canteen.dto.information;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author WJH
 * @date 2019/11/19:56
 */
@Setter
@Getter
public class InformationTypeReq {

    private Long id;

    @NotBlank(message = "名称不能为空")
    @Length(max = 255, message = "长度不能超过255个字符")
    private String name;

    private Boolean status;

    @Length(max = 255)
    private String remark;

    private String bindOrgList;
}
