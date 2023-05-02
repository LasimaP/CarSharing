package carsharing;

import carsharing.car.Car;
import carsharing.company.Company;
import carsharing.customer.Customer;

import java.util.List;
import java.util.Map;

public class Printer {

    public static void printCompanyList(List<Company> companyList) {
        for (var company : companyList) {
            System.out.println(company.getId() + ". " + company.getName());
        }
    }

    public static void printCarList(List<Car> carList, Map<Integer, String> rentedCars) {
        int index = 1;
        for (var car : carList) {
            if (!rentedCars.containsValue(car.getName())) {
                System.out.println(index + ". " + car.getName());
            }
            index++;
        }
    }

    public static void printCustomerList(List<Customer> customerList) {
        for (var customer : customerList) {
            System.out.println(customer.getId() + ". " + customer.getName());
        }
    }
}
