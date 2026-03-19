package com.yaoan.module.econtract.util.xml;
import com.yaoan.module.econtract.controller.admin.contract.xmlvo.ContractVO;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ContractVOCleanerUtil {
    // 最大递归深度限制
    private static final int MAX_DEPTH = 20;
    // 用于检测循环引用的集合
    private static final ThreadLocal<Set<Object>> processedObjects = ThreadLocal.withInitial(HashSet::new);

    public static void filterNullAndEmptyFields(ContractVO contractVO) {
        if (contractVO == null) {
            return;
        }
        try {
            filterObjectFields(contractVO, 0);
        } finally {
            processedObjects.get().clear();
        }
    }

    private static void filterObjectFields(Object object, int depth) {
        if (object == null || depth > MAX_DEPTH) {
            return;
        }

        // 检查是否已经处理过该对象（防止循环引用）
        Set<Object> processed = processedObjects.get();
        if (processed.contains(object)) {
            return;
        }
        processed.add(object);

        try {
            Class<?> clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(object);

                    if (value == null) {
                        continue;
                    }

                    // 处理字符串
                    if (value instanceof String) {
                        if (((String) value).isEmpty()) {
                            field.set(object, null);
                        }
                        String strValue = (String) value;
                        // 先trim()去除首尾空白字符，再检查是否为空
                        if (strValue.trim().isEmpty()) {
                            field.set(object, null);
                        }
                        continue;
                    }

                    // 处理集合
                    if (value instanceof Collection) {
                        Collection<?> collection = (Collection<?>) value;
                        if (collection.isEmpty()) {
                            field.set(object, null);
                            continue;
                        }
                        // 递归处理集合中的每个元素
                        for (Object item : collection) {
                            filterObjectFields(item, depth + 1);
                        }
                    }
                    // 处理自定义对象
                    else if (!isPrimitiveOrWrapper(value.getClass())) {
                        filterObjectFields(value, depth + 1);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            processed.remove(object);
        }
    }

    private static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return clazz.isPrimitive() ||
                clazz.equals(String.class) ||
                clazz.equals(Boolean.class) ||
                clazz.equals(Character.class) ||
                clazz.equals(Byte.class) ||
                clazz.equals(Short.class) ||
                clazz.equals(Integer.class) ||
                clazz.equals(Long.class) ||
                clazz.equals(Float.class) ||
                clazz.equals(Double.class) ||
                clazz.equals(Void.class);
    }
}