package carsharing.car;

import java.util.List;

public interface CarDao {
    void dropCarTable();
    void createCarTable();
    void addCar(String carName, int carID);
    List<Car> listCars(int companyID);
}
