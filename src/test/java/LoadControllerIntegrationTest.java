import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = org.cryptoklineproject.Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.yml")
public class LoadControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @SneakyThrows
    @Test
    public void testLoadData(){
        mvc.perform(MockMvcRequestBuilders
                .post("/load/data")
                .param("symbol", "BTCUSDT")
                .param("startTime", "1697068382000")
                .param("endTime", "1697078392000")
                .param("interval", "1"))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
