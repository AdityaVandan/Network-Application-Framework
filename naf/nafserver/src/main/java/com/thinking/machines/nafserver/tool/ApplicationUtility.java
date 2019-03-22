package com.thinking.machines.nafserver.tool;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.*;
import java.text.*;
import com.thinking.machines.nafserver.model.*;
import com.thinking.machines.nafserver.annotation.*;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;
import java.net.*;
import java.util.zip.*;
public class ApplicationUtility
{
private static Application application=null;
private ApplicationUtility() {}
public static Application getApplication()
{
if(application!=null) return application;
String mainPackage=null;
try
{
throw new RuntimeException();
}catch(RuntimeException re)
{
System.out.println("1 AU");
StackTraceElement e[]=re.getStackTrace();
String className=e[e.length-1].getClassName();
try
{
mainPackage=Class.forName(className).getPackage().getName();
}catch(ClassNotFoundException cnfe)
{
System.out.println("2 AU");
System.out.println("**********Serious Problem**********");
System.exit(0);
}
}
HashMap<String,java.util.List<ModuleMistake>> duplicateServices=new HashMap<>();
HashMap<String,Service> services=new HashMap<>();
LinkedList<ModuleMistake> moduleMistakes=new LinkedList<>();
String packageToAnalyze=mainPackage;
try
{
URLClassLoader ucl=(URLClassLoader)ClassLoader.getSystemClassLoader();
URL urls[]=ucl.getURLs();
String classPathEntry;
ZipInputStream zis;
ZipEntry ze;
String zipEntryName;
String packageName;
String className;
int dotPosition;
String folderName;
File directory;
File files[];
String fileName;
for(URL u:urls)
{
classPathEntry=u.getFile();
if(classPathEntry.endsWith(".jar"))
{
zis=new ZipInputStream(u.openStream());
ze=zis.getNextEntry();
while(ze!=null)
{
zipEntryName=ze.getName();
if(zipEntryName.endsWith(".class"))
{
zipEntryName=zipEntryName.replaceAll("\\\\","\\.");
zipEntryName=zipEntryName.replaceAll("/","\\.");
dotPosition=zipEntryName.lastIndexOf(".",zipEntryName.length()-7);
if(dotPosition==-1)
{
packageName="";
className=zipEntryName;
}
else
{
packageName=zipEntryName.substring(0,dotPosition);
className=zipEntryName.substring(dotPosition+1);
}
if(packageName.startsWith(packageToAnalyze))
{
try
{
Class ccc=Class.forName(zipEntryName.substring(0,zipEntryName.length()-6));
moduleScanner(ccc,services,moduleMistakes,duplicateServices);
}catch(Throwable ee)
{
System.out.println("3 AU");
System.out.println(ee);
}
}
}
ze=zis.getNextEntry();
}
}
else
{
folderName=classPathEntry+packageToAnalyze;
if(File.separator.equals("\\\\"))
{
folderName=folderName.replaceAll("\\.","\\\\");
}
else
{
folderName=folderName.replaceAll("\\.","/");
}
directory=new File(folderName);
if(directory.exists()==false) continue;
Stack<File> stack=new Stack<>();
stack.push(directory);
File fifi;
while(stack.size()>0)
{
fifi=stack.pop();
files=fifi.listFiles();
for(File file:files)
{
if(file.isDirectory())
{
stack.push(file);
continue;
}
if(file.getName().endsWith(".class"))
{
className=file.getName();
packageName=file.getAbsolutePath().substring(classPathEntry.length()-1);
packageName=packageName.substring(0,packageName.length()-className.length()-1);
packageName=packageName.replaceAll("\\\\","\\.");
packageName=packageName.replaceAll("/","\\.");
try
{
Class ccc=Class.forName(packageName+"."+className.substring(0,className.length()-6));
moduleScanner(ccc,services,moduleMistakes,duplicateServices);
}catch(Throwable ee)
{
System.out.println("4 AU");
System.out.println(ee);
}
}
}
}
}
}
}catch(Exception e)
{
System.out.println("5 AU");
StackTraceElement tt[]=e.getStackTrace();
for(StackTraceElement t:tt)
{
System.out.println(t);
}
}
/*
for(Class entry:classes)
{
System.out.println(entry.getName());
}*/
if(moduleMistakes.size()>0 || duplicateServices.size()>0 || services.size()==0)
{

try
{
SimpleDateFormat sdf=new SimpleDateFormat("dd_MM_yyyy__hh_mm_ss");
String fileName="error_"+sdf.format(new java.util.Date())+".pdf";
File file=new File(fileName);
if(file.exists()) file.delete();

Document document=new Document();
PdfWriter.getInstance(document,new FileOutputStream(file));
document.open();
Paragraph paragraph;
int x=0;
int mistakeNumber=1;
if(moduleMistakes.size()==0 && duplicateServices.size()==0 && services.size()==0)
{
paragraph=new Paragraph("No services found");
paragraph.setAlignment(Element.ALIGN_CENTER);
document.add(paragraph);
System.out.println("Errors in application,see file ("+fileName+") for details");
document.close();
System.exit(0);
}
LinkedList<String> classLevelMistakes; 
String moduleName;
LinkedList<ServiceMistake> serviceMistakeList;
LinkedList<String> serviceLevelMistakes;
String serviceName;
String pathString;
LinkedList<ModuleMistake> modm;
String finalString;
LinkedList<String> duplicacyErrs;

if(moduleMistakes.size()>0)
{
for(ModuleMistake moduleMistake:moduleMistakes)
{
moduleName=moduleMistake.getClassName();
classLevelMistakes=(LinkedList<String>)moduleMistake.getMistakes();
serviceMistakeList=(LinkedList<ServiceMistake>)moduleMistake.getServiceMistakes();
if(classLevelMistakes.size()>0)
{
for(String mis:classLevelMistakes)
{
paragraph=new Paragraph(mistakeNumber+". "+moduleName+": "+mis);
document.add(paragraph);
mistakeNumber++;
}
}
if(serviceMistakeList.size()>0)
{
for(ServiceMistake sm:serviceMistakeList)
{
serviceName=sm.getMethod();
serviceLevelMistakes=(LinkedList<String>)sm.getMistakes();
for(String me:serviceLevelMistakes)
{
paragraph=new Paragraph(mistakeNumber+". "+moduleName+":"+serviceName+": "+me);
mistakeNumber++;
}
}
}
}
}
if(duplicateServices.size()>0)
{
for(Map.Entry<String,java.util.List<ModuleMistake>> entry:duplicateServices.entrySet())
{
duplicacyErrs=new LinkedList<String>();
pathString=entry.getKey();
modm=(LinkedList<ModuleMistake>)entry.getValue();
for(ModuleMistake m:modm)
{
moduleName=m.getClassName();
serviceMistakeList=(LinkedList<ServiceMistake>)m.getServiceMistakes();
for(ServiceMistake sm:serviceMistakeList)
{
serviceName=sm.getMethod();
finalString=serviceName;
duplicacyErrs.add(finalString);
}
}
paragraph=new Paragraph(mistakeNumber+". The following methods have same value of Path annotation with path as: "+pathString);
document.add(paragraph);
x=1;
for(String str:duplicacyErrs)
{
paragraph=new Paragraph(x+". "+str);
document.add(paragraph);
x++;
}

}
}




System.out.println("Errors in application,see file("+fileName+") for details");
document.close();
System.exit(0);
}catch(Exception e)
{
System.out.println("6 AU");
System.out.println(e.getMessage());
System.exit(0);
}//create pdf at current location
System.exit(0);
}
application=new Application();
application.setServices(services);
return application;
}
private static void moduleScanner(Class ccc,HashMap<String,Service> services,LinkedList<ModuleMistake> moduleMistakes,HashMap<String,java.util.List<ModuleMistake>> duplicateServices)
{
System.out.println(ccc.getName()+" is being scanned");
Class sessionScopeClass=SessionScope.class;
Class applicationScopeClass=ApplicationScope.class;
int j;
Class parameterTypes[];
ArrayList<Integer> indicesList;
int indices[];
LinkedList<Property> autoWiredProperties;
Property property;
Field fields[];
Field field;
Module module;
boolean flag;
LinkedList<ModuleMistake> linkedList;
ModuleMistake mm;
ServiceMistake sm;
Service s;
Service service;
String servicePathString;
boolean isValidModulePath,isValidServicePath;
isValidModulePath=false;
isValidServicePath=false;
Class pathClass=Path.class;
Path modulePath=null;
String modulePathString=null;
ModuleMistake moduleMistake=null;
if(ccc.isAnnotationPresent(pathClass))
{
modulePath=(Path)ccc.getAnnotation(pathClass);
modulePathString=modulePath.value();
isValidModulePath=isValidPath(modulePathString);
if(isValidModulePath==false)
{
moduleMistake=new ModuleMistake(ccc.getName());
moduleMistake.addMistake("Invalid Path: "+modulePathString);
}
if(Modifier.isPublic(ccc.getModifiers())==false)
{
if(moduleMistake==null) moduleMistake=new ModuleMistake(ccc.getName());
moduleMistake.addMistake("class is not declared as public");
}
}
Method methods[]=ccc.getDeclaredMethods();
Path methodPath=null;
String methodPathString=null;
ServiceMistake serviceMistake;
module=null;
for(Method m:methods)
{
System.out.println("Analyzing: "+m+"for : "+pathClass.getName());
serviceMistake=null;
if(m.isAnnotationPresent(pathClass))
{
System.out.println("Ghoda 1");
methodPath=(Path)m.getAnnotation(pathClass);
methodPathString=methodPath.value();
isValidServicePath=isValidPath(methodPathString);
if(isValidServicePath==false)
{
serviceMistake=new ServiceMistake(m.toString());
serviceMistake.addMistake("Invalid Path: "+methodPathString);
}
if(!Modifier.isPublic(m.getModifiers()))
{
if(serviceMistake==null) serviceMistake=new ServiceMistake(m.toString());
serviceMistake.addMistake("method not declared as public");
}
if(isValidModulePath && isValidServicePath)
{
servicePathString=modulePathString+methodPathString;
if(services.containsKey(servicePathString)==false)
{
if(moduleMistake==null && serviceMistake==null)
{
if(duplicateServices.containsKey(servicePathString)==false)
{
if(duplicateServices.size()==0 && moduleMistakes.size()==0)
{
service=new Service();
if(module==null)
{
module=new Module();
module.setServiceClass(ccc);
if(ccc.isAnnotationPresent(ApplicationAware.class)) module.setIsApplicationAware(true);
if(ccc.isAnnotationPresent(SessionAware.class)) module.setIsApplicationAware(true);
autoWiredProperties=new LinkedList<>();
fields=ccc.getDeclaredFields();
for(Field ff:fields)
{
if(ff.getAnnotation(AutoWired.class)!=null)
{
property=new Property();
property.setName(ff.getName());
property.setType(ff.getType());
autoWiredProperties.add(property);
}
}
module.setAutoWiredProperties(autoWiredProperties);
}
service.setModule(module);
service.setMethod(m);
service.setNumberOfParameters(m.getParameterCount());
if(m.getReturnType().getSimpleName().equalsIgnoreCase("VOID"))
{
service.setIsVoid(true);
}
parameterTypes=m.getParameterTypes();
indicesList=new ArrayList<>();
for(j=0;j<parameterTypes.length;j++)
{
if(parameterTypes[j].equals(sessionScopeClass))
{
indicesList.add(j);
}
}
if(indicesList.size()>0)
{
service.setInjectSession(true);
indices=new int[indicesList.size()];
j=0;
for(Integer iii:indicesList)
{
indices[j]=iii;
j++;
}
service.setSessionParametersIndexes(indices);
}
indicesList=new ArrayList<>();
for(j=0;j<parameterTypes.length;j++)
{
if(parameterTypes[j].equals(applicationScopeClass))
{
indicesList.add(j);
}
}
if(indicesList.size()>0)
{
service.setInjectApplication(true);
indices=new int[indicesList.size()];
j=0;
for(Integer iii:indicesList)
{
indices[j]=iii;
j++;
}
service.setApplicationParametersIndexes(indices);
}

services.put(servicePathString,service);
}
}
else
{
linkedList=(LinkedList<ModuleMistake>)duplicateServices.get(servicePathString);
flag=false;
for(ModuleMistake mmm:linkedList)
{
if(mmm.getClassName().equals(ccc.getName()))
{
sm=new ServiceMistake(m.toString());
mmm.addServiceMistake(sm);
flag=true;
break;
}
}
if(!flag)
{
mm=new ModuleMistake(ccc.getName());
sm=new ServiceMistake(m.toString());
mm.addServiceMistake(sm);
linkedList.add(mm);
}
}
}
}
else
{
s=services.remove(servicePathString);
mm=new ModuleMistake(s.getModule().getServiceClass().getName());
sm=new ServiceMistake(s.getMethod().toString());
mm.addServiceMistake(sm);
linkedList=new LinkedList<>();
linkedList.add(mm);
if(ccc.equals(s.getModule().getServiceClass()))
{
sm=new ServiceMistake(m.toString());
mm.addServiceMistake(sm);
}
else
{
mm=new ModuleMistake(ccc.getName());
System.out.println("Bhadwa: ------------------------------------- "+mm.getClassName());
sm=new ServiceMistake(m.toString());
mm.addServiceMistake(sm);
linkedList.add(mm);
}
duplicateServices.put(servicePathString,linkedList);
}
}
if(serviceMistake!=null)
{
if(moduleMistake==null) moduleMistake=new ModuleMistake(ccc.getName());
moduleMistake.addServiceMistake(serviceMistake);
}
}
}
if(moduleMistake!=null)
{
moduleMistakes.add(moduleMistake);
}
System.out.println("mm: "+moduleMistakes.size()+",ds: "+duplicateServices.size()+",ser: "+services.size());
}
private static boolean isValidPath(String path)
{
return true;
}
}