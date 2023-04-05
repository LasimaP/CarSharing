package carsharing;

import carsharing.car.CarDaoImpl;
import carsharing.company.CompanyDaoImpl;

import java.util.Objects;
import java.util.Scanner;

public class Menu {
    private final CompanyDaoImpl companyDao;
    private final CarDaoImpl carDao;
    private static final Scanner scanner = new Scanner(System.in);

    public Menu(CompanyDaoImpl companyDao, CarDaoImpl carDao) {
        this.companyDao = companyDao;
        this.carDao = carDao;
    }

    public void mainMenu() {
        System.out.println("1. Log in as a manager\n" +
                "0. Exit");

        int input = scanner.nextInt();

        if (input == 1) {
            managerMenu();
        } else if (input == 0) {
            companyDao.closeConnection();
        } else {
            System.out.println("Invalid input");
            mainMenu();
        }
    }

    private void managerMenu() {
        System.out.println("""
                1. Company list
                2. Create a company
                0. Back""");

        int input = scanner.nextInt();

        if (input == 1) {
            companySelectionMenu();
        } else if (input == 2) {
            createCompany();
            managerMenu();
        } else if (input == 0) {
            mainMenu();
        } else {
            System.out.println("Invalid input");
            managerMenu();
        }
    }

    private void companySelectionMenu() {
        if(companyDao.listCompanies()) {
            System.out.println("0. Back");
            int input = scanner.nextInt();
            if (input == 0) {
                managerMenu();
            } else {
                String companyName = companyDao.getCompany(input);
                if (Objects.equals(companyName, "null")) {
                    System.out.println("Invalid input");
                    companySelectionMenu();
                } else {
                    System.out.println("'" + companyDao.getCompany(input) + "' company");
                    companyMenu(input);
                }
            }
        }
        managerMenu();
    }

    private void companyMenu(int id) {
        System.out.println("""
                1. Car list
                2. Create a car
                0. Back""");

        int input = scanner.nextInt();
        if (input == 1) {
            carDao.listCars(id);
            companyMenu(id);
        } else if (input == 2) {
            createCar(id);
            companyMenu(id);
        } else if (input == 0) {
            managerMenu();
        } else {
            System.out.println("Invalid input");
            companyMenu(id);
        }
    }

    private void createCompany() {
        scanner.nextLine();
        System.out.println("Enter the company name: ");
        String companyName = scanner.nextLine();
        companyDao.addCompany(companyName);
    }

    private void createCar(int id) {
        scanner.nextLine();
        System.out.println("Enter the car name: ");
        String carName = scanner.nextLine();
        carDao.addCar(carName, id);
    }
}
