/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mealplanning;

/**
 *
 * @author tridinh
 *

*/

import java.sql.*;
//import java.util.Scanner; 

public class MealPlanning
{
    private static Connection getMySQLConnection(String databaseName, String user, String password)
    {
        try
        {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + databaseName, user, password);
        } catch (SQLException exception)
        {
            System.out.println("Failed to connect to the database" + exception.getMessage());
            return null;
        }
    }
    
    public static void main(String[] args)
    {
        
        //user = root
        //pw = tr666666
        var userName = System.console().readLine("userName: ");
        var password = System.console().readLine("password: ");
       
       
        if (TryExecutingAStoredProcedure("MealPlanning", userName, password))
        {
            System.out.println("Successfully ran a stored procedure");
        } else
        {
            System.out.println("Failed to run a stored procedure");
        }
        
        if (planWeek("MealPlanning", userName, password))
        {
            System.out.println("Successfully ran a planWeek");
        } else
        {
            System.out.println("Failed to run a planWeek");
        }
        
        
    }
   
    public static boolean planWeek(String databaseName, String user, String password)
    {
        Connection myConnection = getMySQLConnection(databaseName, user, password);
        // make the table to hold the days and food for the day
        try 
        {
            CallableStatement starting = myConnection.prepareCall("{Call makeWeek()}");
            starting.executeQuery();
            //show week
            CallableStatement The_week = myConnection.prepareCall("{Call showWeek()}");
            ResultSet myResultss = The_week.executeQuery();

            // Iterate over the ResultSet, row by row
            while (myResultss.next())
            {
                String aDay = myResultss.getString("days");
                String food = myResultss.getString("thefood");

                System.out.println("" + aDay + ": " + food + "");
            }
        } catch (SQLException myException) 
        {
            System.out.println("Failed to execute stored procedure:" + myException.getMessage());
            return false;
        }
        
        //loop to fill in the week and check the cookbooks
        while(true) 
        {  
            try 
            {
                //fillout one day
                String day = System.console().readLine("Pick a day: ");
                String RecipeForTheDay = System.console().readLine("Pick a recipe: ");
                CallableStatement mSPC4 = myConnection.prepareCall("{Call FoodToDay(?,?)}");
                mSPC4.setString(1, day);
                mSPC4.setString(2, RecipeForTheDay);
                mSPC4.executeQuery();
                
                //show week
                CallableStatement The_week = myConnection.prepareCall("{Call showWeek()}");
                ResultSet myResultss = The_week.executeQuery();

                // Iterate over the ResultSet, row by row
                while (myResultss.next())
                {
                    String aDay = myResultss.getString("days");
                    String food = myResultss.getString("thefood");
                    System.out.println("" + aDay + ": " + food + "");
                }
            } catch (SQLException myException) 
            {
                System.out.println("Failed to execute stored procedure:" + myException.getMessage());
                return false;
            }
            
            //check if user wants to continue
            while(true)
            {
                String checking = System.console().readLine("Want to continue with the week(Yes/No): ");
                //if no then remove table and end program
                if (checking.equals("No"))
                {
                    try 
                    {
                        CallableStatement clean_slate = myConnection.prepareCall("{Call removeWeek()}");
                        clean_slate.executeQuery();
                        return true;
                    } catch (SQLException myException) 
                    {
                        System.out.println("Failed to execute stored procedure:" + myException.getMessage());
                        return false;
                    }
                    
                } 
                //if yes then ask if they want to check the cookbooks again or just continue on
                else if (checking.equals("Yes"))
                {
                    while(true)
                    {
                        String checkingBooks = System.console().readLine("Want to check the books again? (Yes/No): ");
                        if (checkingBooks.equals("No"))
                        {
                            break;
                        } 
                        else if (checkingBooks.equals("Yes"))
                        {
                            TryExecutingAStoredProcedure("MealPlanning", user, password);
                            break;
                        }
                        else
                        {
                            System.out.println("Enter either Yes or No");
                        }
                    }
                    break;
                }
                else
                {
                    System.out.println("Enter either Yes or No");
                }
            }
        }
    
    }
    public static boolean TryExecutingAStoredProcedure(String databaseName, String user, String password)
    {
        Connection myConnection = getMySQLConnection(databaseName, user, password);
        try
        {
            
            CallableStatement mSPC = myConnection.prepareCall("{Call getCookbooks()}");
            ResultSet mR = mSPC.executeQuery();
            System.out.println("list of available cookbooks: ");
            while (mR.next())
            {
                String myR = mR.getString("CookbookName");
                System.out.println(myR);
            }
            
            String book = System.console().readLine("enter a book: ");
            CallableStatement mSPC2 = myConnection.prepareCall("{Call getRecipesFromBook(?)}");
            mSPC2.setString(1,book);
            ResultSet mR2 = mSPC2.executeQuery();
            
            System.out.println("Recipes in "+ book +": ");
            while (mR2.next())
            {
                String myR2 = mR2.getString("RecipeName");
                System.out.println(myR2);
            }
            
            //enter recipe then show ingrediants
            String recipe = System.console().readLine("enter a recipe: ");       
            CallableStatement mSPC3 = myConnection.prepareCall("{Call getIngredientsFromRecipe(?)}");
            mSPC3.setString(1, recipe);
            ResultSet mR3 = mSPC3.executeQuery();
            
            
            System.out.println("Ingredents need in "+ recipe +": ");
            while (mR3.next())
                {
                   String s3Result = mR3.getString("IngredientName");
                   System.out.println(s3Result);
                }
           
           
        } catch (SQLException myException)
        {
            System.out.println("Failed to execute stored procedure:" + myException.getMessage());
            return false;
        }
        return true;
    }
}