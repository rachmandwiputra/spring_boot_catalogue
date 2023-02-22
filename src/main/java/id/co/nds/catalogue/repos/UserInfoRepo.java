package id.co.nds.catalogue.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import id.co.nds.catalogue.entities.UserInfoEntity;

@Repository
@Transactional
public interface UserInfoRepo extends JpaRepository<UserInfoEntity, String> {

    @Query(value = "SELECT p.*, c.name AS role_name FROM ms_user AS p " +
            "JOIN ms_role AS c ON p.role_id = c.id " +
            "WHERE p.role_id = ?1", nativeQuery = true)
    List<UserInfoEntity> findAllByRole(String categoryId);
}
