package InMemMatsim.Model.Specification.Core;

import com.google.common.collect.ImmutableMap;
import org.matsim.core.config.Config;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class Parameter {
    // Primitive types defined for use in deciding how to treat parameters
    private static final List<Class> PRIMITIVE_TYPES = Arrays.asList(
            String.class, boolean.class,
            int.class, long.class,
            float.class, double.class);

    // Used to access the correct "valueOf" methods based on input data type
    private static ImmutableMap<Class, Method> VALUEOF_METHODS;
    static { try {
            VALUEOF_METHODS = ImmutableMap.of(
                    boolean.class, Boolean.class.getMethod("valueOf", String.class),
                    int.class, Integer.class.getMethod("valueOf", String.class),
                    long.class, Long.class.getMethod("valueOf", String.class),
                    float.class, Float.class.getMethod("valueOf", String.class),
                    double.class, Double.class.getMethod("valueOf", String.class));
    } catch (NoSuchMethodException e) { e.printStackTrace(); } }

    private static final List<Class> LIST_TYPES = Arrays.asList(
            List.class, ArrayList.class, Collection.class);

    protected Class DESCENDANT;
    public Parameter() { }

    public abstract void toMatsim(Config config);


    public static void populate(Object object, Map<String, String> params) {
        validateInputParams(object, params);
        setFields(object, params);
    }

    public static String[] getPrimitiveFieldNames(Field[] fields) {
        return getFieldNamesByClass(fields, PRIMITIVE_TYPES);
    }

    public static String[] getFieldNamesByClass(Field[] fields, List<Class> classes) {
        List<String> names = new ArrayList<>();
        for (Field field : fields)
            if (classes.contains(field.getType()))
                names.add(field.getName());
        return names.toArray(new String[0]);
    }

    public static Field[] getPrimitiveFields(Field[] fields) {
        return getFieldsByClass(fields, PRIMITIVE_TYPES);
    }

    public static Field[] getFieldsByClass(Field[] fields, List<Class> classes){
        List<Field> fieldList = new ArrayList<>();
        for (Field field : fields)
            if (classes.contains(field.getType()))
                fieldList.add(field);
        return fieldList.toArray(new Field[0]);
    }

    private static void setFields(Object object, Map<String, String> params) {
        for (String key : params.keySet()) {
            if (params.get(key).equals("") || params.get(key).equals("null") || params.get(key) == null)
                continue;
            try {
                Field field = object.getClass().getDeclaredField(key);
                if (PRIMITIVE_TYPES.contains(field.getType())){
                    if (field.getType() == String.class){
                        field.set(object, params.get(key));
                    }
                    else {
                        field.set(object, VALUEOF_METHODS.get(field.getType()).invoke(null, params.get(key)));
                    }
                }
            } catch (IllegalAccessException | NoSuchFieldException | InvocationTargetException e) {
                // TODO: Change this error to exception, and bubble up
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public static void setMatsimParams(Object matsimParams, Object icarusParams) {
        HashMap<String, Method> setters = new HashMap<>();
        for (Method method : matsimParams.getClass().getMethods())
            if (method.getName().startsWith("set"))
                setters.put(method.getName().toLowerCase(), method);

        for (Field field : getPrimitiveFields(icarusParams.getClass().getDeclaredFields())) {
            try {
                Method setter = setters.getOrDefault(("set" + field.getName()).toLowerCase(), null);
                if (setter != null)
                    setter.invoke(matsimParams, field.get(icarusParams));
            } catch (IllegalAccessException | InvocationTargetException e) {
                // TODO: Change this error to exception, and bubble up
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    private static void validateInputParams(Object object, Map<String, ?> params) {
        List<String> fieldNames = Arrays.asList(getPrimitiveFieldNames(
                object.getClass().getDeclaredFields()));

        if (!fieldNames.containsAll(params.keySet()))
            throw new InstantiationError(
                    "Specified key in input parameters doesn't exist in object given.");
    }
}
