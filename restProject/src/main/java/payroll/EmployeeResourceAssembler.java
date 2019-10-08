//Simplifying Link Creation

package payroll;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component //component will be automatically created when the App starts
class EmployeeResourceAssembler implements ResourceAssembler<Employee, Resource<Employee>> {

  //converts a non-resource Object (Employee) into a resource-based Object (Resource<Employee>)
  @Override
  public Resource<Employee> toResource(Employee employee) {

    return new Resource<>(employee,
      linkTo(methodOn(EmployeeController.class).one(employee.getId())).withSelfRel(),
      linkTo(methodOn(EmployeeController.class).all()).withRel("employees"));
  }
}
