// This Interface extends Spring Data JPA’s JpaRepository, 
// specifying the Domain-Type as Employee and the ID-Type as Long. 

package payroll;

import org.springframework.data.jpa.repository.JpaRepository;

interface EmployeeRepository extends JpaRepository<Employee, Long> {

}