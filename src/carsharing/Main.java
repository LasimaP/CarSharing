package carsharing;

import carsharing.car.CarDaoImpl;
import carsharing.company.CompanyDaoImpl;
import carsharing.dataaccess.DatabaseConnector;

public class Main {

    public static void main(String[] args) {

        String databaseFileName = "carsharing";
        DatabaseConnector databaseConnector = new DatabaseConnector(databaseFileName);
        CompanyDaoImpl companyDao = new CompanyDaoImpl(databaseConnector);
        CarDaoImpl carDao = new CarDaoImpl(databaseConnector);

        Menu menu = new Menu(companyDao, carDao);

        menu.mainMenu();
        carDao.dropCarTable();
        companyDao.dropCompanyTable();
    }
}