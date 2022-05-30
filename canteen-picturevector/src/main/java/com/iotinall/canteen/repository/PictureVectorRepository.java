package com.iotinall.canteen.repository;

import com.iotinall.canteen.entity.PictureVector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface PictureVectorRepository extends JpaRepository<PictureVector, Long>, JpaSpecificationExecutor<PictureVector> {

    @Query(value = "select * from picture_vector where data_id = ?1", nativeQuery = true)
    PictureVector findDataId(String dataId);

}
