package de.othr.sw.bank.service;

import de.othr.sw.bank.entity.Customer;
import de.othr.sw.bank.entity.Employee;
import de.othr.sw.bank.entity.EmployeeDesignation;
import de.othr.sw.bank.repo.EmployeeRepositoryIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("employeeUserDetailsService")
public class EmployeeService implements EmployeeServiceIF, UserDetailsService {

    @Autowired
    private EmployeeRepositoryIF employeeRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getEmployeeByUsername(username)
                .orElseThrow(() -> {
                            throw new UsernameNotFoundException("Customer with name " + username + " not found.");
                        }
                );
    }

    @Override
    @Transactional
    public Optional<Employee> getEmployeeByUsername(String username) {
        return employeeRepository.findDistinctByUsername(username);
    }

    @Override
    @Transactional
    public Employee getEmployeeForCustomerSupport() {
        List<Employee> employees = employeeRepository.findEmployeeByDesignation(EmployeeDesignation.ACCOUNT_MANAGER.getDesignation());
        Optional<Employee> employee = employees.stream().min(Comparator.comparingInt(x -> x.getActiveCustomers().size()));
        return employee.isPresent() ? employee.get() : null;
    }

    @Override
    @Transactional
    public void removeCustomerFromEmployee(Customer customer) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(customer.getAttendant().getId());
        optionalEmployee.ifPresent(e -> {
                    e.removeCustomer(customer);
                    employeeRepository.save(e);
                }
        );
    }
}
