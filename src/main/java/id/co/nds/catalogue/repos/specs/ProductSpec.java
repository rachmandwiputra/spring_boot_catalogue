package id.co.nds.catalogue.repos.specs;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import id.co.nds.catalogue.entities.ProductEntity;
import id.co.nds.catalogue.globals.GlobalConstant;
import id.co.nds.catalogue.models.ProductModel;

public class ProductSpec implements Specification<ProductEntity> {
    private ProductModel productModel;

    public ProductSpec(ProductModel productModel) {
        super();
        this.productModel = productModel;
    }

    @Override
    public Predicate toPredicate(Root<ProductEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.and();

        // id criteria
        if (productModel.getId() != null && productModel.getId() != 0) {
            predicate.getExpressions().add(criteriaBuilder.equal(root.get("id"), productModel.getId()));
        }

        // name criteria
        if (productModel.getName() != null && productModel.getName().length() > 0) {
            predicate.getExpressions().add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    "%" + productModel.getName().toLowerCase() + "%"));
        }

        // rec_status criteria
        if (productModel.getRecStatus() != null
                && (productModel.getRecStatus().trim().equalsIgnoreCase(GlobalConstant.REC_STATUS_ACTIVE)
                        || productModel.getRecStatus().trim().equalsIgnoreCase(GlobalConstant.REC_STATUS_NON_ACTIVE))) {
            predicate.getExpressions().add(criteriaBuilder.equal(criteriaBuilder.upper(root.get("recStatus")),
                    productModel.getRecStatus().toUpperCase()));
        }

        query.orderBy(criteriaBuilder.asc(root.get("id")));

        return predicate;
    }

}
