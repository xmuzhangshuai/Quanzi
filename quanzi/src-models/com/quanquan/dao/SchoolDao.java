package com.quanquan.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.quanquan.entities.City;
import com.quanquan.entities.Cometent;

import com.quanquan.entities.School;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SCHOOL.
*/
public class SchoolDao extends AbstractDao<School, Long> {

    public static final String TABLENAME = "SCHOOL";

    /**
     * Properties of entity School.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property SchoolName = new Property(1, String.class, "schoolName", false, "SCHOOL_NAME");
        public final static Property SchoolInfo = new Property(2, String.class, "schoolInfo", false, "SCHOOL_INFO");
        public final static Property CometentID = new Property(3, long.class, "cometentID", false, "COMETENT_ID");
        public final static Property CityID = new Property(4, long.class, "cityID", false, "CITY_ID");
    };

    private DaoSession daoSession;

    private Query<School> cometent_SchoolListQuery;
    private Query<School> city_SchoolListQuery;

    public SchoolDao(DaoConfig config) {
        super(config);
    }
    
    public SchoolDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SCHOOL' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'SCHOOL_NAME' TEXT," + // 1: schoolName
                "'SCHOOL_INFO' TEXT," + // 2: schoolInfo
                "'COMETENT_ID' INTEGER NOT NULL ," + // 3: cometentID
                "'CITY_ID' INTEGER NOT NULL );"); // 4: cityID
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SCHOOL'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, School entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String schoolName = entity.getSchoolName();
        if (schoolName != null) {
            stmt.bindString(2, schoolName);
        }
 
        String schoolInfo = entity.getSchoolInfo();
        if (schoolInfo != null) {
            stmt.bindString(3, schoolInfo);
        }
        stmt.bindLong(4, entity.getCometentID());
        stmt.bindLong(5, entity.getCityID());
    }

    @Override
    protected void attachEntity(School entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public School readEntity(Cursor cursor, int offset) {
        School entity = new School( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // schoolName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // schoolInfo
            cursor.getLong(offset + 3), // cometentID
            cursor.getLong(offset + 4) // cityID
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, School entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setSchoolName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setSchoolInfo(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCometentID(cursor.getLong(offset + 3));
        entity.setCityID(cursor.getLong(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(School entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(School entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "schoolList" to-many relationship of Cometent. */
    public List<School> _queryCometent_SchoolList(long cometentID) {
        synchronized (this) {
            if (cometent_SchoolListQuery == null) {
                QueryBuilder<School> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.CometentID.eq(null));
                cometent_SchoolListQuery = queryBuilder.build();
            }
        }
        Query<School> query = cometent_SchoolListQuery.forCurrentThread();
        query.setParameter(0, cometentID);
        return query.list();
    }

    /** Internal query to resolve the "schoolList" to-many relationship of City. */
    public List<School> _queryCity_SchoolList(long cityID) {
        synchronized (this) {
            if (city_SchoolListQuery == null) {
                QueryBuilder<School> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.CityID.eq(null));
                city_SchoolListQuery = queryBuilder.build();
            }
        }
        Query<School> query = city_SchoolListQuery.forCurrentThread();
        query.setParameter(0, cityID);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getCometentDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getCityDao().getAllColumns());
            builder.append(" FROM SCHOOL T");
            builder.append(" LEFT JOIN COMETENT T0 ON T.'COMETENT_ID'=T0.'_id'");
            builder.append(" LEFT JOIN CITY T1 ON T.'CITY_ID'=T1.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected School loadCurrentDeep(Cursor cursor, boolean lock) {
        School entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Cometent cometent = loadCurrentOther(daoSession.getCometentDao(), cursor, offset);
         if(cometent != null) {
            entity.setCometent(cometent);
        }
        offset += daoSession.getCometentDao().getAllColumns().length;

        City city = loadCurrentOther(daoSession.getCityDao(), cursor, offset);
         if(city != null) {
            entity.setCity(city);
        }

        return entity;    
    }

    public School loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<School> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<School> list = new ArrayList<School>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<School> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<School> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
