package com.thinking.machines.nafserver.model;
public class Property
{
private String name;
private Class type;
public Property()
{
}
public void setName(String name)
{
this.name=name;
}
public String getName()
{
return this.name;
}
public void setType(Class type)
{
this.type=type;
}
public Class getType()
{
return this.type;
}

}