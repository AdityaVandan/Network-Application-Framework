package com.thinking.machines.nafserver.tool;
import java.net.*;
public class TMClassLoader
{
public URL[] getURLs()
{
URL urls[]=new URL[1];
try
{
urls[0]=new URL("file:c:/coding/projects/naf/nafserver/testcases/build/classes/main/");
}catch(Exception exception)
{
System.out.println("TMClassLoader Problem: "+exception.getMessage());
}
return urls;
}
}