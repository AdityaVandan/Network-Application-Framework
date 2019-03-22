package com.thinking.machines.nafserver.model;
import com.thinking.machines.nafcommon.*;
import java.util.*;
public class Application
{
private HashMap<String,Service> services;
public Application()
{
this.services=new HashMap<String,Service>();
}
public void addService(String path,Service service) throws ApplicationException
{
if(services.containsKey(path)) throw new ApplicationException("Not yet completed");
services.put(path,service);
}
public boolean containsService(String path)
{
return services.containsKey(path);
}
public Service getService(String path) throws ApplicationException
{
Service service=services.get(path);
if(service==null) throw new ApplicationException("Not yet Completed");
return service;
}
public HashMap<String,Service> getServices() throws ApplicationException
{
if(this.services.size()==0) throw new ApplicationException("No Services");
return this.services;
}
public void setServices(HashMap<String,Service> services)
{
this.services=services;
}
}