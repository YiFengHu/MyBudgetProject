package com.dreamer.mybudget.core.db.data;


import com.dreamer.mybudget.R;


/**
 * Created by Roder Hu on 15/6/14.
 */
public enum DefaultCategory {
    INCOME_SALARY("income_salary", R.string.category_income_salary),
    INCOME_POCKET_MONEY("income_pocket_money", R.string.category_income_pocket_money),
    INCOME_BONUS("income_bonus", R.string.category_income_bonus),
    INCOME_OTHER("income_other", R.string.category_income_other),
    EXPENSE_FOOD("expense_food", R.string.category_expense_food),
    EXPENSE_CLOTHING("expense_clothing", R.string.category_expense_clothing),
    EXPENSE_HOUSEHOLD("expense_household", R.string.category_expense_household),
    EXPENSE_TRANSPORTATION("expense_transportation", R.string.category_expense_transportation),
    EXPENSE_LEISURE("expense_leisure", R.string.category_expense_leisure),
    EXPENSE_RENT("expense_rent", R.string.category_expense_rent),
    EXPENSE_SELF_DEVELOPMENT("expense_self_development", R.string.category_expense_self_development),
    EXPENSE_OTHER("expense_other", R.string.category_expense_other);

    int categoryNameRes;
    String dbName;

    DefaultCategory(String dbName, int nameRes) {
        categoryNameRes = nameRes;
        this.dbName = dbName;
    }

    public int getCategoryNameRes() {
        return categoryNameRes;
    }

    public String getDbName() {
        return dbName;
    }

    public static DefaultCategory getDefaultCategory(String categoryDBName) {
        for (DefaultCategory category :values()) {
            if(category.getDbName().equals(categoryDBName)){
                return category;
            }
        }

        return null;

    }
}
