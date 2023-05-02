package carsharing.customer;

import carsharing.dataaccess.DatabaseConnector;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    private final DatabaseConnector databaseConnector;

    public CustomerDaoImpl(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
        createCustomerTable();
    }


    @Override
    public void dropCustomerTable() {
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "DROP TABLE IF EXISTS CUSTOMER";
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public void createCustomerTable() {
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                    "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(45) NOT NULL UNIQUE, " +
                    "rented_car_id INTEGER UNIQUE, " +
                    "PRIMARY KEY(id), " +
                    "CONSTRAINT fk_car FOREIGN KEY (rented_car_id) " +
                    "REFERENCES CAR(id))";
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public void addCustomer(String customerName) {
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "INSERT INTO CUSTOMER (name) " +
                    "VALUES ('" + customerName + "')";
            stmt.executeUpdate(sql);
            System.out.println("The customer was added!");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public List<Customer> listCustomers() {
        List<Customer> customerList = new ArrayList<>();
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE)
        ) {
            String sql = "SELECT * FROM CUSTOMER";
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int customerID = rs.getInt(1);
                String customerName = rs.getObject(2).toString();
                Customer customer = new Customer(customerName, customerID);
                customerList.add(customer);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return customerList;
    }

    public Customer getCustomer (int customerID) {
        Customer customer = null;
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "SELECT * " +
                    "FROM CUSTOMER " +
                    "WHERE id = " + customerID;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String customerName = rs.getObject(2).toString();
                customer = new Customer(customerName, customerID);
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return customer;
    }

    public boolean rentCar(int customerID, int carID) {
        boolean carRented = true;
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "UPDATE CUSTOMER " +
                    "SET rented_car_id = " + carID +
                    " WHERE id = " + customerID;
            stmt.executeUpdate(sql);
        } catch (JdbcSQLIntegrityConstraintViolationException e) {
            carRented = false;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return carRented;
    }
    public int getRentalCarID(int customerID) {
        int carID = 0;
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "SELECT rented_car_id " +
                    "FROM CUSTOMER " +
                    " WHERE id = " + customerID;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                carID = rs.getInt(1);
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return carID;
    }

    public boolean returnCar(int customerID) {
        boolean carReturned = false;
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "UPDATE CUSTOMER " +
                    "SET rented_car_id = NULL" +
                    " WHERE id = " + customerID;
            stmt.executeUpdate(sql);
            carReturned = true;
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return carReturned;
    }

    public String[] viewRentedDetails(int customerID) {
        String[] rentedDetails = new String[2];
        int carID = 0;
        int companyID = 0;
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "SELECT rented_car_id " +
                    "FROM CUSTOMER " +
                    " WHERE id = " + customerID;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                carID = rs.getInt(1);
            }
            String sql2 = "SELECT name, company_id " +
                    "FROM CAR " +
                    " WHERE id = " + carID;
            rs = stmt.executeQuery(sql2);
            if (rs.next()) {
                String carName = rs.getString(1);
                rentedDetails[0] = carName;

                companyID = rs.getInt(2);
            }
            String sql3 = "SELECT name " +
                    "FROM COMPANY " +
                    " WHERE id = " + companyID;
            rs = stmt.executeQuery(sql3);
            if (rs.next()) {
                String companyName = rs.getString(1);
                rentedDetails[1] = companyName;
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return rentedDetails;
    }
}