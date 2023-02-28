package id.co.nds.catalogue.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.co.nds.catalogue.entities.UserEntity;
import id.co.nds.catalogue.entities.UserInfoEntity;
import id.co.nds.catalogue.exceptions.ClientException;
import id.co.nds.catalogue.exceptions.NotFoundException;
import id.co.nds.catalogue.globals.GlobalConstant;
import id.co.nds.catalogue.models.UserModel;
import id.co.nds.catalogue.repos.UserInfoRepo;
import id.co.nds.catalogue.repos.UserRepo;
import id.co.nds.catalogue.repos.specs.UserSpec;
import id.co.nds.catalogue.validators.RoleValidator;
import id.co.nds.catalogue.validators.UserValidator;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserInfoRepo userInfoRepo;

    UserValidator userValidator = new UserValidator();
    RoleValidator roleValidator = new RoleValidator();

    public UserEntity add(UserModel userModel) throws ClientException {

        // Validate
        // Required
        userValidator.nullCheckFullname(userModel.getFullname());
        userValidator.nullCheckRoleId(userModel.getRoleId());
        userValidator.validateRoleId(userModel.getRoleId());
        userValidator.validateCallNumber(userModel.getCallNumber());
        // Optisonal

        Long count = userRepo.countByCallNumber(userModel.getCallNumber());

        if (count > 0) {
            throw new ClientException("user call number is already existed");
        }

        // Proses
        UserEntity user = new UserEntity();
        user.setFullname(userModel.getFullname());
        user.setRoleId(userModel.getRoleId());
        user.setCallNumber(userModel.getCallNumber());
        user.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        user.setCreatorId(userModel.getActorId() == null ? 0 : userModel.getActorId());
        user.setRecStatus(GlobalConstant.REC_STATUS_ACTIVE);

        return userRepo.save(user);
    }

    public List<UserEntity> findAll() {
        List<UserEntity> users = new ArrayList<>();
        userRepo.findAll().forEach(users::add);

        return users;
    }

    public List<UserEntity> findAllByCriteria(UserModel userModel) {
        List<UserEntity> users = new ArrayList<>();
        UserSpec specs = new UserSpec(userModel);
        userRepo.findAll(specs).forEach(users::add);

        return users;
    }

    public UserEntity findById(Integer id) throws ClientException, NotFoundException {
        userValidator.nullCheckUserId(id);
        userValidator.validateUserId(id);

        UserEntity user = userRepo.findById(id).orElse(null);
        userValidator.nullCheckObject(user);

        return user;
    }

    public List<UserInfoEntity> findAllByRole(String categoryId) throws ClientException, NotFoundException {
        roleValidator.nullCheckRoleId(categoryId);
        roleValidator.validateRoleId(categoryId);

        List<UserInfoEntity> users = userInfoRepo.findAllByRole(categoryId);
        userValidator.nullCheckObject(users);

        return users;
    }

    public List<UserEntity> findUsersByRole(String categoryId) throws ClientException, NotFoundException {
        roleValidator.nullCheckRoleId(categoryId);
        roleValidator.validateRoleId(categoryId);

        List<UserEntity> users = userRepo.findUsersByRoleId(categoryId);
        userValidator.nullCheckObject(users);

        return users;
    }

    public UserEntity edit(UserModel userModel) throws ClientException, NotFoundException {
        userValidator.nullCheckUserId(userModel.getId());
        userValidator.validateUserId(userModel.getId());

        if (!userRepo.existsById(userModel.getId())) {
            throw new NotFoundException("Cannot find user with id " + userModel.getId());
        }

        UserEntity user = new UserEntity();
        user = findById(userModel.getId());

        // fullname
        if (userModel.getFullname() != null) {
            userValidator.validateFullname(userModel.getFullname());

            user.setFullname(userModel.getFullname());
        }

        // role id
        if (userModel.getRoleId() != null) {
            userValidator.validateRoleId(userModel.getRoleId());

            user.setRoleId(userModel.getRoleId());
        }

        // callnumber
        if (userModel.getCallNumber() != null) {
            userValidator.validateCallNumber(userModel.getCallNumber());

            Long count = userRepo.countByCallNumber(userModel.getCallNumber());

            if (count > 0 && !user.getCallNumber().equals(userModel.getCallNumber())) {
                throw new ClientException("User callnumber is already existed");
            }

            user.setCallNumber(userModel.getCallNumber());
        }

        user.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        user.setUpdaterId(userModel.getActorId() == null ? 0 : userModel.getActorId());

        return userRepo.save(user);
    }

    public UserEntity delete(Integer id, Integer actorId) throws ClientException, NotFoundException {
        userValidator.nullCheckUserId(id);
        userValidator.validateUserId(id);

        if (!userRepo.existsById(id)) {
            throw new NotFoundException("Cannot find user with id: " + id);
        }

        UserEntity user = new UserEntity();
        user = findById(id);

        if (user.getRecStatus().equalsIgnoreCase(GlobalConstant.REC_STATUS_NON_ACTIVE)) {
            throw new ClientException("User id (" + id + ") is already been deleted.");
        }

        user.setRecStatus(GlobalConstant.REC_STATUS_NON_ACTIVE);
        user.setDeletedDate(new Timestamp(System.currentTimeMillis()));
        user.setDeleterId(actorId == null ? 0 : actorId);

        // Alternative Delete Fuction
        // productRepo.doDelete(product.getId(), product.getDeleterId());
        // return product;

        return userRepo.save(user);
    }
}
