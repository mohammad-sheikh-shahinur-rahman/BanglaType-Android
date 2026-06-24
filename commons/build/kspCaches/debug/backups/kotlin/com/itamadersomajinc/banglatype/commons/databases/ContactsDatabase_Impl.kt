package com.itamadersomajinc.banglatype.commons.databases

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.itamadersomajinc.banglatype.commons.interfaces.ContactsDao
import com.itamadersomajinc.banglatype.commons.interfaces.ContactsDao_Impl
import com.itamadersomajinc.banglatype.commons.interfaces.GroupsDao
import com.itamadersomajinc.banglatype.commons.interfaces.GroupsDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ContactsDatabase_Impl : ContactsDatabase() {
  private val _contactsDao: Lazy<ContactsDao> = lazy {
    ContactsDao_Impl(this)
  }

  private val _groupsDao: Lazy<GroupsDao> = lazy {
    GroupsDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(3, "a37ad6b27306d974626c808d21c72186", "23cf23e4c1764e7c663df2b9a36fc2e6") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `contacts` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `prefix` TEXT NOT NULL, `first_name` TEXT NOT NULL, `middle_name` TEXT NOT NULL, `surname` TEXT NOT NULL, `suffix` TEXT NOT NULL, `nickname` TEXT NOT NULL, `photo` BLOB, `photo_uri` TEXT NOT NULL, `phone_numbers` TEXT NOT NULL, `emails` TEXT NOT NULL, `events` TEXT NOT NULL, `starred` INTEGER NOT NULL, `addresses` TEXT NOT NULL, `notes` TEXT NOT NULL, `groups` TEXT NOT NULL, `company` TEXT NOT NULL, `job_position` TEXT NOT NULL, `websites` TEXT NOT NULL, `ims` TEXT NOT NULL, `ringtone` TEXT)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_contacts_id` ON `contacts` (`id`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `groups` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `title` TEXT NOT NULL, `contacts_count` INTEGER NOT NULL)")
        connection.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_groups_id` ON `groups` (`id`)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'a37ad6b27306d974626c808d21c72186')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `contacts`")
        connection.execSQL("DROP TABLE IF EXISTS `groups`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection): RoomOpenDelegate.ValidationResult {
        val _columnsContacts: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsContacts.put("id", TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("prefix", TableInfo.Column("prefix", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("first_name", TableInfo.Column("first_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("middle_name", TableInfo.Column("middle_name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("surname", TableInfo.Column("surname", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("suffix", TableInfo.Column("suffix", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("nickname", TableInfo.Column("nickname", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("photo", TableInfo.Column("photo", "BLOB", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("photo_uri", TableInfo.Column("photo_uri", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("phone_numbers", TableInfo.Column("phone_numbers", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("emails", TableInfo.Column("emails", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("events", TableInfo.Column("events", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("starred", TableInfo.Column("starred", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("addresses", TableInfo.Column("addresses", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("notes", TableInfo.Column("notes", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("groups", TableInfo.Column("groups", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("company", TableInfo.Column("company", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("job_position", TableInfo.Column("job_position", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("websites", TableInfo.Column("websites", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("ims", TableInfo.Column("ims", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsContacts.put("ringtone", TableInfo.Column("ringtone", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysContacts: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesContacts: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesContacts.add(TableInfo.Index("index_contacts_id", true, listOf("id"), listOf("ASC")))
        val _infoContacts: TableInfo = TableInfo("contacts", _columnsContacts, _foreignKeysContacts, _indicesContacts)
        val _existingContacts: TableInfo = read(connection, "contacts")
        if (!_infoContacts.equals(_existingContacts)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |contacts(com.itamadersomajinc.banglatype.commons.models.contacts.LocalContact).
              | Expected:
              |""".trimMargin() + _infoContacts + """
              |
              | Found:
              |""".trimMargin() + _existingContacts)
        }
        val _columnsGroups: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsGroups.put("id", TableInfo.Column("id", "INTEGER", false, 1, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGroups.put("title", TableInfo.Column("title", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsGroups.put("contacts_count", TableInfo.Column("contacts_count", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysGroups: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesGroups: MutableSet<TableInfo.Index> = mutableSetOf()
        _indicesGroups.add(TableInfo.Index("index_groups_id", true, listOf("id"), listOf("ASC")))
        val _infoGroups: TableInfo = TableInfo("groups", _columnsGroups, _foreignKeysGroups, _indicesGroups)
        val _existingGroups: TableInfo = read(connection, "groups")
        if (!_infoGroups.equals(_existingGroups)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |groups(com.itamadersomajinc.banglatype.commons.models.contacts.Group).
              | Expected:
              |""".trimMargin() + _infoGroups + """
              |
              | Found:
              |""".trimMargin() + _existingGroups)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "contacts", "groups")
  }

  public override fun clearAllTables() {
    super.performClear(false, "contacts", "groups")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(ContactsDao::class, ContactsDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(GroupsDao::class, GroupsDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>): List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun ContactsDao(): ContactsDao = _contactsDao.value

  public override fun GroupsDao(): GroupsDao = _groupsDao.value
}
