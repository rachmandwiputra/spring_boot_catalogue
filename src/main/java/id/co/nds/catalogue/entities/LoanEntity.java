package id.co.nds.catalogue.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "tx_loan")
public class LoanEntity {
    @Id
    @GenericGenerator(name = "loan_id_seq", strategy = "id.co.nds.catalogue.generators.LoanIdGenerator")
    @GeneratedValue(generator = "loan_id_seq")
    @Column(name = "id")
    private String id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role_id")
    private String roleId;

    @Column(name = "loan_amount")
    private Double loanAmount;

    @Column(name = "loan_term")
    private Integer loanTerm;

    @Column(name = "interest_rate")
    private Double interestRate;

    @Column(name = "loan_total")
    private Double loanTotal;

    @Column(name = "custome_name")
    private String customerName;

    @Column(name = "start_date")
    private Timestamp startDate;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return this.roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Double getLoanAmount() {
        return this.loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getLoanTerm() {
        return this.loanTerm;
    }

    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }

    public Double getInterestRate() {
        return this.interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Double getLoanTotal() {
        return this.loanTotal;
    }

    public void setLoanTotal(Double loanTotal) {
        this.loanTotal = loanTotal;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Timestamp getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

}
