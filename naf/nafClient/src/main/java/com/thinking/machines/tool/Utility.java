package com.thinking.machines.tool;
import java.lang.reflect.*;
import java.util.*;
public class Utility
{
private static HashSet<String> primitiveDataTypeSet;
private static HashSet<String> arrayDataTypeSet;
static
{
primitiveDataTypeSet=new HashSet<>();
primitiveDataTypeSet.add("int");
primitiveDataTypeSet.add("long");
primitiveDataTypeSet.add("char");
primitiveDataTypeSet.add("float");
primitiveDataTypeSet.add("double");
primitiveDataTypeSet.add("short");
primitiveDataTypeSet.add("byte");
primitiveDataTypeSet.add("boolean");
primitiveDataTypeSet.add("java.lang.String");
primitiveDataTypeSet.add("java.lang.Integer");
primitiveDataTypeSet.add("java.lang.Long");
primitiveDataTypeSet.add("java.lang.Character");
primitiveDataTypeSet.add("java.lang.Float");
primitiveDataTypeSet.add("java.lang.Double");
primitiveDataTypeSet.add("java.lang.Short");
primitiveDataTypeSet.add("java.lang.Byte");
primitiveDataTypeSet.add("java.lang.Boolean");
arrayDataTypeSet=new HashSet<>();
arrayDataTypeSet.add("[I");
arrayDataTypeSet.add("[J");
arrayDataTypeSet.add("[C");
arrayDataTypeSet.add("[F");
arrayDataTypeSet.add("[D");
arrayDataTypeSet.add("[S");
arrayDataTypeSet.add("[B");
arrayDataTypeSet.add("[Z");
arrayDataTypeSet.add("[Ljava.lang.String;");
arrayDataTypeSet.add("[Ljava.lang.Integer;");
arrayDataTypeSet.add("[Ljava.lang.Long;");
arrayDataTypeSet.add("[Ljava.lang.Character;");
arrayDataTypeSet.add("[Ljava.lang.Float;");
arrayDataTypeSet.add("[Ljava.lang.Double;");
arrayDataTypeSet.add("[Ljava.lang.Short;");
arrayDataTypeSet.add("[Ljava.lang.Byte;");
arrayDataTypeSet.add("[Ljava.lang.Boolean;");
arrayDataTypeSet.add("[Ljava.lang.Object;");
}
public Utility()
{
}
// ************************copyObject
public static void copyObject(Object target,Object source)
{
Class sourceClass,targetClass;
sourceClass=source.getClass();
targetClass=target.getClass();
Method targetMethods[];
targetMethods=targetClass.getMethods();
Method sourceMethods[];
sourceMethods=sourceClass.getMethods();
LinkedList<Method> sourceGetterMethods=new LinkedList<>();
LinkedList<Pair<Method,Method>> setterGetters=new LinkedList<Pair<Method,Method>>();
//---------------------------------------- extraction of informatio about setters and getters starts
for(Method method:sourceMethods)
{
if(isGetter(method))
{
System.out.println("Source Getter method: "+method.getName());
sourceGetterMethods.add(method);
}
}
String setterName,getterName;
Method getterMethod;
for(Method method:targetMethods)
{
if(!isSetter(method)) continue;
getterMethod=getGetterOf(method,sourceGetterMethods);
if(getterMethod!=null) setterGetters.add(new Pair(method,getterMethod));
}
//---------------------------------------- Information about setters of source and getters of target gathered
Class propertyType;
Object object;
for(Pair<Method,Method> pair:setterGetters)
{
System.out.println(pair.first.getName()+"------>"+pair.second.getName());
propertyType=pair.second.getReturnType();
//if(isPrimitive(propertyType))
//{
try
{
pair.first.invoke(target,pair.second.invoke(source));
}catch(IllegalAccessException iae) {}
catch(InvocationTargetException ite) {}
catch(Throwable t) {}
//continue;
//}
if(isOneDimensionalArray(propertyType))
{
System.out.println("array hai class mai");
continue;
}




}

}
public static boolean isPrimitive(Class propertyType)
{
return primitiveDataTypeSet.contains(propertyType.getName());
}
public static boolean isOneDimensionalArray(Class propertyType)
{
return arrayDataTypeSet.contains(propertyType.getName());
}
public static Method getGetterOf(Method setterMethod,LinkedList<Method> getterMethods)
{
String setterPropertyName="";
String setterName=setterMethod.getName();
Class setterPropertyType;
Class getterPropertyType;
if(setterName.length()>3) setterPropertyName=setterName.substring(3);
String getterName;
setterPropertyType=setterMethod.getParameterTypes()[0];
String getterPropertyName;
for(Method method:getterMethods)
{
getterPropertyName="";
getterName=method.getName();
if(getterName.length()>3) getterPropertyName=getterName.substring(3);
getterPropertyType=method.getReturnType();
if(getterPropertyName.equals(setterPropertyName) && getterPropertyType.equals(setterPropertyType)) return method;
}
return null;
}
public static boolean isSetter(Method method)
{
return method.getName().startsWith("set") && method.getParameterCount()==1;
}
public static boolean isGetter(Method method)
{
if(method.getName().startsWith("get")==false) return false;
if(method.getReturnType().getSimpleName().toUpperCase().equals("VOID")) return false;
if(method.getParameterCount()>0) return false;
return true;
}

}