package id.co.nds.catalogue.services;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.co.nds.catalogue.entities.ProductEntity;
import id.co.nds.catalogue.entities.ProductInfoEntity;
import id.co.nds.catalogue.exceptions.ClientException;
import id.co.nds.catalogue.exceptions.NotFoundException;
import id.co.nds.catalogue.globals.GlobalConstant;
import id.co.nds.catalogue.models.ProductModel;
import id.co.nds.catalogue.repos.ProductInfoRepo;
import id.co.nds.catalogue.repos.ProductRepo;
import id.co.nds.catalogue.repos.specs.ProductSpec;
import id.co.nds.catalogue.validators.CategoryValidator;
import id.co.nds.catalogue.validators.ProductValidator;

@Service
public class ProductService implements Serializable {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductInfoRepo productInfoRepo;

    // private ProductTrxService productTrxService;

    ProductValidator productValidator = new ProductValidator();
    CategoryValidator categoryValidator = new CategoryValidator();

    public ProductEntity add(ProductModel productModel) throws ClientException {
        productValidator.notNullCheckProductId(productModel.getId());
        productValidator.nullCheckName(productModel.getName());
        productValidator.validateName(productModel.getName());
        productValidator.nullCheckQuantity(productModel.getQuantity());
        productValidator.validateQuantity(productModel.getQuantity());
        productValidator.nullCheckCategoryId(productModel.getCategoryId());
        productValidator.validateCategoryId(productModel.getCategoryId());

        System.out.println("masuk");
        Long count = productRepo.countByName(productModel.getName());

        if (count > 0) {
            throw new ClientException("product name is already existed");
        }

        ProductEntity product = new ProductEntity();
        product.setName(productModel.getName());
        product.setQuantity(productModel.getQuantity());
        product.setCategoryId(productModel.getCategoryId());
        product.setRecStatus(GlobalConstant.REC_STATUS_ACTIVE);
        product.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        product.setCreatorId(productModel.getActorId() == null ? 0 : productModel.getActorId());

        return productRepo.save(product);
    }

    public List<ProductEntity> findAll() {
        List<ProductEntity> products = new ArrayList<>();
        productRepo.findAll().forEach(products::add);

        return products;
    }

    public List<ProductEntity> findAllByCriteria(ProductModel productModel) {
        List<ProductEntity> products = new ArrayList<>();
        ProductSpec specs = new ProductSpec(productModel);
        productRepo.findAll(specs).forEach(products::add);

        return products;
    }

    public ProductEntity findById(Integer id) throws ClientException, NotFoundException {
        productValidator.nullCheckProductId(id);

        ProductEntity product = productRepo.findById(id).orElse(null);
        productValidator.nullCheckObject(product);

        return product;
    }

    public List<ProductInfoEntity> findAllByCategory(String categoryId) throws ClientException, NotFoundException {
        categoryValidator.nullCheckCategoryId(categoryId);
        categoryValidator.validateCategoryId(categoryId);

        List<ProductInfoEntity> products = productInfoRepo.findAllByCategory(categoryId);
        productValidator.nullCheckObject(products);

        return products;
    }

    public List<ProductEntity> findProductsByCategory(String categoryId) throws ClientException, NotFoundException {
        categoryValidator.nullCheckCategoryId(categoryId);
        categoryValidator.validateCategoryId(categoryId);

        List<ProductEntity> products = productRepo.findProductsByCategoryId(categoryId);
        productValidator.nullCheckObject(products);

        return products;
    }

    public ProductEntity edit(ProductModel productModel) throws ClientException, NotFoundException {
        productValidator.nullCheckProductId(productModel.getId());

        if (!productRepo.existsById(productModel.getId())) {
            throw new NotFoundException("Cannot find product with id " + productModel.getId());
        }

        ProductEntity product = new ProductEntity();
        product = findById(productModel.getId());

        if (productModel.getName() != null) {
            productValidator.validateName(productModel.getName());

            Long count = productRepo.countByName(productModel.getName());

            if (count > 0 && !product.getName().equals(productModel.getName())) {
                throw new ClientException("Product name is already existed");
            }

            product.setName(productModel.getName());
        }

        if (productModel.getQuantity() != null) {
            productValidator.validateQuantity(productModel.getQuantity());

            product.setQuantity(productModel.getQuantity());
        }

        if (productModel.getCategoryId() != null) {
            productValidator.validateCategoryId(productModel.getCategoryId());

            product.setCategoryId(productModel.getCategoryId());
        }

        product.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        product.setUpdaterId(productModel.getActorId() == null ? 0 : productModel.getActorId());

        return productRepo.save(product);
    }

    public ProductEntity delete(Integer productId, Integer actorId) throws ClientException, NotFoundException {
        productValidator.nullCheckProductId(productId);

        if (!productRepo.existsById(productId)) {
            throw new NotFoundException("Cannot find product with id: " + productId);
        }

        ProductEntity product = new ProductEntity();
        product = findById(productId);

        if (product.getRecStatus().equalsIgnoreCase(GlobalConstant.REC_STATUS_NON_ACTIVE)) {
            throw new ClientException("Product id (" + productId + ") is already been deleted.");
        }

        product.setRecStatus(GlobalConstant.REC_STATUS_NON_ACTIVE);
        product.setDeletedDate(new Timestamp(System.currentTimeMillis()));
        product.setDeleterId(actorId == null ? 0 : actorId);

        // Alternative Delete Fuction
        // productRepo.doDelete(product.getId(), product.getDeleterId());
        // return product;

        return productRepo.save(product);
    }

    // public ResponseEntity<String> addMany(List<ProductModel> models){
    // ResponseModel response = new ResponseModel();

    // try {
    // List<ProductEntity> arData = null;
    // arData = productTrxService.doSave(models);

    // response.setMessage("Successfully create new products");
    // response.setData((arData == null) ? "" : arData.toString());

    // String c = null;
    // if(arData == null)
    // {
    // c = "";
    // }else{
    // c = arData.toString();
    // }

    // return new ResponseEntity<String>()
    // } catch (Exception e) {
    // response.setMessage(e.getMessage());
    // e.printStackTrace();

    // return new ResponseEntity<>(Global)
    // }
    // }
}
