package com.zitlab.palmyra.core.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.biocliq.fluwiz.server.api.ApiConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zitlab.palmyra.cinch.pojo.Tuple;
import com.zitlab.palmyra.util.ErrorHelper;

@AutoConfigureMockMvc	
@SpringBootTest(classes = ApiConfiguration.class, properties = "spring.main.allow-bean-definition-overriding=true")
@ExtendWith(SpringExtension.class)
public class SampleAPITest {
	
	private final static String TEST_USER_ID = "user-id-123";
	@Autowired
	private MappingJackson2HttpMessageConverter springMvcJacksonConverter;
		
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetTypes() throws Exception {        
    	MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/dev/v2/myapp/type/Student");
    	
        MvcResult result = mockMvc.perform(
        		requestBuilder
                .with(user(TEST_USER_ID))
                .with(csrf())
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultCZ = result.getResponse().getContentAsString();        
        assertNotNull(resultCZ);        
        System.out.println(resultCZ);        
    }
    
    @Test
    public void testGetAllTypes() throws Exception {        
    	MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/dev/v2/myapp/type");
    	
        MvcResult result = mockMvc.perform(
        		requestBuilder
                .with(user(TEST_USER_ID))
                .with(csrf())
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultCZ = result.getResponse().getContentAsString();        
        assertNotNull(resultCZ);        
        System.out.println(resultCZ);
        List<Tuple> types = deserialize(resultCZ, new TypeReference<ArrayList<Tuple>>() {
		});
        System.out.println(types.get(0).get("table"));
        Assertions.assertTrue(types.size() > 0);
    }

	private  ArrayList<Tuple> deserialize(String data, TypeReference<ArrayList<Tuple>> typeReference) {
		ObjectMapper mapper = springMvcJacksonConverter.getObjectMapper();
		try {
			return  mapper.readValue(data, typeReference);
		} catch (JsonProcessingException e) {
			ErrorHelper.rethrow(e);
		}	
		return null;
	}

}
