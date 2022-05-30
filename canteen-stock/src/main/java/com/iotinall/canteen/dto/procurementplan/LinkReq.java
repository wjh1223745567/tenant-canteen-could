package com.iotinall.canteen.dto.procurementplan;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class LinkReq {

    private List<String> ids;

    private LocalDate date;

}
