package com.iotinall.canteen.dto.menu;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author WJH
 * @date 2019/11/517:28
 */
@Setter
@Getter
@Accessors(chain = true)
public class MenuMainDto {
    private int state;
    private List<MenuDto> breakfast;
    private List<MenuDto> lunch;
    private List<MenuDto> dinner;
}
