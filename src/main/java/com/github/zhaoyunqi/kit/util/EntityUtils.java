package com.github.zhaoyunqi.kit.util;

import com.github.ag.core.context.BaseContextHandler;
import com.github.zhaoyunqi.kit.audit.AceAudit;
import com.github.zhaoyunqi.kit.audit.CrtTime;
import com.github.zhaoyunqi.kit.audit.CrtUserId;
import com.github.zhaoyunqi.kit.audit.CrtUserName;
import com.github.zhaoyunqi.kit.audit.ModifiedTime;
import com.github.zhaoyunqi.kit.audit.ModifiedUserId;
import com.github.zhaoyunqi.kit.audit.ModifiedUserName;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.Date;


@Slf4j
public class EntityUtils {

    public static <T> void setCreatAndUpdatInfo(T entity) {
        setCreateInfo(entity);
        setUpdatedInfo(entity);
    }

    public static <T> void setCreateInfo(T entity) {
        String userName = BaseContextHandler.getName();
        String userId = BaseContextHandler.getUserID();
        String departId = BaseContextHandler.getDepartID();
        String tenantId = BaseContextHandler.getTenantID();
        // 默认属性
        String[] fieldNames = {"crtUserName", "crtUserId", "crtTime", "departId", "tenantId"};
        if (entity.getClass().getAnnotation(AceAudit.class) != null) {
            Field[] fields = entity.getClass().getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    if (field.getAnnotation(CrtUserName.class) != null) {
                        fieldNames[0] = field.getName();
                        continue;
                    }
                    if (field.getAnnotation(CrtUserId.class) != null) {
                        fieldNames[1] = field.getName();
                        continue;
                    }
                    if (field.getAnnotation(CrtTime.class) != null) {
                        fieldNames[2] = field.getName();
                        continue;
                    }
                }
            }
        }
        Field field = ReflectionUtils.getAccessibleField(entity, "crtTime");
        // 默认值
        Object[] value = null;
        if (field != null && field.getType().equals(Date.class)) {
            value = new Object[]{userName, userId, new Date(), departId, tenantId};
        }
        // 填充默认属性值
        setDefaultValues(entity, fieldNames, value);
    }


    public static <T> void setUpdatedInfo(T entity) {
        String userName = BaseContextHandler.getName();
        String userId = BaseContextHandler.getUserID();
        // 默认属性
        String[] fieldNames = {"updUserName", "updUserId", "updTime"};
        if (entity.getClass().getAnnotation(AceAudit.class) != null) {
            Field[] fields = entity.getClass().getDeclaredFields();
            if (fields != null) {
                for (Field field : fields) {
                    if (field.getAnnotation(ModifiedUserName.class) != null) {
                        fieldNames[0] = field.getName();
                        continue;
                    }
                    if (field.getAnnotation(ModifiedUserId.class) != null) {
                        fieldNames[1] = field.getName();
                        continue;
                    }
                    if (field.getAnnotation(ModifiedTime.class) != null) {
                        fieldNames[2] = field.getName();
                        continue;
                    }
                }
            }
        }
        Field field = ReflectionUtils.getAccessibleField(entity, "updTime");
        Object[] value = null;
        if (field != null && field.getType().equals(Date.class)) {
            value = new Object[]{userName, userId, new Date()};
        }
        // 填充默认属性值
        setDefaultValues(entity, fieldNames, value);
    }

    private static <T> void setDefaultValues(T entity, String[] fields, Object[] value) {
        StringBuffer sb = new StringBuffer("");
        for (int i = 0; i < fields.length; i++) {
            String field = fields[i];
            try {
                if (ReflectionUtils.hasField(entity, field)) {
                    ReflectionUtils.invokeSetter(entity, field, value[i]);
                }
            } catch (Exception e) {
                sb.append(field).append(" ");
            }
        }
        if (!sb.toString().isEmpty()) {
            log.error(entity.getClass().getName() + ",部分字段审计失败: " + sb.toString());
        }
    }

    public static <T> boolean isPKNotNull(T entity, String field) {
        if (!ReflectionUtils.hasField(entity, field)) {
            return false;
        }
        Object value = ReflectionUtils.getFieldValue(entity, field);
        return value != null && !"".equals(value);
    }
}
