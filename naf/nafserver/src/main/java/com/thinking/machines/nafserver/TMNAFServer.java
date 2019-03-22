package com.thinking.machines.nafserver;
import com.thinking.machines.nafserver.tool.*;
import com.thinking.machines.nafserver.model.*;
import java.net.*;
public class TMNAFServer
{
private Application application;
public TMNAFServer(int portNumber)
{
initialize();
startServer(portNumber);
}
public void startServer(int portNumber)
{
try
{
ServerSocket server=new ServerSocket(portNumber);
Socket socket;
ProcessRequest pr;
while(true)
{
System.out.println("Server is ready and is in listening mode");
socket=server.accept();
System.out.println("Request Arrived");
pr=new ProcessRequest(socket,this.application);
}
}catch(Throwable e)
{
System.out.println("1 TS");
System.out.println(e);
}

}// starting server ends


public void initialize()
{
this.application=ApplicationUtility.getApplication();
}
}