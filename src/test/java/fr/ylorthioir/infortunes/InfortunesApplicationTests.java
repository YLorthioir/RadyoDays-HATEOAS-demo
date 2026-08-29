package fr.ylorthioir.infortunes;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InfortunesApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void apiIsNavigableThroughHateoasLinks() throws Exception {
        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cercle info"))
                .andExpect(jsonPath("$._links.beers.href").value("http://localhost/api/beer"));

        mockMvc.perform(get("/api/beer"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.beerModelList").isArray())
                .andExpect(jsonPath("$._embedded.beerModelList[0]._links.rename").doesNotExist())
                .andExpect(jsonPath("$._embedded.beerModelList[0]._links.self.href").value("http://localhost/api/beer/1"))
                .andExpect(jsonPath("$._links.bar.href").value("http://localhost/api"));
    }

    @Test
    void orderingBeerDecreasesItsStock() throws Exception {
        mockMvc.perform(post("/api/beer/1/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stock").value(11))
                .andExpect(jsonPath("$._links.order.href").value("http://localhost/api/beer/1/order"));
    }

    @Test
    void beerExposesUpdateLinkAndCanBeUpdated() throws Exception {
        mockMvc.perform(get("/api/beer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._links.rename.href").value("http://localhost/api/beer/1/name"))
                .andExpect(jsonPath("$._links.change-price.href").value("http://localhost/api/beer/1/price"));

        mockMvc.perform(patch("/api/beer/1/name")
                        .contentType("application/json")
                        .content("{\"name\":\"Orval Renommee\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Orval Renommee"));

        mockMvc.perform(patch("/api/beer/1/price")
                        .contentType("application/json")
                        .content("{\"price\":\"7.00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(7.00));
    }

    @Test
    void notFoundResponseContainsNavigationLinks() throws Exception {
        mockMvc.perform(get("/api/beer/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$._links.root.href").value("http://localhost/api"))
                .andExpect(jsonPath("$._links.beers.href").value("http://localhost/api/beer"));
    }

}
