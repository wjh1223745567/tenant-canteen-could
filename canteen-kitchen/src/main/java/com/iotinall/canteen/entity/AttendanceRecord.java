package com.iotinall.canteen.entity;

import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

/**
 * 考勤记录
 */
@Data
@Entity
@Table(name = "attendance_record")
@ToString(exclude = "details")
@NamedEntityGraphs(value = {@NamedEntityGraph(name = "detailsGraph", attributeNodes = @NamedAttributeNode(value = "details"))})
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AttendanceRecord extends BaseEntity {
    private Long empId;

    private LocalDate recordDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "record")
    @OrderBy("shiftBeginTime asc")
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<AttendanceShiftRecord> details;
}
