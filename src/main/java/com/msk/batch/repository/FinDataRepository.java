package com.msk.batch.repository;


import com.msk.batch.model.FinData;
import com.msk.batch.model.FinDataId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FinDataRepository extends JpaRepository<FinData, FinDataId> {


}
