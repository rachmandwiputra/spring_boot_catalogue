package id.co.nds.catalogue.repos.specs;

import org.springframework.data.jpa.domain.Specification;
import id.co.nds.catalogue.entities.UserEntity;
import id.co.nds.catalogue.models.UserModel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import id.co.nds.catalogue.globals.GlobalConstant;

public class UserSpec implements Specification<UserEntity> {
    private UserModel userModel;

    public UserSpec(UserModel userModel) {
        super();
        this.userModel = userModel;
    }

    @Override
    public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.and();

        // id criteria
        if (userModel.getId() != null && userModel.getId() != 0) {
            predicate.getExpressions().add(criteriaBuilder.equal(root.get("id"), userModel.getId()));
        }

        // fullname criteria
        if (userModel.getFullname() != null && userModel.getFullname().length() > 0) {
            predicate.getExpressions().add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullname")),
                    "%" + userModel.getFullname().toLowerCase() + "%"));
        }

        // role id criteria
        if (userModel.getRoleId() != null && userModel.getRoleId().length() > 0) {
            predicate.getExpressions().add(criteriaBuilder.like(criteriaBuilder.lower(root.get("roleId")),
                    "%" + userModel.getRoleId().toLowerCase() + "%"));
        }

        // callnumber criteria
        if (userModel.getCallNumber() != null && userModel.getCallNumber().length() > 0) {
            predicate.getExpressions().add(criteriaBuilder.like(criteriaBuilder.lower(root.get("callNumber")),
                    "%" + userModel.getCallNumber().toLowerCase() + "%"));
        }

        // rec status id criteria
        if (userModel.getRecStatus() != null
                && (userModel.getRecStatus().trim().equalsIgnoreCase(GlobalConstant.REC_STATUS_ACTIVE)
                        || userModel.getRecStatus().trim().equalsIgnoreCase(GlobalConstant.REC_STATUS_NON_ACTIVE))) {
            predicate.getExpressions().add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("recStatus")),
                    userModel.getRecStatus().toUpperCase()));
        }

        query.orderBy(criteriaBuilder.asc(root.get("id")));

        return predicate;
    }

}
