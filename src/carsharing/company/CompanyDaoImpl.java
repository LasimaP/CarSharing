package carsharing.company;

import carsharing.dataaccess.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {
    private final DatabaseConnector databaseConnector;

    public CompanyDaoImpl(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
        createCompanyTable();
    }


    @Override
    public void dropCompanyTable() {
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "DROP TABLE IF EXISTS COMPANY";
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public void createCompanyTable() {
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
                    "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(45) NOT NULL UNIQUE, " +
                    "PRIMARY KEY(id))";
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public void addCompany(String companyName) {
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "INSERT INTO COMPANY (name) " +
                    "VALUES ('" + companyName + "')";
            stmt.executeUpdate(sql);

            System.out.println("The company was created!");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public List<Company> listCompanies() {
        List<Company> companyList = new ArrayList<>();
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE)
        ) {
            String sql = "SELECT * FROM COMPANY";
            ResultSet rs = stmt.executeQuery(sql);


            while (rs.next()) {
                int companyID = rs.getInt(1);
                String companyName = rs.getObject(2).toString();
                Company company = new Company(companyName, companyID);
                companyList.add(company);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return companyList;
    }

    public Company getCompany(int companyID) {
        Company company = null;
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "SELECT * " +
                    "FROM COMPANY " +
                    "WHERE id = " + companyID;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String companyName = rs.getObject(2).toString();
                company = new Company(companyName, companyID);
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return company;
    }
    public void closeConnection() {
        this.databaseConnector.closeConnection();
    }
}

