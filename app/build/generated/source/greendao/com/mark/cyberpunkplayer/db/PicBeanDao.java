package com.mark.cyberpunkplayer.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "PIC_BEAN".
*/
public class PicBeanDao extends AbstractDao<PicBean, String> {

    public static final String TABLENAME = "PIC_BEAN";

    /**
     * Properties of entity PicBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Path = new Property(0, String.class, "path", true, "PATH");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Format = new Property(2, int.class, "format", false, "FORMAT");
        public final static Property Size = new Property(3, long.class, "size", false, "SIZE");
        public final static Property Width = new Property(4, int.class, "width", false, "WIDTH");
        public final static Property Height = new Property(5, int.class, "height", false, "HEIGHT");
    }


    public PicBeanDao(DaoConfig config) {
        super(config);
    }
    
    public PicBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PIC_BEAN\" (" + //
                "\"PATH\" TEXT PRIMARY KEY NOT NULL UNIQUE ," + // 0: path
                "\"NAME\" TEXT," + // 1: name
                "\"FORMAT\" INTEGER NOT NULL ," + // 2: format
                "\"SIZE\" INTEGER NOT NULL ," + // 3: size
                "\"WIDTH\" INTEGER NOT NULL ," + // 4: width
                "\"HEIGHT\" INTEGER NOT NULL );"); // 5: height
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PIC_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, PicBean entity) {
        stmt.clearBindings();
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(1, path);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        stmt.bindLong(3, entity.getFormat());
        stmt.bindLong(4, entity.getSize());
        stmt.bindLong(5, entity.getWidth());
        stmt.bindLong(6, entity.getHeight());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, PicBean entity) {
        stmt.clearBindings();
 
        String path = entity.getPath();
        if (path != null) {
            stmt.bindString(1, path);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
        stmt.bindLong(3, entity.getFormat());
        stmt.bindLong(4, entity.getSize());
        stmt.bindLong(5, entity.getWidth());
        stmt.bindLong(6, entity.getHeight());
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public PicBean readEntity(Cursor cursor, int offset) {
        PicBean entity = new PicBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // path
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.getInt(offset + 2), // format
            cursor.getLong(offset + 3), // size
            cursor.getInt(offset + 4), // width
            cursor.getInt(offset + 5) // height
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, PicBean entity, int offset) {
        entity.setPath(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFormat(cursor.getInt(offset + 2));
        entity.setSize(cursor.getLong(offset + 3));
        entity.setWidth(cursor.getInt(offset + 4));
        entity.setHeight(cursor.getInt(offset + 5));
     }
    
    @Override
    protected final String updateKeyAfterInsert(PicBean entity, long rowId) {
        return entity.getPath();
    }
    
    @Override
    public String getKey(PicBean entity) {
        if(entity != null) {
            return entity.getPath();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(PicBean entity) {
        return entity.getPath() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}