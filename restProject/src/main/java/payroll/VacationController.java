package payroll;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

//TODO: make it possible that "Vacations" must be approved before (Update MVC Route 5 PUT)
@RestController             
class VacationController {

  private final VacationRepository vacationRepository;
  
  private final VacationResourceAssembler assembler;

  
  //An VacationRepository and VacationResourceAssembler are injected by the Constructor
  VacationController(VacationRepository vacationRepository, VacationResourceAssembler assembler) {
    this.vacationRepository = vacationRepository;
    this.assembler = assembler;
  }
  
  //Spring MVC-Route 1: GET (all)
  @GetMapping("/vacations")
  Resources<Resource<Vacation>> all() {

    List<Resource<Vacation>> vacations = vacationRepository.findAll().stream()
                                                           .map(assembler::toResource)
                                                           .collect(Collectors.toList());
    
  return new Resources<>(vacations, linkTo(methodOn(VacationController.class).all()).withSelfRel());
  }

  //Spring MVC-Route 2: GET (single Item)
  @GetMapping("/vacations/{id}")
  Resource<Vacation> one(@PathVariable Long id) {

    Vacation vacation = vacationRepository.findById(id)
                                          .orElseThrow(() -> new EmployeeNotFoundException(id));
    
  return assembler.toResource(vacation);
  }
  
  //Spring MVC-Route 3: POST
  @PostMapping("/vacations")
  ResponseEntity<Resource<Vacation>> newVacation(@RequestBody Vacation vacation) {

    vacation.setStatus(Status.IN_PROGRESS);
    Vacation newVacation = vacationRepository.save(vacation);

  return ResponseEntity.created(linkTo(methodOn(OrderController.class).one(newVacation.getId())).toUri())
                       .body(assembler.toResource(newVacation));
  }

  //Spring MVC-Route 4: Delete
  @DeleteMapping("/vacations/{id}/cancel")
  ResponseEntity<ResourceSupport> cancel(@PathVariable Long id) {

    Vacation vacation = vacationRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

    if (vacation.getStatus() == Status.IN_PROGRESS) {
      vacation.setStatus(Status.CANCELLED);
      return ResponseEntity.ok(assembler.toResource(vacationRepository.save(vacation)));
    }

  return ResponseEntity
    .status(HttpStatus.METHOD_NOT_ALLOWED)
    .body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is in the " + vacation.getStatus() + " status"));
  } 
  
  //Spring MVC-Route 5: Put
  //Complete-Method
  @PutMapping("/orders/{id}/complete")
  ResponseEntity<ResourceSupport> complete(@PathVariable Long id) {

    Vacation vacation = vacationRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

    if (vacation.getStatus() == Status.IN_PROGRESS) {
      vacation.setStatus(Status.COMPLETED);
      return ResponseEntity.ok(assembler.toResource(vacationRepository.save(vacation)));
    }

  return ResponseEntity
      .status(HttpStatus.METHOD_NOT_ALLOWED)
      .body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + vacation.getStatus() + " status"));
}
}
