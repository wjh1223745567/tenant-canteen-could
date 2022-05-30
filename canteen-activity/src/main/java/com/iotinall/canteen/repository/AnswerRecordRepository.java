package com.iotinall.canteen.repository;

import com.iotinall.canteen.domain.ActivityAnswerRecord;
import com.iotinall.canteen.protocol.statistic.ActivityAnswerRecordDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 答题记录持久化类
 *
 * @author joelau
 * @date 2021/05/25 10:39
 */
public interface AnswerRecordRepository extends JpaRepository<ActivityAnswerRecord, Long>, JpaSpecificationExecutor<ActivityAnswerRecord> {

    void deleteAllBySubject_IdAndEmpId(Long subjectId,Long empId);

    @Query(value = "select COUNT(*) from activity_answer_record where finished = 1 and emp_id =:empId and activity_survey_id =:surveyId",nativeQuery = true)
    Integer existentFinished(@Param("surveyId")Long surveyId, @Param("empId")Long empId);

    /**
     * 存在答题记录
     * @param surveyId
     * @return
     */
    @Query(value = "select COUNT(*) from activity_answer_record where activity_survey_id =:surveyId",nativeQuery = true)
    Integer existentAnswer(@Param("surveyId")Long surveyId);

    @Query(value = "select count(distinct i.emp_id) from activity_answer_record i where i.activity_survey_id=:surveyId and finished = 1", nativeQuery = true)
    Integer countSubmittedEmployee(@Param("surveyId") Long surveyId);


    List<ActivityAnswerRecord> findAllByActivitySurvey_IdAndSubject_IdAndFinished(Long surveyId,Long subjectId,Boolean finished);
//    List<ActivityAnswerRecord> findAllByActivitySurvey_IdAndSubject_Id(Long surveyId,Long subjectId);
    List<ActivityAnswerRecord> findAllByActivitySurvey_IdAndSubject_IdAndEmpId(Long surveyId,Long subjectId,Long empId);
    List<ActivityAnswerRecord> findAllByActivitySurvey_IdAndEmpId(Long surveyId,Long empId);

    @Query(value = "select \n" +
            "record.activity_survey_id surveyId,\n" +
            "record.subject_id subjectId,\n" +
            "record.option_id optionId,\n" +
            "opt.`name` `name`,\n" +
            "count(DISTINCT record.emp_id) count\n" +
            "from activity_answer_record record LEFT\n" +
            "\n" +
            "join activity_subject sub on(sub.id = record.subject_id)\n" +
            "join activity_subject_option opt on(opt.id = record.option_id)\n" +
            "\n" +
            "\n" +
            "where (record.activity_survey_id =:surveyId\n and finished = 1)" +
            "GROUP BY record.subject_id,record.option_id"
            ,nativeQuery = true,countProjection = "1")
    List<ActivityAnswerRecordDTO> activityOptions(@Param("surveyId") Long surveyId);

}
