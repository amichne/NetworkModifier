package InMemMatsim.Model.Specification.Core;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Parser {

    public static Element getParameterElement(Element element, String parameter) throws InstantiationException {
        NodeList nodeList = element.getElementsByTagName(parameter);
        if (nodeList.getLength() == 1){
            return (Element) nodeList.item(0);
        }
        if (nodeList.getLength() == 0){
            return element;
        }
        else {
            throw new InstantiationException("The specification contains multiple tags with the name " +
                    parameter + "\nThis is not permitted and must be fixed.");
        }
    }

    public static HashMap<String, String> getParameters(Element element, Class parseClass, String attribute){
        HashMap<String, String> params = new HashMap<>();
        for (String field : Parameter.getPrimitiveFieldNames(parseClass.getDeclaredFields())) {
            Node node = getChild(element, field);
            if (node != null)
                params.put(field, getChild(element, field).getAttribute(attribute));
        }

        return params;
    }

    public static HashMap<String, String> getParameters(Element element, Class parseClass){
        return getParameters(element, parseClass, "value");
    }

    @Deprecated
    public static void getDescendant(Parameter param, Element element){
        String[] fieldNames = Parameter.getPrimitiveFieldNames(param.getClass().getDeclaredFields());
        for (Field field : param.getClass().getDeclaredFields()) {
            if (!Arrays.asList(fieldNames).contains(field.getName())) {
                if (param.DESCENDANT == field.getType()) {
                    try {
                        field.set(param, field.getType().getDeclaredMethod("parse", Element.class).invoke(null, element));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        // TODO: Fix this up a bit
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }
        }
    }

    public static Element getChild(Element element, String childName){
        NodeList nodes = element.getElementsByTagName(childName);
        Element child = null;
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (child == null)
                    child = (Element) node;
                else
                    throw new InstantiationError(
                            childName + " must be unique element specification XML.");
            }
        }
        return child;
    }

    public static List<Element> getChildren(Element element, String childName){
        NodeList nodes = element.getElementsByTagName(childName);
        List<Element> children = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                children.add((Element) node);
            }
        }
        return children;
    }

    public static String getClassName(Class classObj){
        return classObj.getName().toLowerCase().split("\\.")[
                classObj.getName().toLowerCase().split("\\.").length - 1];
    }
}
