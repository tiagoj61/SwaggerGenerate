package swagger.automate;
import java.lang.Class;
import java.lang.reflect.*;
//define Person interface
interface Person {
   public void display();
}
 
//declare class Student that implements Person
class Student implements Person {
   //define interface method display
   public void display() {
       System.out.println("I am a Student");
   }
}
 
class Superclass {
   public static void main(String[] args) {
       try {
           // create an object of Student class
           Student s1 = new Student();
 
           // get Class object using getClass()
           Class obj = s1.getClass();
 
           // get the superclass of Student
           Class superClass = obj.getSuperclass();
           System.out.println("Superclass of Student Class: " + superClass.getName());
       }
 
       catch(Exception e) {
           e.printStackTrace();
       }
   }
}