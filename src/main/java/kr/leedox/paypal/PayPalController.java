package kr.leedox.paypal;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.leedox.entity.Order;
import kr.leedox.wordbook.OrderDTO;
import kr.leedox.wordbook.OrderRepository;
import kr.leedox.wordbook.OrderResponseDTO;
import kr.leedox.wordbook.PaypalConfig;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class PayPalController {

    private final static Logger LOGGER =  Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final ObjectMapper objectMapper;
    private final PaypalConfig paypalConfig;

    @Autowired
    OrderRepository orderRepository;
    public PayPalController(ObjectMapper objectMapper, PaypalConfig paypalConfig) {
        this.objectMapper = objectMapper;
        this.paypalConfig = paypalConfig;
    }

    @RequestMapping(value = "/club/orders", method = RequestMethod.POST)
    @CrossOrigin
    public Object createOrder(@RequestBody OrderDTO orderDTO) throws JsonProcessingException {
        String accessToken = generateAccessToken();
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        //JSON String
        //String requestJson = "{\"intent\":\"CAPTURE\",\"purchase_units\":[{\"amount\":{\"currency_code\":\"USD\",\"value\":\"10.00\"}}]}";
        String requestJson = objectMapper.writeValueAsString(orderDTO);
        System.out.println(requestJson);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
            paypalConfig.getBaseUrl() + "/v2/checkout/orders",
                HttpMethod.POST,
                entity,
                Object.class
        );

        if(response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.log(Level.INFO, "ORDER CAPTURE");
            return response.getBody();
        } else {
            LOGGER.log(Level.INFO, "FAILED CAPTURING ORDER");
            return "Unavailable to get CAPTURE ORDER, STATUS CODE " + response.getStatusCode();
        }
    }

    @RequestMapping(value = "/club/orders/{orderId}/capture", method = RequestMethod.POST)
    @CrossOrigin
    public Object capturePayment(@PathVariable("orderId") String orderId) throws IOException {
        String accessToken = generateAccessToken();
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();

        headers.set("Authorization", "Bearer " + accessToken);
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<Object> response = restTemplate.exchange(
                paypalConfig.getBaseUrl() + "/v2/checkout/orders/" + orderId + "/capture",
                HttpMethod.POST,
                entity,
                Object.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.log(Level.INFO, "ORDER CREATED");

            String resJson = objectMapper.writeValueAsString(response.getBody());
            var dto = objectMapper.readValue(resJson, OrderResponseDTO.class);
            var order = new Order();
            order.setPaypalOrderId(dto.getId());
            order.setPaypalOrderStatus(dto.getStatus().toString());
            orderRepository.save(order);

            return response.getBody();
        } else {
            LOGGER.log(Level.INFO, "FAILED CREATING ORDER");
            return "Unavailable to get CREATE AN ORDER, STATUS CODE " + response.getStatusCode();
        }
    }

    private String generateAccessToken() {
        String auth = this.getAuth(
                 "AXCCZ3DPNAIy4kpeYaxDqZNj6QigVcPtXNm2PSvhiFcKsMGUZhfeUUZtg55M3vWtLq5-oRxQ5w_tltKN",
                "EDlYNMkpFAEwspn2M6pskBUESpYl8MTStU8Np4gmq7CUjuzBmsjYwC7-MLgERNOBBzgRUuw-bQ5hm7ek");
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + auth);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        HttpEntity<?> request = new HttpEntity<>(requestBody, headers);
        requestBody.add("grant_type", "client_credentials");

        ResponseEntity<String> response = restTemplate.postForEntity(paypalConfig.getBaseUrl() + "/v1/oauth2/token", request, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            LOGGER.log(Level.INFO, "GET TOKEN: SUCCESSFUL!");
            return new JSONObject(response.getBody()).getString("access_token");
        } else {
            LOGGER.log(Level.SEVERE, "GET TOKEN: FAILED!");
            return "Unavailable to get ACCESS TOKEN, STATUS CODE " + response.getStatusCode();
        }
    }

    private String getAuth(String client_id, String app_secret) {
        String auth = client_id + ":" + app_secret;
        return Base64.getEncoder().encodeToString(auth.getBytes());
    }
}
