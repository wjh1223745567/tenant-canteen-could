package com.iotinall.canteen.protocol.statistic;

import lombok.experimental.Accessors;

@Accessors(chain = true)
public interface ActivityAnswerRecordDTO {

    Long getSurveyId();

    Long getSubjectId();

    Long getOptionId();

    String getName();

    Integer getCount();
}
