package id.co.nds.catalogue.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import id.co.nds.catalogue.entities.ProductEntity;
import id.co.nds.catalogue.globals.GlobalConstant;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface ProductRepo extends JpaRepository<ProductEntity, Integer>, JpaSpecificationExecutor<ProductEntity> {
    @Query(value = "SELECT COUNT(*) FROM ms_product where rec_status = '" + GlobalConstant.REC_STATUS_ACTIVE
            + "' AND LOWER(name) = LOWER(:name)", nativeQuery = true)
    long countByName(@Param("name") String name);

    @Modifying
    @Query(value = "Update ms_product SET rec_status = '" + GlobalConstant.REC_STATUS_NON_ACTIVE
            + "', deleter_id = ?2, deleted_date = NOW() " + "WHERE id = ?1", nativeQuery = true)
    Integer doDelete(Integer id, Integer deleterId);
}
