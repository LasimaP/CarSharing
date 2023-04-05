package carsharing;

import carsharing.company.CompanyDao;
import carsharing.company.CompanyDaoImpl;
import carsharing.dataaccess.DatabaseConnector;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static String databaseName;
    private static DatabaseConnector databaseConnector;
    private static CompanyDao companyDao;

    public static void main(String[] args) {

        databaseName = "carsharing";
        databaseConnector = new DatabaseConnector(databaseName);
        companyDao = new CompanyDaoImpl(databaseConnector);

        companyDao.dropCompanyTable();
        companyDao.createCompanyTable();
        mainMenu();

    }

    private static void mainMenu() {
        System.out.println("1. Log in as a Manager\n" +
                "0. Exit");

        int input;
        do {
            input = scanner.nextInt();
            switch (input) {
                case 1 -> managerMenu();
                case 0 -> databaseConnector.closeConnection();
                default -> System.out.println("Invalid input");
            }
        } while (input != 0);

    }

    private static void managerMenu() {
        System.out.println("""
                1. Company list
                2. Create a company
                0. Back""");

        int input;
        do {
            input = scanner.nextInt();
            switch (input) {
                case 1 -> companyDao.listCompanies();
                case 2 -> createCompany();
                case 0 -> mainMenu();
                default -> System.out.println("Invalid input");
            }
        } while (input != 0);
    }

    private static void createCompany() {
        scanner.nextLine();
        System.out.println("Enter the company name: ");
        String companyName = scanner.nextLine();
        companyDao.addCompany(companyName);
    }
}