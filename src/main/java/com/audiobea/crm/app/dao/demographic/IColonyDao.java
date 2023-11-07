package com.audiobea.crm.app.dao.demographic;

import com.audiobea.crm.app.dao.demographic.model.Colony;
import com.audiobea.crm.app.utils.Constants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface IColonyDao extends PagingAndSortingRepository<Colony, Long> {

    @Query(value = Constants.FIND_COLONIES, nativeQuery = true)
    Page<Colony> findAllByColonyOrPostalCode(@Param("colony") String colony, @Param("postalCode") String postalCode, Pageable pageable);

    @Query(value = Constants.FIND_COLONIES_BY_CITY_ID, nativeQuery = true)
    Page<Colony> findByCityId(@Param("cityId") Long cityId, @Param("colony") String colony, @Param("postalCode") String postalCode, Pageable pageable);

    @Query(value = Constants.FIND_COLONIES_BY_STATE_ID, nativeQuery = true)
    Page<Colony> findByStateId(@Param("stateId") Long stateId, @Param("colony") String colony, @Param("postalCode") String postalCode, Pageable pageable);

    @Query(value = Constants.FIND_COLONIES_BY_STATE_ID_CITY_ID_CODE_POSTAL, nativeQuery = true)
    Page<Colony> findByStateIdAndCityAndCodePostalId(@Param("stateId") Long stateId, @Param("cityId") Long cityId, @Param("colony") String colony, @Param("postalCode") String postalCode, Pageable pageable);

    @Query(value = Constants.FIND_COLONIES_BY_STATE_NAME_CITY_NAME_COLONY_NAME_CODE_POSTAL, nativeQuery = true)
    Page<Colony> findAllByStateNameAndCityNameAndColonyNameCodePostal(@Param("state") String state, @Param("city") String city, @Param("colony") String colony, @Param("postalCode") String postalCode, Pageable pageable);
}
