package carsharing.customer;

import java.util.List;

public interface CustomerDao {
    void dropCustomerTable();
    void createCustomerTable();
    void addCustomer(String customerName);
    List<Customer> listCustomers();
}
