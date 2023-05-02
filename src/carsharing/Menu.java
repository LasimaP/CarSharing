package carsharing;

import carsharing.car.Car;
import carsharing.car.CarDaoImpl;
import carsharing.company.Company;
import carsharing.company.CompanyDaoImpl;
import carsharing.customer.Customer;
import carsharing.customer.CustomerDaoImpl;

import java.util.*;

public class Menu {
    private static final Scanner scanner = new Scanner(System.in);
    private int input;
    private final CompanyDaoImpl companyDao;
    private final CarDaoImpl carDao;
    private final CustomerDaoImpl customerDao;

    private static Map<Integer, String> rentedCars;


    public Menu(CompanyDaoImpl companyDao, CarDaoImpl carDao, CustomerDaoImpl customerDao) {
        this.companyDao = companyDao;
        this.carDao = carDao;
        this.customerDao = customerDao;
        rentedCars = new HashMap<>();
    }

    public void mainMenu() {
        System.out.println("""
                1. Log in as a manager
                2. Log in as a customer
                3. Create a customer
                0. Exit""");

        input = scanner.nextInt();

        if (input == 1) {
            managerMenu();
        } else if (input == 2) {
            customerSelectionMenu();
        } else if (input == 3) {
            createCustomer();
            mainMenu();
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

        input = scanner.nextInt();

        switch (input) {
            case 1 -> companySelectionMenu();
            case 2 -> {
                createCompany();
                managerMenu();
            }
            case 0 -> mainMenu();
            default -> {
                System.out.println("Invalid input");
                managerMenu();
            }
        }
    }

    private void companySelectionMenu() {
        List<Company> companyList = companyDao.listCompanies();
        if (companyList.isEmpty()) {
            System.out.println("The company list is empty!");
            managerMenu();
        } else {
            System.out.println("Choose the company:");
            Printer.printCompanyList(companyList);
            System.out.println("0. Back");

            input = scanner.nextInt();
            if (input == 0) {
                managerMenu();
            } else {
                Company company = companyDao.getCompany(input);
                if (Objects.equals(company, null)) {
                    System.out.println("Invalid input");
                    companySelectionMenu();
                } else {
                    System.out.println("'" + company.getName() + "' company");
                    companyMenu(input);
                }
            }
        }
    }

    private void customerSelectionMenu() {
        List<Customer> customerList = customerDao.listCustomers();
        if (customerList.isEmpty()) {
            System.out.println("The customer list is empty!");
            mainMenu();
        } else {
            System.out.println("Customer List:");
            Printer.printCustomerList(customerList);
            System.out.println("0. Back");

            input = scanner.nextInt();
            if (input == 0) {
                mainMenu();
            } else {
                Customer customer = customerDao.getCustomer(input);
                if (Objects.equals(customer, null)) {
                    System.out.println("Invalid input");
                    customerSelectionMenu();
                } else {
                    customerMenu(input);
                }
            }
        }
    }

    private void companyMenu(int companyID) {
        System.out.println("""
                1. Car list
                2. Create a car
                0. Back""");

        input = scanner.nextInt();
        switch (input) {
            case 1 -> {
                listCars(companyID);
                companyMenu(companyID);
            }
            case 2 -> {
                createCar(companyID);
                companyMenu(companyID);
            }
            case 0 -> managerMenu();
            default -> {
                System.out.println("Invalid input");
                companyMenu(companyID);
            }
        }
    }

    private void customerMenu(int customerID) {
        System.out.println("""
                1. Rent a car
                2. Return a rented car
                3. My rented car
                0. Back""");

        input = scanner.nextInt();
        switch (input) {
            case 1 -> {
                if (customerDao.getRentalCarID(customerID) != 0) {
                    System.out.println("You've already rented a car!");
                    customerMenu(customerID);
                } else {
                    rentACarMenu(customerID);
                }
            }
            case 2 -> {
                if (customerDao.getRentalCarID(customerID) != 0) {
                    customerDao.returnCar(customerID);
                    rentedCars.remove(customerID);
                    System.out.println("You've returned a rented car!");
                } else {
                    System.out.println("You didn't rent a car!");
                }
                customerMenu(customerID);
            }
            case 3 -> {
                if (customerDao.getRentalCarID(customerID) != 0) {
                    String[] rentedCarDetails = customerDao.viewRentedDetails(customerID);
                    System.out.println("Your rented car:");
                    System.out.println(rentedCarDetails[0]);
                    System.out.println("Company:");
                    System.out.println(rentedCarDetails[1]);

                } else {
                    System.out.println("You didn't rent a car!");
                }
                customerMenu(customerID);
            }
            case 0 -> mainMenu();
            default -> {
                System.out.println("Invalid input");
                customerMenu(customerID);
            }
        }
    }

    private void rentACarMenu(int customerID) {
        System.out.println("Choose a company:");
        Printer.printCompanyList(companyDao.listCompanies());
        System.out.println("0. Back");

        input = scanner.nextInt();
        if (input != 0) {
            Company company = companyDao.getCompany(input);
            System.out.println("Choose a car:");
            List<Car> carList = carDao.listCars(input);
            Printer.printCarList(carList, rentedCars);
            System.out.println("0. Back");

            input = scanner.nextInt();
            if (input == 0) {
                rentACarMenu(customerID);
            } else {
                Car car = carList.get(input - 1);
                if (Objects.equals(car, null)) {
                    System.out.println("Invalid input");
                } else {
                    if (customerDao.rentCar(customerID, car.getId())) {
                        rentedCars.put(customerID, car.getName());
                        System.out.println("You rented '" + car.getName() + "'");

                    } else {
                        System.out.println("No available cars in the" + company.getName() + "company");
                        rentACarMenu(customerID);
                    }
                }
            }
        }
        customerMenu(customerID);
    }

    private void createCompany() {
        scanner.nextLine();
        System.out.println("Enter the company name: ");
        String companyName = scanner.nextLine();
        companyDao.addCompany(companyName);
    }

    private void createCar(int companyID) {
        scanner.nextLine();
        System.out.println("Enter the car name: ");
        String carName = scanner.nextLine();
        carDao.addCar(carName, companyID);
    }

    private void createCustomer() {
        scanner.nextLine();
        System.out.println("Enter the customer name: ");
        String customerName = scanner.nextLine();
        customerDao.addCustomer(customerName);
    }

    private void listCars(int companyID) {
        List<Car> carList = carDao.listCars(companyID);
        if (carList.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            System.out.println("Car list:");
            Printer.printCarList(carList, rentedCars);
        }
    }
}