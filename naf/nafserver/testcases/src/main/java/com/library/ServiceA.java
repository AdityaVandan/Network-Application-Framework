package com.library;
import com.thinking.machines.nafserver.annotation.*;
@Path("/serviceA")
public class ServiceA
{
@Path("/whatever")
public String getWhatever()
{
return "Whatever";
}
@Path("/add")
public int add(int a,int b)
{
System.out.println(a+b);
return a+b;
}
@Path("/product")
public int getProduct(int a,int b)
{
return a*b;
}
@Path("/printWhatever")
public void printWhatever()
{
System.out.println("Whatever");
}
public int getDiff(int e,int f)
{
return e-f;
}
}