package id.co.nds.catalogue.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.co.nds.catalogue.entities.UserEntity;
import id.co.nds.catalogue.exceptions.ClientException;
import id.co.nds.catalogue.exceptions.NotFoundException;
import id.co.nds.catalogue.globals.GlobalConstant;
import id.co.nds.catalogue.models.UserModel;
import id.co.nds.catalogue.repos.UserRepo;
import id.co.nds.catalogue.repos.specs.UserSpec;
import id.co.nds.catalogue.validators.UserValidator;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    UserValidator userValidator = new UserValidator();

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

            if (count > 0 && userModel.getCallNumber() != userModel.getCallNumber()) {
                throw new ClientException("User callnumber is already existed");
            }

            user.setCallNumber(userModel.getCallNumber());
        }

        user.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        user.setUpdaterId(userModel.getActorId() == null ? 0 : userModel.getActorId());

        return userRepo.save(user);
    }

    public UserEntity delete(UserModel userModel) throws ClientException, NotFoundException {
        userValidator.nullCheckUserId(userModel.getId());
        userValidator.validateUserId(userModel.getId());

        if (!userRepo.existsById(userModel.getId())) {
            throw new NotFoundException("Cannot find user with id: " + userModel.getId());
        }

        UserEntity user = new UserEntity();
        user = findById(userModel.getId());

        if (user.getRecStatus().equalsIgnoreCase(GlobalConstant.REC_STATUS_NON_ACTIVE)) {
            throw new ClientException("User id (" + userModel.getId() + ") is already been deleted.");
        }

        user.setRecStatus(GlobalConstant.REC_STATUS_NON_ACTIVE);
        user.setDeletedDate(new Timestamp(System.currentTimeMillis()));
        user.setDeleterId(userModel.getActorId() == null ? 0 : userModel.getActorId());

        // Alternative Delete Fuction
        // productRepo.doDelete(product.getId(), product.getDeleterId());
        // return product;

        return userRepo.save(user);
    }
}
