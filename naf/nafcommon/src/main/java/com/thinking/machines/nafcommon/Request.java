package com.thinking.machines.nafcommon;
public class Request implements java.io.Serializable
{
private String path;
private Object [] arguments;
private String clientID;
public Request()
{
this.path=null;
this.arguments=null;
clientID=null;
}
public void setPath(String path)
{
this.path=path;
}
public String getPath()
{
return this.path;
}
public void setArguments(Object [] arguments)
{
this.arguments=arguments;
}
public Object [] getArguments()
{
return this.arguments;
}
public void setClientID(String path)
{
this.clientID=clientID;
}
public String getClientID()
{
return this.clientID;
}
}