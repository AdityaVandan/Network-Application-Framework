package com.client;
import com.thinking.machines.nafclient.*;
import com.library.*;
public class Main
{
public static void main(String gg[])
{
try
{
TMNAFClient cl=new TMNAFClient("localhost",5000);
Object r;
r=cl.process("/serviceA/add",100,31);
int rs=(Integer)r;
Object pro;
//cl=new TMNAFClient("localhost",5000);
pro=cl.process("/serviceA/product",12,31);
int prors=(Integer)pro;
Object what;
//cl=new TMNAFClient("localhost",5000);
what=cl.process("/serviceA/whatever");
String ever=(String)what;
System.out.println("Result is: "+rs);
System.out.println("Product is: "+prors);
System.out.println("String is: "+ever);
}catch(Throwable th)
{
System.out.println("Main Client");
System.out.println(th);
}
}
}