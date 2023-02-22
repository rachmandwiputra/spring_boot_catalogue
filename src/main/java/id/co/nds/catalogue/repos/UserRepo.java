package id.co.nds.catalogue.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import id.co.nds.catalogue.entities.UserEntity;
import id.co.nds.catalogue.globals.GlobalConstant;

@Repository
public interface UserRepo extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity> {
        @Query(value = "SELECT COUNT(*) FROM ms_user where rec_status = '" + GlobalConstant.REC_STATUS_ACTIVE
                        + "' AND LOWER(fullname) = LOWER(:fullname)", nativeQuery = true)
        long countByFullName(@Param("fullname") String fullname);

        @Query(value = "SELECT COUNT(*) FROM ms_user where rec_status = '" + GlobalConstant.REC_STATUS_ACTIVE
                        + "' AND LOWER(call_number) = LOWER(:call_number)", nativeQuery = true)
        long countByCallNumber(@Param("call_number") String callNumber);

        @Query(value = "SELECT p.*, c.name AS role_name FROM ms_user AS p " +
                        "JOIN ms_role AS c ON p.role_id = c.id " +
                        "WHERE p.role_id = ?1", nativeQuery = true)
        List<UserEntity> findUsersByRoleId(String roleId);
}
