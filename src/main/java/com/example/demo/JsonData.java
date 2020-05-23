package com.example.demo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties
public class JsonData {

    private static final ObjectMapper mapper = new ObjectMapper();
    @JsonInclude(JsonInclude.Include.NON_NULL)
    ObjectNode data = mapper.createObjectNode();
    String time = new SimpleDateFormat("yyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date());

}
