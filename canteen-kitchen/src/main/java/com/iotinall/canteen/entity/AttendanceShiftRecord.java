package com.iotinall.canteen.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.iotinall.canteen.common.entity.BaseEntity;
import lombok.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attendance_shift_record")
@NamedEntityGraph(name = "getAllAttendanceRecord", attributeNodes = @NamedAttributeNode(value = "record"))
@ToString(exclude = "record")
@EqualsAndHashCode(callSuper = true)
public class AttendanceShiftRecord extends BaseEntity {
    private Long shiftId;

    private String shiftName;

    private LocalTime shiftBeginTime;

    private LocalTime shiftEndTime;
    /**
     * 上班打卡时间
     */
    @Column(columnDefinition = "time")
    private LocalTime punchInTime;

    /**
     * 0-请假 1-正常 2-迟到 3-早退
     */
    private Integer punchInStatus;

    /**
     * 打卡最新图片
     */
    @Column(columnDefinition = "longtext")
    private String punchInImg;

    /**
     * 打卡体温
     */
    private Float punchInTemperature;

    /**
     * 下班打卡最新时间
     */
    @Column(columnDefinition = "time")
    private LocalTime punchOutTime;

    /**
     * 0-未打卡 1-正常 2-迟到 3-早退 4-缺卡 9-请假
     */
    private Integer punchOutStatus;

    /**
     * 下班打卡最新图片
     */
    @Column(columnDefinition = "longtext")
    private String punchOutImg;

    /**
     * 打卡体温
     */
    private Float punchOutTemperature;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private AttendanceRecord record;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shiftRecordId")
    private Set<AttendanceRecordDetail> details;

}
