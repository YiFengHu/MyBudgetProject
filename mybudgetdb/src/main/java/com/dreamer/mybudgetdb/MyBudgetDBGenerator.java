package com.dreamer.mybudgetdb;

import com.dreamer.mybudgetdb.info.DetailTable;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyBudgetDBGenerator {

	public static String mainProjectPackageName = "com.dreamer.mybudget";
        public static final String OuputDir = "./MyBudget/src-gen/";

        public static void main(String args[]) throws Exception{
            Schema schema = new Schema(DBInfo.DBVersion, mainProjectPackageName);
            Entity detailEntity = addDetailTable(schema);
            new DaoGenerator().generateAll(schema, OuputDir);
        }

        private static Entity addDetailTable(Schema schema) {
            Entity detailEntity = schema.addEntity(DetailTable.TableName);
            detailEntity.addLongProperty(DetailTable.Did).primaryKey().autoincrement();
            detailEntity.addStringProperty(DetailTable.Io).notNull();
            detailEntity.addLongProperty(DetailTable.Time).notNull();
            detailEntity.addIntProperty(DetailTable.Price).notNull();
            detailEntity.addStringProperty(DetailTable.Type).notNull();
            detailEntity.addStringProperty(DetailTable.Mark);

            return detailEntity;
        }
}
