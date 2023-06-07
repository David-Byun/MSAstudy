package msa.ar.customer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;
    public void registerCustomer(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail()).build();
        //to do : check if email valid
        //to do : check if email not taken
        customerRepository.saveAndFlush(customer);
        //to do : check if fraudster
        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(
                "http://localhost:8091/api/v1/fraud-check/{customerId}",
                //entity lifecycle 확인 필요
                FraudCheckResponse.class,
                customer.getId()
                //위가 return Type
        );

        if (fraudCheckResponse.getIsFraudster()) {
            throw new IllegalStateException("fraudster");
        }
        //to do : send notification
    }
}
