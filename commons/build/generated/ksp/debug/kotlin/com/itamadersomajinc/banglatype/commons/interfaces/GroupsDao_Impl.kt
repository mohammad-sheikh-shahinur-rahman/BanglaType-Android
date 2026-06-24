package com.itamadersomajinc.banglatype.commons.interfaces

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performBlocking
import androidx.sqlite.SQLiteStatement
import com.itamadersomajinc.banglatype.commons.models.contacts.Group
import javax.`annotation`.processing.Generated
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class GroupsDao_Impl(
  __db: RoomDatabase,
) : GroupsDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfGroup: EntityInsertAdapter<Group>
  init {
    this.__db = __db
    this.__insertAdapterOfGroup = object : EntityInsertAdapter<Group>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `groups` (`id`,`title`,`contacts_count`) VALUES (?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Group) {
        val _tmpId: Long? = entity.id
        if (_tmpId == null) {
          statement.bindNull(1)
        } else {
          statement.bindLong(1, _tmpId)
        }
        statement.bindText(2, entity.title)
        statement.bindLong(3, entity.contactsCount.toLong())
      }
    }
  }

  public override fun insertOrUpdate(group: Group): Long = performBlocking(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfGroup.insertAndReturnId(_connection, group)
    _result
  }

  public override fun getGroups(): List<Group> {
    val _sql: String = "SELECT * FROM groups"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfContactsCount: Int = getColumnIndexOrThrow(_stmt, "contacts_count")
        val _result: MutableList<Group> = mutableListOf()
        while (_stmt.step()) {
          val _item: Group
          val _tmpId: Long?
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null
          } else {
            _tmpId = _stmt.getLong(_columnIndexOfId)
          }
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpContactsCount: Int
          _tmpContactsCount = _stmt.getLong(_columnIndexOfContactsCount).toInt()
          _item = Group(_tmpId,_tmpTitle,_tmpContactsCount)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun deleteGroupId(id: Long) {
    val _sql: String = "DELETE FROM groups WHERE id = ?"
    return performBlocking(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id)
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
