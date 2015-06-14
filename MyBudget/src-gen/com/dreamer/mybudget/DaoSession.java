package com.dreamer.mybudget;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.dreamer.mybudget.Category;
import com.dreamer.mybudget.Detail;

import com.dreamer.mybudget.CategoryDao;
import com.dreamer.mybudget.DetailDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig categoryDaoConfig;
    private final DaoConfig detailDaoConfig;

    private final CategoryDao categoryDao;
    private final DetailDao detailDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        categoryDaoConfig = daoConfigMap.get(CategoryDao.class).clone();
        categoryDaoConfig.initIdentityScope(type);

        detailDaoConfig = daoConfigMap.get(DetailDao.class).clone();
        detailDaoConfig.initIdentityScope(type);

        categoryDao = new CategoryDao(categoryDaoConfig, this);
        detailDao = new DetailDao(detailDaoConfig, this);

        registerDao(Category.class, categoryDao);
        registerDao(Detail.class, detailDao);
    }
    
    public void clear() {
        categoryDaoConfig.getIdentityScope().clear();
        detailDaoConfig.getIdentityScope().clear();
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public DetailDao getDetailDao() {
        return detailDao;
    }

}
