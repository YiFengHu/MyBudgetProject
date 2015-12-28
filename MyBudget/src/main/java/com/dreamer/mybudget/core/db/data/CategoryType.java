package com.dreamer.mybudget.core.db.data;

import com.dreamer.mybudget.R;

/**
 * Created by Roder Hu on 15/6/11.
 */
public enum CategoryType {
    Income("income", R.string.detail_type_category_income),
    Expense("expense", R.string.detail_type_category_expense);

    private int typeNameRes;
    private String typeDBName;

    CategoryType(String typeDBName, int typeNameRes){
        this.typeDBName = typeDBName;
        this.typeNameRes = typeNameRes;
    }

//    public static CategoryType getCategoryType(String categoryTypeString){
//        if(Income.name().equals(categoryTypeString)){
//            return Income;
//        }else if(Expense.name().equals(categoryTypeString)){
//            return Expense;
//        }
//
//        return null;
//    }

    public static CategoryType getCategoryType(String categoryTypeDBName){
        if(Income.getTypeDBName().equals(categoryTypeDBName)){
            return Income;
        }else if(Expense.getTypeDBName().equals(categoryTypeDBName)){
            return Expense;
        }

        return null;
    }

    public int getTypeNameRes() {
        return typeNameRes;
    }

    public String getTypeDBName() {
        return typeDBName;
    }
}
