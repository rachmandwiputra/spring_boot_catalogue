package id.co.nds.catalogue.validators;

import id.co.nds.catalogue.exceptions.ClientException;
import id.co.nds.catalogue.exceptions.NotFoundException;
import id.co.nds.catalogue.globals.GlobalConstant;

public class ProductValidator {
    public void nullCheckProductId(Integer id) throws ClientException {
        if (id == null) {
            throw new ClientException("Product id is required");
        }
    }

    public void notNullCheckProductId(Integer id) throws ClientException {
        if (id != null) {
            throw new ClientException("Product id is auto generated, do not input id");
        }
    }

    public void nullCheckName(String name) throws ClientException {
        if (name == null) {
            throw new ClientException("Product name is required");
        }
    }

    public void nullCheckQuantity(Integer quantity) throws ClientException {
        if (quantity == null) {
            throw new ClientException("Product quantity is required");
        }
    }

    public void nullCheckCategoryId(String categoryId) throws ClientException {
        if (categoryId == null) {
            throw new ClientException("Product category id is required");
        }
    }

    public void nullCheckObject(Object object) throws NotFoundException {
        if (object == null) {
            throw new NotFoundException("Product is not found");
        }
    }

    public void validateName(String name) throws ClientException {
        if (name.trim().equalsIgnoreCase("")) {
            throw new ClientException("Product name is required");
        }
    }

    public void validateQuantity(Integer quantity) throws ClientException {
        if (quantity < 0) {
            throw new ClientException("Product quantity must be positive integer number");
        }
    }

    public void validateCategoryId(String categoryId) throws ClientException {
        if (categoryId.length() != 6 || !categoryId.startsWith("P")) {
            throw new ClientException("Product category id contains six digits and stars with P");
        }
    }

    public void validateRecStatus(String id, String recStatus) throws ClientException {
        if (recStatus.equalsIgnoreCase(GlobalConstant.REC_STATUS_NON_ACTIVE)) {
            throw new ClientException("Product with id = " + id + "is already been deleted");
        }
    }
}
