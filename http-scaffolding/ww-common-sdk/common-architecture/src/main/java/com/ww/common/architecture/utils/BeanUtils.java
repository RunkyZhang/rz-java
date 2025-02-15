package com.ww.common.architecture.utils;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class BeanUtils {
    /**
     * @param orig 源对象
     * @param dest 目标对象
     */
    public static void copyProperties(final Object orig, final Object dest) {
        try {
            org.springframework.beans.BeanUtils.copyProperties(orig, dest);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * @param origs           源list对象
     * @param dests           目标list对象
     * @param origsElementTpe 源list元素类型对象
     * @param destElementTpe  目标list元素类型对象
     * @param <T1>            源list元素类型
     * @param <T2>            目标list元素类型
     * @Description：拷贝list元素对象，将origs中的元素信息，拷贝覆盖至dests中
     */
    public static <T1, T2> void copyProperties(final List<T1> origs, final List<T2> dests, Class<T1> origsElementTpe, Class<T2> destElementTpe) {
        if (origs == null || dests == null) {
            return;
        }
        if (dests.size() != 0) {
            //防止目标对象被覆盖，要求必须长度为零
            throw new RuntimeException("目标对象存在值");
        }
        try {
            for (T1 orig : origs) {
                T2 t = destElementTpe.newInstance();
                dests.add(t);
                copyProperties(orig, t);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 将对象转化为map
     *
     * @param bean
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key.toString(), beanMap.get(key));
            }
        }
        return map;
    }

    public static Map<String, Object> objToMap(Object object) {
        Class clazz = object.getClass();
        Map<String, Object> treeMap = Maps.newHashMap();

        while (null != clazz.getSuperclass()) {
            Field[] declaredFields1 = clazz.getDeclaredFields();
            for (Field field : declaredFields1) {
                String name = field.getName();

                // 获取原来的访问控制权限
                boolean accessFlag = field.isAccessible();
                // 修改访问控制权限
                field.setAccessible(true);
                try {
                    Object value = field.get(object);
                    // 恢复访问控制权限
                    field.setAccessible(accessFlag);
                    if (null != value && StringUtils.isNotBlank(value.toString())) {
                        //如果是List,将List转换为json字符串
                        if (value instanceof List) {
                            value = JSON.toJSONString(value);
                        }
                        treeMap.put(name, value);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            clazz = clazz.getSuperclass();
        }
        return treeMap;
    }

    public static void defaultValue(Object demo) {
        try {
            Class<?> aClass = demo.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            List<String> fieldName = new ArrayList<>();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                String name = field.getName();
                if (!"".equals(name)) {
                    fieldName.add(name);
                }
            }
            for (String s : fieldName) {
                Method method = aClass.getMethod("get" + s.substring(0, 1).toUpperCase() + s.substring(1));
                Object invoke = method.invoke(demo);
                if (invoke == null || "".equals(invoke)) {
                    Field field = aClass.getDeclaredField(s);
                    field.setAccessible(true);
                    String typeName = field.getGenericType().getTypeName();
                    if (typeName.contains("String")) {
                        field.set(demo, "");
                    } else if (typeName.contains("Integer")) {
                        field.set(demo, 0);
                    } else if (typeName.contains("Double")) {
                        field.set(demo, 0.0);
                    } else if (typeName.contains("BigDecimal")) {
                        field.set(demo, BigDecimal.valueOf(0));
                    } else if (typeName.contains("Date")) {
                        if (typeName.equals("java.time.LocalDate")) {
                            field.set(demo, LocalDate.now());
                        } else if (typeName.equals("java.time.LocalDateTime")) {
                            field.set(demo, LocalDateTime.now());
                        } else {
                            field.set(demo, new Date());
                        }
                    } else if (typeName.contains("Boolean")) {
                        field.set(demo, true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}