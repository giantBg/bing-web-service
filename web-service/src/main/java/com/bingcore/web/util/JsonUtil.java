package com.bingcore.web.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by xubing on 16/5/11.
 *
 * @Author xubing
 * @DateTime 2016-05-11 16:14
 * @Function 统计服务jsonApi
 */
public class JsonUtil {

    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    /**
     * 默认不排除任何属性
     */
    public static final JsonUtil DEFAULT = new JsonUtil();

    private static ObjectMapper mapper;

    private JsonUtil() {
        mapper = new ObjectMapper();
        // ignore attributes exists in json string, but not in java object when deserialization
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.registerModule(new GuavaModule());
    }

    /**
     * get default itemStyle of AreaChart
     */
    public JsonNode getAreaItemStyle() {
        try {
            //Create the root node
            ObjectNode root = mapper.createObjectNode();
            //Create a child node
            ObjectNode child = mapper.createObjectNode();
            //Create a leaf node
            ObjectNode leaf = mapper.createObjectNode();
            leaf.put("show", true);
            child.set("label", leaf);
            root.set("normal", child);
            root.set("emphasis", child);
            return root;
        } catch (Exception e) {
            logger.error("write to json node error:", e);
            return null;
        }
    }

    /**
     * convert an object(POJO, Collection, ...) to json string
     *
     * @param target target object
     * @return json string
     */
    public String toJson(Object target) {

        try {
            return mapper.writeValueAsString(target);
        } catch (IOException e) {
            logger.error("write to json string error:" + target, e);
            return null;
        }
    }

    /**
     * deserialize a json to target class object
     *
     * @param json   json string
     * @param target target class
     * @param <T>
     * @return target object
     */
    public <T> T fromJson(String json, Class<T> target) {
        if (Strings.isNullOrEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, target);
        } catch (IOException e) {
            logger.warn("parse json string error:" + json, e);
            return null;
        }
    }

    public static void main(String args[]) {
        System.out.println(JsonUtil.DEFAULT.toJson(JsonUtil.DEFAULT.getAreaItemStyle()));
    }

}
