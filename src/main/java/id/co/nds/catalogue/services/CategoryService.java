package id.co.nds.catalogue.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.co.nds.catalogue.entities.CategoryEntity;
import id.co.nds.catalogue.exceptions.ClientException;
import id.co.nds.catalogue.exceptions.NotFoundException;
import id.co.nds.catalogue.generators.CategoryIdGenerator;
import id.co.nds.catalogue.globals.GlobalConstant;
import id.co.nds.catalogue.models.CategoryModel;
import id.co.nds.catalogue.repos.CategoryRepo;
import id.co.nds.catalogue.validators.CategoryValidator;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepo categoryRepo;

    CategoryValidator categoryValidator = new CategoryValidator();
    CategoryIdGenerator categoryIdGenerator = new CategoryIdGenerator();

    public CategoryEntity add(CategoryModel categoryModel) throws ClientException {
        categoryValidator.notNullCheckCategoryId(categoryModel.getId());

        categoryValidator.nullCheckName(categoryModel.getName());
        categoryValidator.validateName(categoryModel.getName());

        Long count = categoryRepo.countByName(categoryModel.getName());

        if (count > 0) {
            throw new ClientException("category name is already existed");
        }

        CategoryEntity category = new CategoryEntity();

        category.setName(categoryModel.getName());
        category.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        category.setCreatorId(categoryModel.getActorId() == null ? 0 : categoryModel.getActorId());
        category.setRecStatus(GlobalConstant.REC_STATUS_ACTIVE);

        return categoryRepo.save(category);
    }

    public List<CategoryEntity> findAll() {
        List<CategoryEntity> categories = new ArrayList<>();
        categoryRepo.allCategorySortedById().forEach(categories::add);

        return categories;
    }

    public CategoryEntity findById(String id) throws ClientException, NotFoundException {
        categoryValidator.nullCheckCategoryId(id);
        categoryValidator.validateCategoryId(id);

        CategoryEntity category = categoryRepo.findById(id).orElse(null);
        categoryValidator.nullCheckObject(category);

        return category;
    }

    public CategoryEntity edit(CategoryModel categoryModel) throws ClientException, NotFoundException {
        categoryValidator.nullCheckCategoryId(categoryModel.getId());
        categoryValidator.validateCategoryId(categoryModel.getId());

        if (!categoryRepo.existsById(categoryModel.getId())) {
            throw new NotFoundException("Cannot find category with id " + categoryModel.getId());
        }

        CategoryEntity category = new CategoryEntity();
        category = findById(categoryModel.getId());

        // Nama
        if (categoryModel.getName() != null) {
            categoryValidator.validateName(categoryModel.getName());

            Long count = categoryRepo.countByName(categoryModel.getName());

            if (count > 0 && !category.getName().toLowerCase().equals(categoryModel.getName().toLowerCase())) {
                throw new ClientException("Category name is already existed");
            }

            category.setName(categoryModel.getName());
        }

        category.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        category.setUpdaterId(categoryModel.getActorId() == null ? 0 : categoryModel.getActorId());

        return categoryRepo.save(category);
    }

    public CategoryEntity delete(String id, Integer actorId) throws ClientException, NotFoundException {
        categoryValidator.nullCheckCategoryId(id);
        categoryValidator.validateCategoryId(id);

        if (!categoryRepo.existsById(id)) {
            throw new NotFoundException("Cannot find category with id: " + id);
        }

        CategoryEntity category = new CategoryEntity();
        category = findById(id);

        if (category.getRecStatus().equalsIgnoreCase(GlobalConstant.REC_STATUS_NON_ACTIVE)) {
            throw new ClientException("Category id (" + id + ") is already been deleted.");
        }

        category.setRecStatus(GlobalConstant.REC_STATUS_NON_ACTIVE);
        category.setDeletedDate(new Timestamp(System.currentTimeMillis()));
        category.setDeleterId(actorId == null ? 0 : actorId);

        return categoryRepo.save(category);
    }
}
