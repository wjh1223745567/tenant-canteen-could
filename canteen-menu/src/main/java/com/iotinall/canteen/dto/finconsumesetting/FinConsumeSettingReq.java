package com.iotinall.canteen.dto.finconsumesetting;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.HibernateValidator;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Set;

/**
 * 添加fin_consume_setting请求
 * @author xin-bing
 * @date 2019-10-23 17:57:09
 */
@Data
@ApiModel(description = "修改消费设置请求")
public class FinConsumeSettingReq {

    @ApiModelProperty(value = "就餐开始时间，HH:mm", required = true)
    @NotBlank(message = "请填写开始时间")
    @Pattern(regexp = "^(20|21|22|23|[0-1]\\d):[0-5]\\d$", message = "开始时间格式不正确")
    private String beginTime;// begin_time

    @ApiModelProperty(value = "就餐截止时间，HH:mm", required = true)
    @NotBlank(message = "请填写结束时间")
    @Pattern(regexp = "^(20|21|22|23|[0-1]\\d):[0-5]\\d$", message = "结束时间格式不正确")
    private String endTime;// end_time

    public void validate() throws BindException {
        Set<ConstraintViolation<FinConsumeSettingReq>> set = Validation.byProvider(HibernateValidator.class).configure()
                .buildValidatorFactory().getValidator().validate(this);
        if(!CollectionUtils.isEmpty(set)) {
            ConstraintViolation<FinConsumeSettingReq> violation = set.iterator().next();
            BindingResult bindingResult = new BeanPropertyBindingResult(this, this.getClass().getName());
            bindingResult.addError(new ObjectError(this.getClass().getName(), violation.getMessage()));
            throw new BindException(bindingResult);
        }
        if(getEndTime().compareTo(getBeginTime()) <= 0) {
            BindingResult bindingResult = new BeanPropertyBindingResult(this, this.getClass().getName());
            bindingResult.addError(new ObjectError(this.getClass().getName(), "开始时间必须小于结束时间"));
            throw new BindException(bindingResult);
        }
    }
}