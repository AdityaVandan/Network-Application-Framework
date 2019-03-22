package com.thinking.machines.nafcommon;
public class Response implements java.io.Serializable
{
private Object[] arguments;
private Object result;
private String exception;
private String error;
private boolean isVoid;
private boolean isException;
private boolean isError;
private boolean isSuccessful;
public Response()
{
this.arguments=null;
this.result=null;
this.exception=null;
this.isVoid=false;
this.isException=false;
this.isError=false;
this.isSuccessful=false;
}
public void setArguments(Object[] arguments)
{
this.arguments=arguments;
}
public Object[] getArguments()
{
return this.arguments;
}
public void setResult(Object result)
{
this.result=result;
}
public Object getResult()
{
return this.result;
}
public void setException(String exception)
{
this.exception=exception;
}
public String getException()
{
return this.exception;
}
public void setError(String error)
{
this.error=error;
}
public String getError()
{
return this.error;
}
public void setIsVoid(boolean isVoid)
{
this.isVoid=isVoid;
}
public boolean getIsVoid()
{
return this.isVoid;
}
public void setIsException(boolean isException)
{
this.isException=isException;
}
public boolean getIsException()
{
return this.isException;
}
public void setIsError(boolean isError)
{
this.isError=isError;
}
public boolean getIsError()
{
return this.isError;
}
public void setIsSuccessful(boolean isSuccessful)
{
this.isSuccessful=isSuccessful;
}
public boolean getIsSuccessful()
{
return this.isSuccessful;
}
}