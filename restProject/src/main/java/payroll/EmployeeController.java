//Spring MVC REST Controller
//https://de.wikipedia.org/wiki/Model_View_Controller
//https://spring.io/guides/tutorials/rest/
//https://de.wikipedia.org/wiki/Representational_State_Transfer#HATEOAS

package payroll;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController //indicates that the Data returned by each Method will be written straight into the Response-Body 
                //instead of rendering a Template
class EmployeeController {

  private final EmployeeRepository repository;
  
  private final EmployeeResourceAssembler assembler;

  
  //An EmployeeRepository and EmployeeResourceAssembler are injected by the Constructor
  EmployeeController(EmployeeRepository repository, EmployeeResourceAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }
  
  //We have Routes for each Operation (@GetMapping, @PostMapping, @PutMapping and @DeleteMapping) 
  //corresponding to HTTP GET, POST, PUT, and DELETE calls

  //Spring MVC-Route 1: GET
  @GetMapping("/employees")
  Resources<Resource<Employee>> all() {

    List<Resource<Employee>> employees = repository.findAll()
                                                   .stream()
                                                   .map(assembler::toResource)
                                                   .collect(Collectors.toList());
    
   return new Resources<>(employees, linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
   }

  //Spring MVC-Route 2: GET (Single item)
  //Resource<T> is a generic Container from Spring HATEOAS that includes not only the Data but a Collection of Links.
  @GetMapping("/employees/{id}")
  Resource<Employee> one(@PathVariable Long id) {

    Employee employee = repository.findById(id)
                                  .orElseThrow(() -> new EmployeeNotFoundException(id));

  return assembler.toResource(employee);
  }
  
  //Spring MVC-Route 3: POST
  @PostMapping("/employees")
  ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) throws URISyntaxException {
    
    //Object is wrapped using the EmployeeResourceAssembler
    Resource<Employee> resource = assembler.toResource(repository.save(newEmployee));

  //create an HTTP 201 Created status message
  return ResponseEntity.created(new URI(resource.getId().expand().getHref()))
                       .body(resource);
  }

  //Spring MVC-Route 4: PUT 
  @PutMapping("/employees/{id}")
  ResponseEntity<?> replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) throws URISyntaxException {

    Employee updatedEmployee = repository.findById(id)
      .map(employee -> {
        employee.setName(newEmployee.getName());
        employee.setRole(newEmployee.getRole());
        employee.setDepartment(newEmployee.getDepartment());
        employee.setSalary(newEmployee.getSalary());
        return repository.save(employee);
      })
      .orElseGet(() -> {
        newEmployee.setId(id);
        return repository.save(newEmployee);
      });
    
      //Wrappe into a Resource<Employee>-Object
      Resource<Employee> resource = assembler.toResource(updatedEmployee);

      //fetch it’s "self" link via the getId() method call
      //This method yields a Link which you can turn into a Java URI
      return ResponseEntity.created(new URI(resource.getId().expand().getHref()))
                           .body(resource); // inject the Resource itself into the body()-Method
  }

  //Spring MVC-Route 5: Delete (single Item)
  @DeleteMapping("/employees/{id}")
  ResponseEntity<?> deleteEmployee(@PathVariable Long id) {

    repository.deleteById(id);
  
  //returns an HTTP 204 No-Content-Response
  return ResponseEntity.noContent().build();
}
}
