package com.iotinall.canteen.dto.personalrecords;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.iotinall.canteen.common.util.ImgPair;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class OrgEmployeePersonalRecordsReq {

    private Long id;

    @NotBlank(message = "请输入名称")
    @Size(max = 200)
    private String name;

    @NotNull(message = "请选择日期")
    private LocalDate haveDate;

    @Size(max = 400)
    @JsonDeserialize(using = ImgPair.ImgDeserializer.class)
    @NotBlank(message = "请上传照片")
    private String url;

}
