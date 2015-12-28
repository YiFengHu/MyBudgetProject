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
                    new String[]{DefaultCategory.EXPENSE_FOOD.getDbName(),
                            DefaultCategory.EXPENSE_CLOTHING.getDbName(),
                            DefaultCategory.EXPENSE_HOUSEHOLD.getDbName(),
                            DefaultCategory.EXPENSE_LEISURE.getDbName(),
                            DefaultCategory.EXPENSE_TRANSPORTATION.getDbName(),
                            DefaultCategory.EXPENSE_SELF_DEVELOPMENT.getDbName(),
                            DefaultCategory.EXPENSE_RENT.getDbName(),
                            DefaultCategory.EXPENSE_OTHER.getDbName()};

            defaultIncomeCategories =
                    new String[]{DefaultCategory.INCOME_SALARY.getDbName(),
                            DefaultCategory.INCOME_BONUS.getDbName(),
                            DefaultCategory.INCOME_POCKET_MONEY.getDbName(),
                            DefaultCategory.INCOME_OTHER.getDbName()};
            Category category;
            for (int i = 0; i < defaultExpenseCategories.length; i++) {
                category = new Category();
                category.setCategory_name(defaultExpenseCategories[i]);
                category.setCategory_type(CategoryType.Expense.getTypeDBName());
                insertCategory(category);
            }

            for (int i = 0; i < defaultIncomeCategories.length; i++) {
                category = new Category();
                category.setCategory_name(defaultIncomeCategories[i]);
                category.setCategory_type(CategoryType.Income.getTypeDBName());
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

    public Category queryCategory(CategoryType categoryType, String catecoryDBName){
        QueryBuilder<Category> queryBuilder = categoryDao.queryBuilder();
        queryBuilder.where(CategoryDao.Properties.Category_name.eq(catecoryDBName));
        queryBuilder.where(CategoryDao.Properties.Category_type.eq(categoryType.getTypeDBName()));

        return queryBuilder.unique();
    }

    public List<Category> queryCategories(CategoryType categoryType){
        QueryBuilder<Category> queryBuilder = categoryDao.queryBuilder();
        queryBuilder.where(CategoryDao.Properties.Category_type.eq(categoryType.getTypeDBName()));
        List<Category> result = queryBuilder.list();
        return result == null? new ArrayList<Category>(): result;
    }
}
