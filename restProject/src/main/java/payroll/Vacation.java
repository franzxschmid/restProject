package payroll;

import java.time.LocalDate;
import lombok.Data;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "VACATION")
public class Vacation {
    
    private @Id @GeneratedValue Long id;
    
    @ManyToOne //One employee can go on vacation several times
    private Employee employee;
    private Status status;
    private LocalDate startDate;
    private LocalDate endDate;
    
    Vacation(){}
    
    Vacation(Employee employee, LocalDate startDate, LocalDate endDate){
       this.employee = employee;
       this.startDate = startDate;
       this.endDate = endDate;
    }
    
    
}
