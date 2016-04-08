package cn.getdone.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import cn.getdone.dao.HistoryTask;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table HISTORY_TASK.
*/
public class HistoryTaskDao extends AbstractDao<HistoryTask, Long> {

    public static final String TABLENAME = "HISTORY_TASK";

    /**
     * Properties of entity HistoryTask.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Priority = new Property(2, int.class, "priority", false, "PRIORITY");
        public final static Property CreateTime = new Property(3, java.util.Date.class, "createTime", false, "CREATE_TIME");
        public final static Property ExcuteTime = new Property(4, java.util.Date.class, "excuteTime", false, "EXCUTE_TIME");
        public final static Property FriendId = new Property(5, Long.class, "friendId", false, "FRIEND_ID");
    };


    public HistoryTaskDao(DaoConfig config) {
        super(config);
    }
    
    public HistoryTaskDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'HISTORY_TASK' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'TITLE' TEXT NOT NULL ," + // 1: title
                "'PRIORITY' INTEGER NOT NULL ," + // 2: priority
                "'CREATE_TIME' INTEGER NOT NULL ," + // 3: createTime
                "'EXCUTE_TIME' INTEGER," + // 4: excuteTime
                "'FRIEND_ID' INTEGER);"); // 5: friendId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'HISTORY_TASK'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, HistoryTask entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getTitle());
        stmt.bindLong(3, entity.getPriority());
        stmt.bindLong(4, entity.getCreateTime().getTime());
 
        java.util.Date excuteTime = entity.getExcuteTime();
        if (excuteTime != null) {
            stmt.bindLong(5, excuteTime.getTime());
        }
 
        Long friendId = entity.getFriendId();
        if (friendId != null) {
            stmt.bindLong(6, friendId);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public HistoryTask readEntity(Cursor cursor, int offset) {
        HistoryTask entity = new HistoryTask( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // title
            cursor.getInt(offset + 2), // priority
            new java.util.Date(cursor.getLong(offset + 3)), // createTime
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // excuteTime
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5) // friendId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, HistoryTask entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.getString(offset + 1));
        entity.setPriority(cursor.getInt(offset + 2));
        entity.setCreateTime(new java.util.Date(cursor.getLong(offset + 3)));
        entity.setExcuteTime(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setFriendId(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(HistoryTask entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(HistoryTask entity) {
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
    
}