package com.dreamer.mybudget.db.data;

import android.content.Context;

import com.dreamer.mybudget.R;

/**
 * Created by Roder Hu on 15/6/14.
 */
public class DefaultCategory {

    private DefaultCategory(){}

    public static String INCOME_SALARY = null;
    public static String INCOME_POCKET_MONEY = null;
    public static String INCOME_BONUS = null;
    public static String INCOME_OTHER = null;

    public static String EXPENSE_FOOD = null;
    public static String EXPENSE_CLOTHING = null;
    public static String EXPENSE_HOUSEHOLD = null;
    public static String EXPENSE_TRANSPORTATION = null;
    public static String EXPENSE_LEISURE = null;
    public static String EXPENSE_RENT = null;
    public static String EXPENSE_SELF_DEVELOPMENT = null;
    public static String EXPENSE_OTHER = null;

    public static void init(Context context){
        INCOME_SALARY = context.getResources().getString(R.string.category_income_salary);
        INCOME_POCKET_MONEY = context.getResources().getString(R.string.category_income_pocket_money);
        INCOME_BONUS = context.getResources().getString(R.string.category_income_bonus);
        INCOME_OTHER = context.getResources().getString(R.string.category_income_other);

        EXPENSE_FOOD = context.getResources().getString(R.string.category_expense_food);
        EXPENSE_CLOTHING = context.getResources().getString(R.string.category_expense_clothing);
        EXPENSE_HOUSEHOLD = context.getResources().getString(R.string.category_expense_household);
        EXPENSE_TRANSPORTATION = context.getResources().getString(R.string.category_expense_transportation);
        EXPENSE_LEISURE = context.getResources().getString(R.string.category_expense_leisure);
        EXPENSE_RENT = context.getResources().getString(R.string.category_expense_rent);
        EXPENSE_SELF_DEVELOPMENT = context.getResources().getString(R.string.category_expense_self_development);
        EXPENSE_OTHER = context.getResources().getString(R.string.category_expense_other);
    }
}
