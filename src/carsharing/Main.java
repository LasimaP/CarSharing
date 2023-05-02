package carsharing;

import carsharing.car.CarDaoImpl;
import carsharing.company.CompanyDaoImpl;
import carsharing.customer.CustomerDaoImpl;
import carsharing.dataaccess.DatabaseConnector;

public class Main {

    public static void main(String[] args) {

        String databaseFileName = "carsharing";
        DatabaseConnector databaseConnector = new DatabaseConnector(databaseFileName);
        CompanyDaoImpl companyDao = new CompanyDaoImpl(databaseConnector);
        CarDaoImpl carDao = new CarDaoImpl(databaseConnector);
        CustomerDaoImpl customerDao = new CustomerDaoImpl(databaseConnector);

        Menu menu = new Menu(companyDao, carDao, customerDao);

        menu.mainMenu();

        // Commands To Clear Data
//        customerDao.dropCustomerTable();
//        carDao.dropCarTable();
//        companyDao.dropCompanyTable();
    }
}