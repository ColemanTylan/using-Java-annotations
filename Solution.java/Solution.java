import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.*;

//This makes custom annotation 'FamilyBudget' to be applied to only methods
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface FamilyBudget {
    String userRole() default "guest";
    // represents limit assigned to each member
    int budgetLimit();
}

//added FamilyBudget annotation to satisfy isAnnotationPresent if statements
class FamilyMember {
    //Creating Senior Member method
    @FamilyBudget(userRole = "senior", budgetLimit = 20)
    public void seniorMember(int budget, int moneySpend) {
        System.out.println("Senior Member");
        System.out.println("Spend: " + moneySpend);
        System.out.println("Budget Left: " + (budget - moneySpend));
    }
    //Creating Junior Member method
    @FamilyBudget(userRole = "junior", budgetLimit = 10)
    public void juniorUser(int budget, int moneySpend) {
        System.out.println("Junior Member");
        System.out.println("Spend: " + moneySpend);
        System.out.println("Budget Left: " + (budget - moneySpend));
    }
}

public class Solution {

    public static void main(String[] args) {
        //Storing Input Stream for method use on 'in' variable
        Scanner in = new Scanner(System.in);
        //Turns the input collected into integers and prompts end user input via terminal using testCases int var
        int testCases = Integer.parseInt(in.nextLine());
        //While input collected (integers) more than 0 run method
        while (testCases > 0) {
            //Prompts terminal to collect next token this is String
            String role = in.next();
            //Prompts terminal to next token as integers
            int spend = in.nextInt();
            try {
                //Ensures annotatedClass only represents FamilyMember class object providing safety at compile time
                //annotatedClass var holds metaData from FamilyMember class
                Class<FamilyMember> annotatedClass = FamilyMember.class;
                //methods var holds returned array of public annotatedClass methods using getMethods()
                Method[] methods = annotatedClass.getMethods();//fetches all public methods
                //This for-each loop iterates over each method in methods array
                for (Method method : methods) {
                    // If iteration has annotation present on method in FamilyMember class
                    if (method.isAnnotationPresent(FamilyBudget.class)) {
                        //Create annotation var family
                        FamilyBudget family = method
                                //the iteration gets the annotation on the method
                                .getAnnotation(FamilyBudget.class);
                        // Create userRole var to hold string value on current iteration of method annotation
                        String userRole = family.userRole();
                        // holds int value assigned to current iteration of method annotation
                        int budgetLimit = family.budgetLimit();
                        //If userRole string ('senior', 'junior', or 'guest' equals first terminal prompt token)
                        if (userRole.equals(role)) {
                            // if 2nd terminal prompt int less than budgetLimit on annotation
                            if(spend<=budgetLimit){
                                //Trigger current iteration method
                                method.invoke(FamilyMember.class.newInstance(),
                                        budgetLimit, spend);
                            }else{
                                //self-explanatory lol
                                System.out.println("Budget Limit Over");
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            testCases--;
        }
    }
}
