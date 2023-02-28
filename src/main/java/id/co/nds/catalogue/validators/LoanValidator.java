package id.co.nds.catalogue.validators;

import java.sql.Timestamp;

import id.co.nds.catalogue.exceptions.ClientException;
import id.co.nds.catalogue.exceptions.NotFoundException;

public class LoanValidator {
    public void nullCheckId(String id) throws ClientException {
        if (id.equals(null)) {
            throw new ClientException("Loan id is required");
        }
    }

    public void nullCheckLoanAmount(Double loanAmount) throws ClientException {
        if (loanAmount == null) {
            throw new ClientException("Loan amount is required");
        }
    }

    public void nullCheckInterestRate(Double interestRate) throws ClientException {
        if (interestRate == null) {
            throw new ClientException("Loan interest rate is required");
        }
    }

    public void nullCheckCustomerName(String customerName) throws ClientException {
        if (customerName.equals(null)) {
            throw new ClientException("Loan customer name is required");
        }
    }

    public void nullCheckLoanTerm(Integer term) throws ClientException {
        if (term == null) {
            throw new ClientException("Loan term name is required");
        }
    }

    public void nullCheckStartDate(Timestamp startDate) throws ClientException {
        if (startDate == null) {
            throw new ClientException("Loan start date is required");
        }
    }

    public void validateLoanTerm(Integer term) throws ClientException {
        if (term < 1) {
            throw new ClientException("Loan term must be positive integer number");
        }
    }

    public void nullCheckObject(Object object) throws NotFoundException {
        if (object == null) {
            throw new NotFoundException("Loan is not found");
        }
    }
}
