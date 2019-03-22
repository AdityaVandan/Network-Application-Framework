package com.thinking.machines.nafserver.tool;
import java.util.*;
class ModuleMistake
{
private String className;
private List<String> mistakes;
private List<ServiceMistake> serviceMistakes;
public ModuleMistake(String className)
{
this.className=null;
this.mistakes=new LinkedList<>();
this.serviceMistakes=new LinkedList<>();
}
public void addMistake(String mistake)
{
this.mistakes.add(mistake);
}
public void addServiceMistake(ServiceMistake serviceMistake)
{
this.serviceMistakes.add(serviceMistake);
}
public String getClassName()
{
return this.className;
}
public List<String> getMistakes()
{
return this.mistakes;
}
public List<ServiceMistake> getServiceMistakes()
{
return this.serviceMistakes;
}
}