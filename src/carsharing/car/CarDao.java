package carsharing.car;

public interface CarDao {
    void dropCarTable();
    void createCarTable();
    void addCar(String name, int id);
    void listCars(int id);
}
