package com.thinking.machines.nafserver;
import com.thinking.machines.nafcommon.*;
import com.thinking.machines.nafserver.model.*;
import java.net.*;
import java.lang.reflect.*;
import java.util.*;
import java.io.*;
class ProcessRequest extends Thread
{
Socket socket;
Application application;
HashMap<String,Service> services;
ProcessRequest(Socket ss,Application application)
{
this.socket=ss;
this.application=application;
start();
}
public void run()
{
try
{
services=application.getServices();
//Socket socket;
InputStream is;
int byteCount;
byte requestLengthInBytes[]=new byte[4];;
int requestLength;
byte ack[]=new byte[1];
OutputStream os;
ByteArrayOutputStream baos;
int bytesToRead;
byte chunk[]=new byte[1024];
byte requestBytes[];
ByteArrayInputStream bais;
ObjectOutputStream oos;
ObjectInputStream ois;
Request request;
Response response;
byte responseBytes[];
int responseLength;
byte responseLengthInBytes[]=new byte[4];
int bytesToWrite;
// while loop starting
//socket=server.accept();
is=socket.getInputStream();
byteCount=is.read(requestLengthInBytes);
requestLength=((requestLengthInBytes[0] & 0xFF) <<24 | (requestLengthInBytes[1] & 0xFF) <<16 | (requestLengthInBytes[2] & 0xFF) <<8 | (requestLengthInBytes[3] & 0xFF));
ack[0]=79;
os=socket.getOutputStream();
os.write(ack,0,1);
os.flush();
baos=new ByteArrayOutputStream();
bytesToRead=requestLength;
while(bytesToRead>0)
{
byteCount=is.read(chunk);
if(bytesToRead>0)
{
baos.write(chunk,0,byteCount);
//baos.flush();
}
bytesToRead-=byteCount;
}
ack[0]=79;
os.write(ack,0,1);
os.flush();
requestBytes=baos.toByteArray();
bais=new ByteArrayInputStream(requestBytes);
ois=new ObjectInputStream(bais);
request=(Request)ois.readObject();

//server2.java ->resume from printing info of student
response=this.processRequest(request);
baos=new ByteArrayOutputStream();
oos=new ObjectOutputStream(baos);
oos.writeObject(response);
oos.flush();
responseBytes=baos.toByteArray();
responseLength=responseBytes.length;
responseLengthInBytes[0]=(byte)(responseLength >>24);
responseLengthInBytes[1]=(byte)(responseLength >>16);
responseLengthInBytes[2]=(byte)(responseLength >>8);
responseLengthInBytes[3]=(byte)(responseLength);
os.write(responseLengthInBytes,0,4);
os.flush();
byteCount=is.read(ack);
if(ack[0]!=79) throw new RuntimeException("Unable to receive acknowledgement");
bytesToWrite=responseLength;
int chunkSize=1024;
int i=0;
while(bytesToWrite>0)
{
if(bytesToWrite<chunkSize) chunkSize=bytesToWrite;
os.write(responseBytes,i,chunkSize);
i=i+chunkSize;
bytesToWrite-=chunkSize;
}
byteCount=is.read(ack);
if(ack[0]!=79) throw new RuntimeException("Unable To receive acknowledgement");
socket.close();

//while loop ending



}
catch(ApplicationException ae)
{
System.out.println("1 PR");
}
catch(IOException ie)
{
System.out.println("2 PR");
System.out.println(ie);
}
catch(ClassNotFoundException cnfe)
{
System.out.println("3 PR");
System.out.println(cnfe);
}
}
public Response processRequest(Request request)
{
try
{
if(request==null)
{
System.out.println("Null Request");
}
String path=request.getPath();
Object [] arguments=request.getArguments();
String clientID=request.getClientID();
Response response=new Response();
if(!services.containsKey(path))
{
response.setIsError(true);
response.setError("Error: Invalid Path \r\n");
}
Service service=services.get(path);
Method method=service.getMethod();
Module module=service.getModule();
Class serviceClass=module.getServiceClass();
Object o=serviceClass.newInstance();
LinkedList<Property> autoWiredProperties=module.getAutoWiredProperties();
if(service.getNumberOfParameters()!=arguments.length)
{
response.setIsError(true);
response.setError("Error: Invalid number of arguments provided of method with path: "+path);
return response;
}
response.setArguments(arguments);

Object result;
if(!service.getIsVoid())
{
result=method.invoke(o,arguments);
response.setResult(result);
}
else
{
method.invoke(o,arguments);
response.setIsVoid(true);
}
response.setIsSuccessful(true);
return response;
}
//catch(ApplicationException ae)
//{
//System.out.println("4 PR");
//}
catch(IllegalAccessException iae)
{
System.out.println("5 PR");
Response response=new Response();
response.setIsException(true);
response.setException(iae.toString());
return response;
}
catch(InstantiationException ise)
{
System.out.println("6 PR");
Response response=new Response();
response.setIsException(true);
response.setException(ise.toString());
return response;
}
catch(InvocationTargetException ite)
{
System.out.println("7 PR");
Response response=new Response();
response.setIsException(true);
response.setException(ite.toString());
return response;
}
//return response;
}
}