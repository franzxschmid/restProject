//adding a System that fulfills Orders

package payroll;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "CUSTOMER_ORDER") //changing the table’s 
                                //name to CUSTOMER_ORDER because ORDER is not a valid name for table
class Order {

  private @Id @GeneratedValue Long id;

  private String description;
  private Status status;

  Order() {}

  Order(String description, Status status) {
    this.description = description;
    this.status = status;
  }
}
