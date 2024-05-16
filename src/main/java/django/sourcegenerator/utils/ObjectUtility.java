/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package django.sourcegenerator.utils;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 *
 * @author Mamisoa
 */
public class ObjectUtility {
    public static String capitalize(String text){
        return text.substring(0,1).toUpperCase().concat(text.substring(1));
    }

    public static List<Integer> getUpperCaseIndex(String txt){
        ArrayList<Integer> res = new ArrayList<>();
        for(int i = 0; i < txt.length(); i++){
            if(Character.isUpperCase(txt.charAt(i)))
                res.add(i);
        }
        return res;
    }

    public static String formatToSpacedString(String text){
        String temp = formatToCamelCase(text);
        List<Integer> lst = getUpperCaseIndex(temp);
        String res = "";
        int i = 0;
        for (Integer index : lst) {
            res += capitalize(temp.substring(i, index)) + " ";
            i = index; 
        }
        res += temp.substring(i, temp.length());
        return capitalize(res);
    }

    public static String formatToCamelCase(String str){
        String[] splited = str.split("_");
        if(splited.length <2)
            return str;
        String res = splited[0];
        for(int i = 1; i < splited.length; i++){
            res += ObjectUtility.capitalize(splited[i]);
        }
        return res;
    }

    public static String fillZero(int length, int prefixLength, String num){ //Fill the zero Before the number
        int lim = (length - prefixLength) - num.length();
        String zero = ""+0;
        for(int i = 1 ; i <= lim ; i++){
            num = zero+num;
        }
        return num;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(List<T> list, T obj){
        Object[] array = new Object[list.size()];
        for(int i = 0; i < list.size(); i++){
            T temp = (T) list.get(i);
            Array.set(array, i, temp);
        }
        T[] res = (T[]) array;
        return res;

    }
    public static String formatNumber(Double value, String separation){
        DecimalFormat df = new DecimalFormat( "#,###,###,##0.00" );
        return df.format(value).toString();
    }

    public static String formatNumber(Double value, int approxim, String separation){
        String temp = value.toString();
        String[] list = temp.split("\\.");
        String res = "";
        String after = ".";
        if(!separation.equals(","))
            after = ",";
        if(list.length == 2){
            char[] right = list[1].toCharArray();
            if(right.length > approxim){
                int i = 0;
                while(i < approxim || i > right.length){
                    after += right[i];
                    i++;
                }
            }else{
                after += new String(right);
            }
            String before = addSeparation(list[0], separation);
            res = before + after;
        }else{
            res = addSeparation(temp, separation);
        }
        return res;
    }

    public static String addSeparation(String value, String separation){
        char[] list = value.toCharArray();
        String temp = "";
        int count = 0;
            for(int j = list.length - 1; j >= 0; j--){
                if(count == 3){
                temp += separation;
                count = 0;
            }
            temp += list[j];
            count++;
        }
        return new String(reverseCharArray(temp.toCharArray()));
    }

    public static char[] reverseCharArray(char[] array){
        char[] res = new char[array.length];
        int i = 0;
        for(int j = array.length - 1; j >= 0; j--){
            res[i] = array[j];
            i++;
        }
        return res;
    }
    public static Object[] reverseArray(Object[] array){
        Object[] res = new Object[array.length];
        int i = 0;
        for(int j = array.length - 1; j >= 0; j--){
            res[i] = array[j];
            i++;
        }
        return res;
    }
    public static boolean isAtDefaultValue(Method field, Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Object value = field.invoke(obj);

        if (value == null) {
            return true;
        } else if (field.getReturnType().isPrimitive()) {
            return value.equals(getPrimitiveDefaultValue(field.getReturnType()));
        } else if (value instanceof Collection) {
            return ((Collection<?>) value).isEmpty();
        } else {
            return false;
        }
    }

    public static Object getPrimitiveDefaultValue(Class<?> type) {
        if (type == boolean.class) {
            return false;
        } else if (type == char.class) {
            return '\u0000';
        } else if (type == double.class) {
            return 0.0;
        } else if (type == float.class) {
            return 0.0f;
        } else if (type == byte.class) {
            return (byte) 0;
        } else if (type == short.class) {
            return (short) 0;
        } else if (type == int.class) {
            return 0;
        } else if (type == long.class) {
            return 0L;
        } else {
            return null;  // Pour d'autres types primitifs, la valeur par défaut est null
        }
    }
}
