package carsharing.car;

import carsharing.dataaccess.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CarDaoImpl implements CarDao {
    private final DatabaseConnector databaseConnector;

    public CarDaoImpl(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
        createCarTable();
    }

    @Override
    public void dropCarTable() {
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "DROP TABLE IF EXISTS CAR";
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public void createCarTable() {
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "CREATE TABLE IF NOT EXISTS CAR " +
                    "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                    "name VARCHAR(45) NOT NULL UNIQUE, " +
                    "company_id INTEGER NOT NULL, " +
                    "PRIMARY KEY(id), " +
                    "CONSTRAINT fk_company FOREIGN KEY (company_id) " +
                    "REFERENCES COMPANY(id))";
            stmt.executeUpdate(sql);
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public void addCar(String name, int companyID) {
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "INSERT INTO Car (name, company_id) " +
                    "VALUES ('" + name + "', '" + companyID + "')";
            stmt.executeUpdate(sql);
            System.out.println("The car was added!");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public List<Car> listCars(int companyID) {
        List<Car> carList = new ArrayList<>();
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE)
        ) {
            String sql = "SELECT id, name FROM CAR " +
                    "WHERE company_id = " + companyID;
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int carID = rs.getInt(1);
                String carName = rs.getObject(2).toString();
                Car car = new Car(carName, carID);
                carList.add(car);
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
        return carList;
    }

    public Car getCar(String carName) {
        Car car = null;
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            String sql = "SELECT * " +
                    "FROM CAR " +
                    "WHERE name = '" + carName + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                int carID = rs.getInt(1);
                car = new Car(carName, carID);
            }

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return car;
    }
}
