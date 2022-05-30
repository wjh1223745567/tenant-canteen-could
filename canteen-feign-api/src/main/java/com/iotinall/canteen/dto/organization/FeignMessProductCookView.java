package com.iotinall.canteen.dto.organization;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@Accessors(chain = true)
public class FeignMessProductCookView {

    private Long id;

    private String name; // 姓名

    private Integer gender; // 0女 1男

    private String idNo; // 身份证号码，加密存储

    private String verifyImg; // 用户检验的头像

    private String mobile; // 手机号码

    private String role; // 职位

    private String orgFullName;

    /**
     * 入职日期
     */
    private LocalDate entryDate;

    /**
     * 个人档案
     */
    private List<FeignMessProductCookFilesView> filesViewList = new ArrayList<>(0);

    private Long orgId;

    private String orgName;
}
