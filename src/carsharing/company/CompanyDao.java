package carsharing.company;

public interface CompanyDao {
    void dropCompanyTable();
    void createCompanyTable();
    void addCompany(String name);
    boolean listCompanies();
}
