//Employee handles both "old" (with name) and "new"(with last and first Name) Clients

package payroll;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data   //Lombok-Annotation to create the getters, setters, equals, hash, and toString methods, based on the fields.
@Entity //JPA-Annotation to make this object ready for storage in a JPA-based Datastore (Java Persistence API)
class Employee {

  //Primary-Key and automatically populated by the JPA-Provider
  private @Id @GeneratedValue Long id;
  
  //Other Attributes
  private String firstName;
  private String lastName;
  private String role;
  private String department;
  private int salary;

  Employee() {}
  
  Employee(String firstName, String lastName, String role, String department, int salary) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.role = role; 
    this.department = department;
    this.salary = salary; 
  }
  
   //"virtual" Getter for the old name-Property
   public String getName() {
      return this.firstName + " " + this.lastName;
  }
   //virtual" Setter for the old name-Property
   public void setName(String name) {
      String[] parts =name.split(" ");
      this.firstName = parts[0];
      this.lastName = parts[1];
  }
}
