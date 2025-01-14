package com.mmk.rbac.jpautils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.mmk.rbac.jpa.entity.RoleEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO:看起来不需要这个反序列化器？
 * @author: 大漠穷秋
 */
public class RoleListDeserializer extends StdDeserializer<List<RoleEntity>> {
    public RoleListDeserializer() {
        this(null);
    }

    protected RoleListDeserializer(Class<List<RoleEntity>> vc) {
        super(vc);
    }

    @Override
    public List<RoleEntity> deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
        System.out.println("==========================");
        System.out.println("RoleListDeserializer..................");
        System.out.println("==========================");
        return new ArrayList<>();
    }
}
