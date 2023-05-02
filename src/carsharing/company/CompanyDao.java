package carsharing.company;

import java.util.List;

public interface CompanyDao {
    void dropCompanyTable();
    void createCompanyTable();
    void addCompany(String companyName);
    List<Company> listCompanies();
}

