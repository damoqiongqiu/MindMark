package com.mmk.rbac.jpautils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.mmk.rbac.jpa.entity.RoleEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 由于 RoleEntity 与多个实体之间存在多对多的关系，所以在序列化时，需要自定义序列化器，否则 Jackson 会因为循环引用而抛出异常。
 * 本序列化器将 RoleEntity 序列化为一个 Map 对象，包含 roleId、roleName、status、remark 四个属性，而不包含与其他实体的关联关系。
 * @author 大漠穷秋
 */
public class RoleListSerializer extends StdSerializer<List<RoleEntity>> {
    public RoleListSerializer() {
        this(null);
    }

    protected RoleListSerializer(Class<List<RoleEntity>> t) {
        super(t);
    }

    @Override
    public void serialize(List<RoleEntity> roleEntities, JsonGenerator generator, SerializerProvider provider) throws IOException {
        List<Map> list = new ArrayList<>();
        for (RoleEntity roleEntity : roleEntities) {
            HashMap obj=new HashMap();
            obj.put("roleId",roleEntity.getRoleId());
            obj.put("roleName",roleEntity.getRoleName());
            obj.put("status",roleEntity.getStatus());
            obj.put("remark",roleEntity.getRemark());
            list.add(obj);
        }
        generator.writeObject(list);
    }
}
