package uitls;

import javax.json.*;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.HashMap;

public class JsonHandler {
    private JsonBuilderFactory jsonBuilderFactory = Json.createBuilderFactory(null);
    private JsonObjectBuilder jsonObjectBuilder = jsonBuilderFactory.createObjectBuilder();
    public <E> String objectToJson(Object object)throws Exception{
        Field[] fields = object.getClass().getDeclaredFields();
        int i=0;
        for(Field field: fields){
            if(field.get(object) != null){
                String value;
                if(field.getType().getSimpleName().indexOf("[]") != -1){
                    Object[] objects = (Object[])field.get(object);
                    String s ="[";
                    for(i=0; i< objects.length;i++){
                        if(i == objects.length -1) s += objects[i]+"]";
                        else s += objects[i]+",";
                    }
                    value = s;
                }
                else value = field.get(object).toString();
                jsonObjectBuilder.add(field.getName(), value);
            }
            i++;
        }
        JsonObject data  = jsonObjectBuilder.build();
        return data.toString();
    }
    public void jsonToObject(String json, Object object){
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject data = reader.readObject();
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field: fields){
            if(data.get(field.getName()) == null) continue;
            try {
                switch (field.getType().getSimpleName()){
                    case "Boolean":
                        field.set(object, Boolean.parseBoolean(data.get(field.getName()).toString()));
                        break;
                    case "Boolean[]":
                        String[] boolValues = getStringArrayToConvert(data.get(field.getName()).toString());
                        Boolean[] booleans = new Boolean[boolValues.length];
                        int iBool=0;
                        for(String s: boolValues){
                            booleans[iBool] = Boolean.parseBoolean(s);
                            iBool++;
                        }
                        field.set(object, booleans);
                        break;
                    case "Byte":
                        field.set(object, Byte.parseByte(data.get(field.getName()).toString()));
                        break;
                    case "Byte[]":
                        String[] byteValues = getStringArrayToConvert(data.get(field.getName()).toString());
                        Byte[] bytes = new Byte[byteValues.length];
                        int iByte=0;
                        for(String s: byteValues){
                            bytes[iByte] = Byte.parseByte(s);
                            iByte++;
                        }
                        field.set(object, bytes);
                        break;
                    case "Character":
                        field.set(object, data.get(field.getName()).toString());
                        break;
                    case "Character[]":
                        String[] charValues = getStringArrayToConvert(data.get(field.getName()).toString());
                        Character[] characters = new Character[charValues.length];
                        int iChar=0;
                        for(String s: charValues){
                            characters[iChar] = s.charAt(0);
                            iChar++;
                        }
                        field.set(object, characters);
                        break;
                    case "Short":
                        field.set(object, Short.parseShort(data.get(field.getName()).toString()));
                        break;
                    case "Short[]":
                        String[] shortValues = getStringArrayToConvert(data.get(field.getName()).toString());
                        Short[] shorts = new Short[shortValues.length];
                        int iShort=0;
                        for(String s: shortValues){
                            shorts[iShort] = Short.parseShort(s);
                            iShort++;
                        }
                        field.set(object, shorts);
                        break;
                    case "Integer":
                        field.set(object, Integer.parseInt(data.get(field.getName()).toString()));
                        break;
                    case "Integer[]":
                        String[] integerValues = getStringArrayToConvert(data.get(field.getName()).toString());
                        Integer[] integers = new Integer[integerValues.length];
                        int iInt=0;
                        for(String s: integerValues){
                            integers[iInt] = Integer.parseInt(s);
                            iInt++;
                        }
                        field.set(object, integers);
                        break;
                    case "Float":
                        field.set(object, Float.parseFloat(data.get(field.getName()).toString()));
                        break;
                    case "Float[]":
                        String[] floatValues = getStringArrayToConvert(data.get(field.getName()).toString());
                        Float[] floats = new Float[floatValues.length];
                        int iFloat=0;
                        for(String s: floatValues){
                            floats[iFloat] = Float.parseFloat(s);
                            iFloat++;
                        }
                        field.set(object, floats);
                        break;
                    case "Double":
                        field.set(object, Double.parseDouble(data.get(field.getName()).toString()));
                        break;
                    case "Double[]":
                        String[] doubleValues = getStringArrayToConvert(data.get(field.getName()).toString());
                        Double[] doubles = new Double[doubleValues.length];
                        int iDouble=0;
                        for(String s: doubleValues){
                            doubles[iDouble] = Double.parseDouble(s);
                            iDouble++;
                        }
                        field.set(object, doubles);
                        break;
                    case "Long":
                        field.set(object, Long.parseLong(data.get(field.getName()).toString()));
                        break;
                    case "Long[]":
                        String[] longValues = getStringArrayToConvert(data.get(field.getName()).toString());
                        Long[] longs = new Long[longValues.length];
                        int iLong=0;
                        for(String s: longValues){
                            longs[iLong] = Long.parseLong(s);
                            iLong++;
                        }
                        field.set(object, longs);
                        break;
                    case "String[]":
                        String[] stringValues = getStringArrayToConvert(data.get(field.getName()).toString());
                        field.set(object, stringValues);
                        break;
                    default:
                        String value = data.get(field.getName()).toString();
                        value = value.substring(1, value.length() -1 );
                        field.set(object, value);
                        break;
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    public String[] getStringArrayToConvert(String toConvert){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(toConvert, 1, toConvert.length() - 1);
        return stringBuilder.toString().split(",");
    }

    public String addNewProperty(String json, String propName, String value){
        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject jsonObject = reader.readObject();
        for(String key: jsonObject.keySet()){
            jsonObjectBuilder.add(key, jsonObject.get(key));
        }
        jsonObjectBuilder.add(propName, String.valueOf(value));
        return jsonObjectBuilder.build().toString();
    }

    public void jsonToHashMap(String jsonString, HashMap hashMap)throws Exception{
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonObject = reader.readObject();
        for(String key: jsonObject.keySet()){
            hashMap.put(key, jsonObject.get(key));
        }
    }
}
