package id.co.nds.catalogue.validators;

import id.co.nds.catalogue.exceptions.ClientException;
import id.co.nds.catalogue.exceptions.NotFoundException;
import id.co.nds.catalogue.globals.GlobalConstant;

public class UserValidator {
    public void notNullCheckUserId(Integer id) throws ClientException {
        if (id != null) {
            throw new ClientException("User id is auto generated, do not input id");
        }
    }

    public void nullCheckUserId(Integer id) throws ClientException {
        if (id == null) {
            throw new ClientException("User id is required");
        }
    }

    public void nullCheckFullname(String fullname) throws ClientException {
        if (fullname == null) {
            throw new ClientException("User fullname is required");
        }
    }

    public void nullCheckRoleId(String roleId) throws ClientException {
        if (roleId == null) {
            throw new ClientException("User role id is required");
        }
    }

    public void nullCheckCallNumber(String callNumber) throws ClientException {
        if (callNumber == null) {
            throw new ClientException("User call number is required");
        }
    }

    public void nullCheckObject(Object object) throws NotFoundException {
        if (object == null) {
            throw new NotFoundException("User is not found");
        }
    }

    public void validateUserId(Integer id) throws ClientException {
        if (id <= 0) {
            throw new ClientException("User id input is invalid");
        }
    }

    public void validateFullname(String name) throws ClientException {
        if (name.trim().equalsIgnoreCase("")) {
            throw new ClientException("User name is required");
        }
    }

    public void validateRoleId(String roleId) throws ClientException {
        if (roleId.length() != 5 || !roleId.startsWith("R")) {
            throw new ClientException(
                    "User role id must be start with R and containts 5 characters");
        }
    }

    public void validateCallNumber(String callnumber) throws ClientException {
        if (callnumber.length() < 9 || callnumber.length() > 12
                || (!callnumber.startsWith("0") && !callnumber.startsWith("+62"))) {
            throw new ClientException(
                    "User callnumber must be start with 0 or +62 and only containts 9-12 digit numbers");
        }
    }

    public void validateRecStatus(String id, String recStatus) throws ClientException {
        if (recStatus.equalsIgnoreCase(GlobalConstant.REC_STATUS_NON_ACTIVE)) {
            throw new ClientException("User with id = " + id + "is already been deleted");
        }
    }
}
