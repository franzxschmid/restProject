package payroll;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
class EmployeeNotFoundAdvice {

  @ResponseBody // signals that this Advice is rendered straight into the Response-Body.
  @ExceptionHandler(EmployeeNotFoundException.class) //configures the advice to only respond if an Exception is thrown.
  @ResponseStatus(HttpStatus.NOT_FOUND) //says to Issue an HttpStatus.NOT_FOUND, i.e. an HTTP 404.
  String employeeNotFoundHandler(EmployeeNotFoundException ex) {
      //The body of the Advice generates the Content. In this case, it gives the Message of the Exception
      return ex.getMessage();
  }
}