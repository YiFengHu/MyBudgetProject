package com.dreamer.mybudget.core.db.data;

/**
 * Created by Roder Hu on 15/6/11.
 */
public enum CategoryType {
    Income, Expense;

    public static CategoryType getCategoryType(String categoryTypeString){
        if(Income.name().equals(categoryTypeString)){
            return Income;
        }else if(Expense.name().equals(categoryTypeString)){
            return Expense;
        }

        return null;
    }
}
