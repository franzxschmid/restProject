package payroll;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
class VacationResourceAssembler implements ResourceAssembler<Vacation, Resource<Vacation>> {

  @Override
  public Resource<Vacation> toResource(Vacation vacation) {

    // Unconditional Links to Single-item-Resource and aggregate-Root
    Resource<Vacation> vacationResource = new Resource<>(vacation,
      linkTo(methodOn(VacationController.class).one(vacation.getId())).withSelfRel(),
      linkTo(methodOn(VacationController.class).all()).withRel("vacations")
    );

    // Conditional Links based on State of the Vacation
    if (vacation.getStatus() == Status.IN_PROGRESS) {
      vacationResource.add(
        linkTo(methodOn(OrderController.class)
          .cancel(vacation.getId())).withRel("cancel"));
      vacationResource.add(
        linkTo(methodOn(OrderController.class)
          .complete(vacation.getId())).withRel("complete"));
    }

    return vacationResource;
  }
}
