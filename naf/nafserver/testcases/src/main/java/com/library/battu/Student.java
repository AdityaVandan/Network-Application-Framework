package com.library.battu;
import com.thinking.machines.nafserver.annotation.*;
class Student
{
@AutoWired
private int rollNumber;
@AutoWired
private String name;
@ApplicationScope
public void setRollNumber(int rollNumber)
{
this.rollNumber=rollNumber;
}
@ApplicationScope
public int getRollNumber()
{
return this.rollNumber;
}
@ApplicationScope
public void setName(String name)
{
this.name=name;
}
@ApplicationScope
public String getName()
{
return this.name;
}
}