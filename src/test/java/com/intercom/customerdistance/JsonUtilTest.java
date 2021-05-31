package com.intercom.customerdistance;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonUtilTest {

    private static String INVALID_JSON = "invalid";
    private static String VALID_JSON = "{\"name\": \"sample\", \"value\": \"sample\"}";


    @Test
    void createJsonObject_shouldThrowJSONException() {
        try {
            JsonUtil.createJsonObject(INVALID_JSON);
            fail();
        } catch (JSONException e) {
            assertEquals("A JSONObject text must begin with '{' at 1 [character 2 line 1]", e.getMessage());
        }
    }

    @Test
    void createJsonObject_shouldCreateJSONObjectFromString() {
        JSONObject expectedJsonObject = new JSONObject(VALID_JSON);

        assertEquals(expectedJsonObject.get("name"), JsonUtil.createJsonObject(VALID_JSON).get("name"));
        assertEquals(expectedJsonObject.get("value"), JsonUtil.createJsonObject(VALID_JSON).get("value"));
    }
}