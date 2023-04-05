package carsharing.company;

public interface CompanyDao {
    //    List<Company> getAllCompanies();
    void dropCompanyTable();
    void createCompanyTable();
    void addCompany(String name);
    void listCompanies();
}
