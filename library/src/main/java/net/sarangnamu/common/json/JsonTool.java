/*
 * JsonTool.java
 * Copyright 2013 Burke Choi All rights reserved.
 *             http://www.sarangnamu.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sarangnamu.common.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);
// mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);

/**
 * <pre>
 * {@code
    public class Notice {
        @JsonProperty
        public int no;

        @JsonProperty
        public String date;

        @JsonProperty
        public String content;
    }

    - type1
    (ArrayList<Notice>) JsonTool.toObj(res, new TypeReference<List<Notice>>(){});

    - type2
    (Notice) JsonTool.toObj(res, Notice.class);
 * }
 * </pre>
 * @author <a href="mailto:aucd29@gmail.com">Burke Choi</a>
 */
public class JsonTool {
    private static final Logger mLog = LoggerFactory.getLogger(JsonTool.class);
    private static ObjectMapper mMapper;

    private static void init() {
        if (mMapper == null) {
            mMapper = new ObjectMapper();

            mMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
            mMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            mMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        }
    }

    public static ObjectMapper getMapper() {
        init();
        return mMapper;
    }

    public static String marshalling(Object obj) throws JsonProcessingException {
        return getMapper().writeValueAsString(obj);
    }

    public static <T> T unmarshalling(String json, Class<?> clazz) throws IOException, NullPointerException, JsonParseException, JsonMappingException {
        if (json == null) {
            throw new NullPointerException();
        }

        return (T) getMapper().readValue(json, clazz);
    }

    public static <T> T unmarshalling(File fp, Class<?> clazz) throws IOException, NullPointerException, JsonParseException, JsonMappingException {
        if (fp == null) {
            throw new NullPointerException();
        }

        if (!fp.exists()) {
            throw new FileNotFoundException();
        }

        BufferedReader reader = new BufferedReader(new FileReader(fp));
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        return (T) getMapper().readValue(sb.toString(), clazz);
    }
}
