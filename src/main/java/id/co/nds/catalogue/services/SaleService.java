package id.co.nds.catalogue.services;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import id.co.nds.catalogue.entities.ProductEntity;
import id.co.nds.catalogue.entities.SaleEntity;
import id.co.nds.catalogue.exceptions.ClientException;
import id.co.nds.catalogue.exceptions.NotFoundException;
import id.co.nds.catalogue.models.SaleModel;
import id.co.nds.catalogue.repos.ProductRepo;
import id.co.nds.catalogue.repos.SaleRepo;
import id.co.nds.catalogue.validators.ProductValidator;

@Service
public class SaleService {
    @Autowired
    private SaleRepo saleRepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductService productService;

    private ProductValidator productValidator = new ProductValidator();

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { Exception.class })
    public SaleEntity doSale(SaleModel saleModel) throws ClientException, NotFoundException {
        productValidator.nullCheckProductId(saleModel.getProductId());
        productValidator.nullCheckQuantity(saleModel.getQuantity());

        if (!productRepo.existsById(saleModel.getProductId())) {
            throw new NotFoundException("Cannot find product with id: " + saleModel.getProductId());
        }

        if (saleModel.getPrice() == null || saleModel.getQuantity() == null) {
            throw new ClientException("price or quantity cannot be null");
        }

        ProductEntity productEntity = productService.findById(saleModel.getProductId());
        productEntity.setQuantity(productEntity.getQuantity() - saleModel.getQuantity());

        productRepo.save(productEntity);

        SaleEntity saleEntity = new SaleEntity();

        saleEntity.setProductId(saleModel.getProductId());
        saleEntity.setPrice(saleModel.getPrice().doubleValue());
        saleEntity.setQuantity(saleModel.getQuantity());
        saleEntity.setTotalPrice(saleModel.getPrice().doubleValue() * saleModel.getQuantity());
        saleEntity.setCreatedData(new Timestamp(System.currentTimeMillis()));

        return saleRepo.save(saleEntity);
    }
}
