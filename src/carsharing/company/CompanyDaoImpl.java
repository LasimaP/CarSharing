package carsharing.company;

import carsharing.dataaccess.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
            //System.out.println("Dropping a table...");
            String sql = "DROP TABLE IF EXISTS COMPANY";
            stmt.executeUpdate(sql);
            //System.out.println("Table dropped");
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
    public void addCompany(String name) {
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            Company company = new Company(name);
            String sql = "INSERT INTO COMPANY (name) " +
                    "VALUES ('" + company.getName() + "')";
            stmt.executeUpdate(sql);
            System.out.println("The company was created!");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public boolean listCompanies() {
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE)
        ) {
            String sql = "SELECT * FROM COMPANY";
            ResultSet rs = stmt.executeQuery(sql);


            if (rs.next()) {
                System.out.println("Choose the company:");
                rs.previous();
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String companyName = rs.getObject(2).toString();
                    System.out.println(id + ". " + companyName);
                }
                return true;
            } else {
                System.out.println("The company list is empty!");
                return false;
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return false;
    }

    public String getCompany (int id) {
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "SELECT * " +
                    "FROM COMPANY " +
                    "WHERE id = " + id;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getObject(2).toString();
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return "null";
    }

    public void closeConnection() {
        this.databaseConnector.closeConnection();
    }
}
