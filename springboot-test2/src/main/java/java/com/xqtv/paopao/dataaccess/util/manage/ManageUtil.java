package java.com.xqtv.paopao.dataaccess.util.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import tv.xingqiu.common.util.BeanCopyUtils;

import javax.persistence.Column;
import javax.persistence.criteria.*;
import java.com.xqtv.paopao.dataaccess.log.BCLogger;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ManageUtil {

    public static final int MAX_A_ANCHOR_COUNT = 1000;
    public static final int MAX_B_ANCHOR_COUNT = 1000;

    public static <T> T covertFromJson(JSONObject json, Class<T> clazz) {
        if (json != null && !json.isEmpty()) {
            try {
                T t = clazz.newInstance();
                for (Field field : clazz.getDeclaredFields()) {
                    String fieldName = field.getName();
                    // 参数名为数据库列名
                    String annoName = null;
                    if (field.getAnnotation(Column.class) != null) {
                        annoName = field.getAnnotation(Column.class).name().replaceAll("`", "");
                    }
                    if (StringUtils.isNotBlank(annoName) && json.containsKey(annoName)) {
                        Character c = fieldName.charAt(0);
                        Method method = clazz.getMethod(
                                "set" + fieldName.replaceFirst(c.toString(), c.toString().toUpperCase()),
                                field.getType());
                        method.setAccessible(true);
                        // field类型只有Long，Integer，String和Boolean
                        String type = method.getParameterTypes()[0].getName();
                        if (type.equals(String.class.getName())) {
                            method.invoke(t, json.getString(annoName));
                        } else if (type.equals(Long.class.getName()) || type.equals(long.class.getName())) {
                            method.invoke(t, json.getLong(annoName));
                        } else if (type.equals(Integer.class.getName()) || type.equals(int.class.getName())) {
                            method.invoke(t, json.getInteger(annoName));
                        } else if (type.equals(Boolean.class.getName()) || type.equals(boolean.class.getName())) {
                            method.invoke(t, json.getBoolean(annoName));
                        } else if (type.equals(Double.class.getName()) || type.equals(double.class.getName())) {
                            method.invoke(t, json.getDouble(annoName));
                        }
                    }
                }
                return t;
            } catch (Exception e) {
                BCLogger.error(BeanCopyUtils.class, e.getMessage());
            }
        }
        return null;
    }

    public static <T> JSONArray covertToArray(List<T> list) {
        return covertToArray(list, null, null);
    }

    public static <T> JSONArray covertToArray(List<T> list, Map<String, Map<String, Object>> map, String keyColumn) {
        JSONArray array = new JSONArray();
        for (T po : list) {
            try {
                JSONObject json = new JSONObject();
                for (Field field : po.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    String annoName = null;
                    if (field.getAnnotation(Column.class) != null) {
                        annoName = field.getAnnotation(Column.class).name();
                    }
                    if (StringUtils.isNotBlank(annoName) && field.get(po) != null) {
                        annoName = annoName.replaceAll("`", "");
                        json.put(annoName, field.get(po));
                    }
                }
                // additional fields
                if (map != null && !map.isEmpty() && StringUtils.isNotBlank(keyColumn)) {
                    for (String key : map.keySet()) {
                        Map<String, Object> innerMap = map.get(key);
                        for (String innerKey : innerMap.keySet()) {
                            if (innerKey.equals(json.get(keyColumn).toString())) {
                                json.put(key, innerMap.get(innerKey));
                                break;
                            }
                        }
                    }
                }
                array.add(json);
            } catch (Exception e) {
                BCLogger.error(BeanCopyUtils.class, e.getMessage());
            }
        }
        return array;
    }

    public static JSONObject covertToJson(Object... obj) {
        JSONObject json = new JSONObject();
        for (Object po : obj) {
            try {
                for (Field field : po.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    String annoName = null;
                    if (field.getAnnotation(Column.class) != null) {
                        annoName = field.getAnnotation(Column.class).name();
                    }
                    if (StringUtils.isNotBlank(annoName) && field.get(po) != null) {
                        json.put(annoName.replaceAll("`", ""), field.get(po));
                    }
                }
            } catch (Exception e) {
                BCLogger.error(BeanCopyUtils.class, e.getMessage());
            }
        }
        return json;
    }

    public static <T> Specification<T> createSpecification(final T condition, final int page, final int count) {
        return createSpecification(condition, null, page, count);
    }

    public static <T> Specification<T> createSpecification(final T condition, final Map<String, String> map,
            final int page, final int count) {
        Specification<T> spec = new Specification<T>() {

            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                if (map != null && !map.isEmpty()) {
                    for (String key : map.keySet()) {
                        Path<Long> path = root.get(key);
                        String[] vals = map.get(key).split("%");
                        if (vals.length == 2 && StringUtils.isNotBlank(vals[0])) {
                            predicateList.add(cb.between(path, Long.parseLong(vals[0]), Long.parseLong(vals[1])));
                        } else {
                            if (StringUtils.isNotBlank(vals[0])) {
                                predicateList.add(cb.greaterThanOrEqualTo(path, Long.parseLong(vals[0])));
                            }
                            if (vals.length > 1 && StringUtils.isNotBlank(vals[1])) {
                                predicateList.add(cb.lessThanOrEqualTo(path, Long.parseLong(vals[1])));
                            }
                        }
                    }
                }

                if (condition != null) {
                    for (Field field : condition.getClass().getDeclaredFields()) {
                        try {
                            field.setAccessible(true);
                            Object value = field.get(condition);
                            if (value != null && !value.toString().isEmpty()) {
                                Path<?> path = root.get(field.getName());
                                if (value.toString().trim().startsWith("%") || value.toString().trim().endsWith("%")) {
                                    Path<String> path2 = root.get(field.getName());
                                    predicateList.add(cb.like(path2, value.toString()));
                                } else {
                                    predicateList.add(cb.equal(path, value));
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                if (!predicateList.isEmpty()) {
                    query.where(predicateList.toArray(new Predicate[predicateList.size()]));
                }
                return null;
            }
        };

        return spec;
    }
}
