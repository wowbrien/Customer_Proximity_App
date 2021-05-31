package com.intercom.customerdistance;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtil {

    public static JSONObject createJsonObject(String jsonString) {
        try {
            return new JSONObject(jsonString);
        } catch (JSONException exception) {
//            log ("Customer entry is invalid JSON format.");
            exception.printStackTrace();
            throw exception;
        }
    }
}
