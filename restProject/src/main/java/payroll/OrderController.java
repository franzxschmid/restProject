package payroll;


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

@RestController
class OrderController {

  private final OrderRepository orderRepository;
  private final OrderResourceAssembler assembler;

  //Injects an OrderRepository and OrderResourceAssembler via Constructor
  OrderController(OrderRepository orderRepository, OrderResourceAssembler assembler) {
    this.orderRepository = orderRepository;
    this.assembler = assembler;
  }

  // Aggregate root
  //All the Controller-Methods return one of Spring HATEOAS’s ResourceSupport Subclasses 
  //to properly render Hypermedia (or a Wrapper around such a Type).
  
  //Spring MVC-Route 1: Get(aggregate Root)
  @GetMapping("/orders")
  Resources<Resource<Order>> all() {

    List<Resource<Order>> orders = orderRepository.findAll()
                                                  .stream()
                                                  .map(assembler::toResource)
                                                  .collect(Collectors.toList());
    
  return new Resources<>(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel());
  }

  //Spring MVC-Route 2: Get(Single Item-Order-Resource-Request)
  @GetMapping("/orders/{id}")
  Resource<Order> one(@PathVariable Long id) {
  
      return assembler.toResource(orderRepository.findById(id)
                                                 .orElseThrow(() -> new OrderNotFoundException(id)));
  }

  //Spring MVC-Route 3: Post: handles creating new Orders, by starting them in the IN_PROGRESS-State
  @PostMapping("/orders")
  ResponseEntity<Resource<Order>> newOrder(@RequestBody Order order) {

    order.setStatus(Status.IN_PROGRESS);
    Order newOrder = orderRepository.save(order);

  return ResponseEntity
      .created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
      .body(assembler.toResource(newOrder));
  }
  
  //Spring MVC-Route 4: Delete
  //Delete-Method checks the Order-Status before allowing it to be cancelled. 
  @DeleteMapping("/orders/{id}/cancel")
  ResponseEntity<ResourceSupport> cancel(@PathVariable Long id) {

    Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

    if (order.getStatus() == Status.IN_PROGRESS) {
      order.setStatus(Status.CANCELLED);
      return ResponseEntity.ok(assembler.toResource(orderRepository.save(order)));
    }

  return ResponseEntity
    .status(HttpStatus.METHOD_NOT_ALLOWED)
    .body(new VndErrors.VndError("Method not allowed", "You can't cancel an order that is in the " + order.getStatus() + " status"));
  } 
  
  //Spring MVC-Route 5: Put
  //Complete-Method
  @PutMapping("/orders/{id}/complete")
  ResponseEntity<ResourceSupport> complete(@PathVariable Long id) {

    Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

    if (order.getStatus() == Status.IN_PROGRESS) {
      order.setStatus(Status.COMPLETED);
      return ResponseEntity.ok(assembler.toResource(orderRepository.save(order)));
    }

  return ResponseEntity
      .status(HttpStatus.METHOD_NOT_ALLOWED)
      .body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + order.getStatus() + " status"));
}
  
}