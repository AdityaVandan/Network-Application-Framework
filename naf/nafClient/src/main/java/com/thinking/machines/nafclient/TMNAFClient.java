package com.thinking.machines.nafclient;
import com.thinking.machines.tool.*;
import com.thinking.machines.nafcommon.*;
import java.net.*;
import java.lang.reflect.*;
import java.io.*;
public class TMNAFClient
{
String hostName;
int portNumber;
Socket client;
public TMNAFClient(String hostName,int portNumber)
{
this.hostName=hostName;
this.portNumber=portNumber;
//initialize(s,portNumber);
}
/*
public void initialize(String s,int portNumber)
{
try
{
client=new Socket(s,portNumber);
}catch(Throwable th)
{
System.out.println("1 TC");
System.out.println(th);
}
}
*/
public Object process(String pathValue,Object ...inputs) throws ApplicationException
{
Request request=new Request();
request.setPath(pathValue);
request.setArguments(inputs);
try
{
//change
client=new Socket(hostName,portNumber);
//change
ByteArrayOutputStream baos=new ByteArrayOutputStream();
ObjectOutputStream oos=new ObjectOutputStream(baos);
oos.writeObject(request);
oos.flush();
byte requestBytes[]=baos.toByteArray();
int requestSize=requestBytes.length;
byte requestSizeInBytes[]=new byte[4];
requestSizeInBytes[0]=(byte)(requestSize >>24);
requestSizeInBytes[1]=(byte)(requestSize >>16);
requestSizeInBytes[2]=(byte)(requestSize >>8);
requestSizeInBytes[3]=(byte)(requestSize);
//Initialization of Client Socket
OutputStream os=client.getOutputStream();
os.write(requestSizeInBytes,0,4);
os.flush();
InputStream is=client.getInputStream();
byte ack[]=new byte[1];
int byteCount=is.read(ack);
if(ack[0]!=79) throw new RuntimeException("Unable to receive acknowledgement");
int bytesToSend=requestSize;
int i=0;
int chunkSize=1024;
while(bytesToSend>0)
{
if(bytesToSend<chunkSize) chunkSize=bytesToSend;
os.write(requestBytes,i,chunkSize);
os.flush();
i+=chunkSize;
bytesToSend-=chunkSize;
}
byteCount=is.read(ack);
if(ack[0]!=79) throw new RuntimeException("Unable to receive acknowledgement");
byte responseLengthInBytes[]=new byte[4];
byteCount=is.read(responseLengthInBytes);
int responseLength=((responseLengthInBytes[0] & 0xFF) <<24 | (responseLengthInBytes[1] & 0xFF) <<16 | (responseLengthInBytes[2] & 0xFF) <<8 | (responseLengthInBytes[3] & 0xFF));
ack[0]=79;
os.write(ack,0,1);
os.flush();
baos=new ByteArrayOutputStream();
// Client2.java-> resume from getting response (OR line 56)
byte chunk[]=new byte[1024];
int bytesToRead=responseLength;
while(bytesToRead>0)
{
byteCount=is.read(chunk);
if(byteCount>0)
{
baos.write(chunk,0,byteCount);
baos.flush();
}
bytesToRead-=byteCount;
}
os.write(ack,0,1);
os.flush();
byte responseBytes[]=baos.toByteArray();
ByteArrayInputStream bais=new ByteArrayInputStream(responseBytes);
ObjectInputStream ois=new ObjectInputStream(bais);
Response response=(Response)ois.readObject();
Object result;
client.close();  //doubtful whether socket should get closed here
//new
if(response!=null)
{
Object []modifiedArgs=response.getArguments();
for(int w=0;w<modifiedArgs.length;w++)
{
Utility.copyObject(inputs[w],modifiedArgs[w]);
}
}
if(response.getIsSuccessful())
{
result=response.getResult();
return result;
}
if(response.getIsError())
{
System.out.println("Response has an Error");
throw new ApplicationException(response.getError());
}
if(response.getIsException())
{
System.out.println("Response has an Exception");
throw new ApplicationException(response.getException());
}
//result=this.processResponse(response);
return null;
}catch(Throwable th)
{
System.out.println("2 TC");
System.out.println(th);
throw new ApplicationException(th.getMessage());
//return null;
}

//return null;
}//process method ends
public Object processResponse(Response response)
{
Object result;
if(response.getIsError())
{
System.out.println("Response is Error: "+response.getIsError());
throw new RuntimeException("Error: "+response.getError());
}
if(response.getIsException())
{
System.out.println("Response is Exception: "+response.getIsException());

throw new RuntimeException("Exception: "+response.getException());
}
if(response.getIsSuccessful())
{
System.out.println("Response is Successful: "+response.getIsSuccessful());
result=response.getResult();
System.out.println(result);
return result;
}
else
{
throw new RuntimeException("Did not recieve appropriate response");
}


}
}