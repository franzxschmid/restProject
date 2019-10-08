//EmployeeNotFoundException is an Exception used to indicate when an Employee is looked up but not found.
//When an EmployeeNotFoundException is thrown, this extra Tidbit of Spring MVC onfiguration is used to render an HTTP 404:

package payroll;

class EmployeeNotFoundException extends RuntimeException {

  EmployeeNotFoundException(Long id) {
    super("Could not find employee " + id);
  }
}
