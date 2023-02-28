package id.co.nds.catalogue.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import id.co.nds.catalogue.entities.CategoryEntity;
import id.co.nds.catalogue.globals.GlobalConstant;

public interface CategoryRepo extends JpaRepository<CategoryEntity, String>, JpaSpecificationExecutor<CategoryEntity> {

        @Query(value = "SELECT COUNT(*) FROM ms_category WHERE rec_status = '"
                        + GlobalConstant.REC_STATUS_ACTIVE + "' AND LOWER(name) = LOWER(:name)", nativeQuery = true)
        long countByName(@Param("name") String name);

        @Query(value = "SELECT * FROM ms_category WHERE rec_status = '"
                        + GlobalConstant.REC_STATUS_ACTIVE + "' ORDER BY id ASC", nativeQuery = true)
        List<CategoryEntity> allActiveCategory();

        @Query(value = "SELECT * FROM ms_category ORDER BY id ASC", nativeQuery = true)
        List<CategoryEntity> allCategorySortedById();

}
