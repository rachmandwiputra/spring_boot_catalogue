package id.co.nds.catalogue.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import id.co.nds.catalogue.entities.LoanEntity;
import id.co.nds.catalogue.exceptions.ClientException;
import id.co.nds.catalogue.exceptions.NotFoundException;
import id.co.nds.catalogue.models.LoanModel;
import id.co.nds.catalogue.repos.LoanRepo;
import id.co.nds.catalogue.repos.RoleRepo;
import id.co.nds.catalogue.repos.UserRepo;
import id.co.nds.catalogue.validators.LoanValidator;
import id.co.nds.catalogue.validators.RoleValidator;
import id.co.nds.catalogue.validators.UserValidator;

@Service
public class LoanService {
    @Autowired
    private LoanRepo loanRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    private UserValidator userValidator = new UserValidator();
    private RoleValidator roleValidator = new RoleValidator();
    private LoanValidator loanValidator = new LoanValidator();

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = { Exception.class })
    public LoanEntity doLoan(LoanModel loanModel) throws ClientException, NotFoundException {
        userValidator.nullCheckUserId(loanModel.getUserId());
        userValidator.validateUserId(loanModel.getUserId());
        roleValidator.nullCheckRoleId(loanModel.getRoleId());
        roleValidator.validateRoleId(loanModel.getRoleId());

        loanValidator.nullCheckLoanAmount(loanModel.getLoanAmount());
        loanValidator.nullCheckInterestRate(loanModel.getInterestRate());
        loanValidator.nullCheckStartDate(loanModel.getStartDate());
        loanValidator.nullCheckLoanTerm(loanModel.getLoanTerm());
        loanValidator.validateLoanTerm(loanModel.getLoanTerm());

        // check user Id
        if (!userRepo.existsById(loanModel.getUserId())) {
            throw new NotFoundException("Cannot find user with id: " + loanModel.getUserId());
        }

        // check role Id
        if (!roleRepo.existsById(loanModel.getRoleId())) {
            throw new NotFoundException("Cannot find role with id: " + loanModel.getRoleId());
        }

        LoanEntity loanEntity = new LoanEntity();

        loanEntity.setUserId(loanModel.getUserId());
        loanEntity.setRoleId(loanModel.getRoleId());
        loanEntity.setLoanAmount(loanModel.getLoanAmount());
        loanEntity.setLoanTerm(loanModel.getLoanTerm());
        loanEntity.setInterestRate(loanModel.getInterestRate());
        loanEntity
                .setLoanTotal(loanModel.getLoanAmount() / loanModel.getLoanTerm() * loanModel.getInterestRate() / 100);
        loanEntity.setCustomerName(loanModel.getCustomerName().trim());
        loanEntity.setStartDate(loanModel.getStartDate());

        return loanRepo.save(loanEntity);
    }
}
