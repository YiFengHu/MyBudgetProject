package com.dreamer.mybudget.core.db;

import android.content.Context;

import com.dreamer.mybudget.Category;
import com.dreamer.mybudget.CategoryDao;
import com.dreamer.mybudget.DaoSession;
import com.dreamer.mybudget.core.db.data.CategoryType;
import com.dreamer.mybudget.core.db.data.DefaultCategory;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Roder Hu on 15/6/11.
 */
public class CategoryDBHandler {

    private static String TAG = CategoryDBHandler.class.getSimpleName();
    private String[] defaultExpenseCategories = null;
    private String[] defaultIncomeCategories = null;

    private Context context = null;
    private CategoryDao categoryDao = null;

    public CategoryDBHandler(Context context, DaoSession daoSession){
        this.context = context;
        this.categoryDao = daoSession.getCategoryDao();
        DefaultCategory.init(context);
        insertDefaultCategories();
    }

    private void insertDefaultCategories() {
        int categoryCount = 0;
        List<Category> allCategories = categoryDao.loadAll();
        if(allCategories!=null){
            categoryCount = allCategories.size();
        }

        if(categoryCount==0) {
            defaultExpenseCategories =
                    new String[]{DefaultCategory.EXPENSE_FOOD, DefaultCategory.EXPENSE_CLOTHING,
                            DefaultCategory.EXPENSE_HOUSEHOLD, DefaultCategory.EXPENSE_LEISURE,
                            DefaultCategory.EXPENSE_TRANSPORTATION, DefaultCategory.EXPENSE_SELF_DEVELOPMENT,
                            DefaultCategory.EXPENSE_RENT, DefaultCategory.EXPENSE_OTHER};

            defaultIncomeCategories =
                    new String[]{DefaultCategory.INCOME_SALARY, DefaultCategory.INCOME_BONUS,
                            DefaultCategory.INCOME_POCKET_MONEY, DefaultCategory.INCOME_OTHER};

            for (int i = 0; i < defaultExpenseCategories.length; i++) {
                Category category = new Category();
                category.setCategory_name(defaultExpenseCategories[i]);
                category.setCategory_type(CategoryType.Expense.name());
                insertCategory(category);
            }

            for (int i = 0; i < defaultIncomeCategories.length; i++) {
                Category category = new Category();
                category.setCategory_name(defaultIncomeCategories[i]);
                category.setCategory_type(CategoryType.Income.name());
                insertCategory(category);
            }
        }
    }


    public long insertCategory(Category category){
        if(category!=null) {
            return categoryDao.insert(category);
        }
        return -1;
    }

    public Category queryCategory(String categoryName){
        QueryBuilder<Category> queryBuilder = categoryDao.queryBuilder();
        queryBuilder.where(CategoryDao.Properties.Category_name.eq(categoryName));
        return queryBuilder.unique();
    }

    public List<Category> queryCategories(String categoryType){
        QueryBuilder<Category> queryBuilder = categoryDao.queryBuilder();
        queryBuilder.where(CategoryDao.Properties.Category_type.eq(categoryType));
        List<Category> result = queryBuilder.list();
        return result == null? new ArrayList<Category>(): result;
    }
}
