package com.thinking.machines.nafserver.tool;
import java.util.*;
public class ServiceMistake
{
private String method;
private List<String> mistakes;
public ServiceMistake(String method)
{
this.method=method;
this.mistakes=new LinkedList<String>();
}
public void addMistake(String mistake)
{
this.mistakes.add(mistake);
}
public List<String> getMistakes()
{
return this.mistakes;
}
public String getMethod()
{
return this.method;
}
}