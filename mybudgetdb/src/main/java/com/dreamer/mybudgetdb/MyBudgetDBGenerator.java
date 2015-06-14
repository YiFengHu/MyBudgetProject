package com.dreamer.mybudgetdb;

import com.dreamer.mybudgetdb.info.CategoryTable;
import com.dreamer.mybudgetdb.info.DetailTable;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MyBudgetDBGenerator {

	public static String mainProjectPackageName = "com.dreamer.mybudget";
        public static final String OuputDir = "./MyBudget/src-gen/";

        public static void main(String args[]) throws Exception{
            Schema schema = new Schema(DBInfo.DBVersion, mainProjectPackageName);
            Entity categoryEntity = addCategoryTable(schema);
            Entity detailEntity = addDetailTable(schema, categoryEntity);

            new DaoGenerator().generateAll(schema, OuputDir);
        }

        private static Entity addDetailTable(Schema schema, Entity categoryEntity) {
            Entity detailEntity = schema.addEntity(DetailTable.TableName);
            detailEntity.addLongProperty(DetailTable.Did).primaryKey().autoincrement();
            detailEntity.addStringProperty(DetailTable.Io).notNull();
            detailEntity.addLongProperty(DetailTable.Time).notNull();
            detailEntity.addIntProperty(DetailTable.Price).notNull();
            Property category = detailEntity.addLongProperty(DetailTable.Category_cid).notNull().getProperty();
            detailEntity.addStringProperty(DetailTable.Mark);

            detailEntity.addToOne(categoryEntity ,category, "categoryName");
            return detailEntity;
        }

        private static Entity addCategoryTable(Schema schema) {
            Entity detailEntity = schema.addEntity(CategoryTable.TableName);
            detailEntity.addLongProperty(CategoryTable.Cid).primaryKey().autoincrement();
            detailEntity.addStringProperty(CategoryTable.CategoryName).unique().notNull();
            detailEntity.addStringProperty(CategoryTable.CategoryType).notNull();

            return detailEntity;
        }
}
