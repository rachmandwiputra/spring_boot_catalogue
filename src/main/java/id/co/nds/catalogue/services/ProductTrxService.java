package id.co.nds.catalogue.services;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.co.nds.catalogue.entities.ProductEntity;
import id.co.nds.catalogue.globals.GlobalConstant;
import id.co.nds.catalogue.models.ProductModel;
import id.co.nds.catalogue.repos.ProductRepo;

@Service
public class ProductTrxService implements Serializable {

    @Autowired
    ProductRepo productRepo;

    public List<ProductEntity> doSave(List<ProductModel> productmodels) throws Exception {

        // Validate
        ArrayList<String> arError = new ArrayList<String>();

        for (ProductModel productModel : productmodels) {
            if (productModel.getName() == null || productModel.getName().trim().equalsIgnoreCase("")) {
                arError.add("Product name is required!");
            }

            if (productModel.getQuantity() == null) {
                arError.add("Product quantity is required!");
            }

            if (productModel.getCategoryId() == null) {
                arError.add("Product category is required!");
            }
        }

        if (arError.size() > 0) {
            throw new Exception(arError.toString());
        }

        List<ProductEntity> result = null;
        ProductEntity productEntity = null;

        for (ProductModel productModel : productmodels) {
            System.out.println("insert product " + productModel.getName() + "\n\n");

            Long checkData = productRepo.countByName(productModel.getName());
            if (checkData > 0) {
                throw new Exception("active Product name (" + productModel.getName() + ") is already exist");
            }

            productEntity = new ProductEntity();
            productEntity.setName(productModel.getName());
            productEntity.setQuantity(productModel.getQuantity());
            productEntity.setCategoryId(productModel.getCategoryId());

            productEntity.setRecStatus(GlobalConstant.REC_STATUS_ACTIVE);
            productEntity.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            productEntity.setCreatorId(productModel.getActorId() == 0 ? 0 : productModel.getActorId());

            productEntity = productRepo.save(productEntity);

            result = new ArrayList<ProductEntity>();

            result.add(productEntity);

        }

        return result;
    }

}
