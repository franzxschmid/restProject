//Main
//https://spring.io/guides/tutorials/rest/

/*
- Don’t remove old Fields. Instead, support them.
- Use rel-based Links so Clients don’t have to hard code URIs.
- Retain old Links as long as possible. Even if you have to change the URI, keep the Rels so older Clients 
  have a Path onto the newer Features.
- Use Links, not payload Data, to instruct Clients when various state-driving-Operations are available.
*/

package payroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//Will fire up a Servlet-Container and serve up our Service
@SpringBootApplication
public class PayrollApplication {

  public static void main(String... args) {
    SpringApplication.run(PayrollApplication.class, args);
  }
}
