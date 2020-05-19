package com.example.demo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class Converter {
    private Converter(){

    }
    private static final ObjectMapper mapper  = new ObjectMapper();
    public static ObjectNode toNode(Object obj){
        return mapper.convertValue(obj,ObjectNode.class);
    }
}
