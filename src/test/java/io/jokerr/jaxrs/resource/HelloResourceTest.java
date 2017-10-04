package io.jokerr.jaxrs.resource;

import io.jokerr.auth.Roles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by jokerr.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HelloResourceTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    public void getUserHelloWorld() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Username", "testUser");
        headers.add("x-role", Roles.ROLE_USER);

        ResponseEntity<String> response = get("/v1/hello", headers, String.class);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void getAdminHelloWorld() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Username", "testAdmin");
        headers.add("x-role", Roles.ROLE_ADMIN);

        ResponseEntity<String> response = get("/v1/hello/admin", headers, String.class);
        assertEquals(200, response.getStatusCodeValue());
    }

    public <T>ResponseEntity<T> get(String uri, HttpHeaders headers, Class<T> responseType) {
        return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), responseType);
    }
}