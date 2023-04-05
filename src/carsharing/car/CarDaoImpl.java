package carsharing.car;

import carsharing.dataaccess.DatabaseConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
    public void addCar(String name, int foreignKey) {
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement()
        ) {
            Car car = new Car(name);
            String sql = "INSERT INTO Car (name, company_id) " +
                    "VALUES ('" + car.getName() + "', '" + foreignKey + "')";
            stmt.executeUpdate(sql);
            System.out.println("The car was added!");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

    @Override
    public void listCars(int foreignKey) {
        try (
                Connection conn = this.databaseConnector.getConnection();
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE)
        ) {
            String sql = "SELECT id, name FROM CAR " +
                    "WHERE company_id = " + foreignKey;
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                System.out.println("Car list:");
                rs.previous();
            } else {
                System.out.println("The car list is empty!");
            }
            int count = 1;
            while (rs.next()) {
                String carName = rs.getObject(2).toString();
                System.out.println(count + ". " + carName);
                count++;
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}
