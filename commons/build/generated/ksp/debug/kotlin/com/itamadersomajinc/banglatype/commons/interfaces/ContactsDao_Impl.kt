package com.itamadersomajinc.banglatype.commons.interfaces

import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.util.appendPlaceholders
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performBlocking
import androidx.sqlite.SQLiteStatement
import com.itamadersomajinc.banglatype.commons.helpers.Converters
import com.itamadersomajinc.banglatype.commons.models.PhoneNumber
import com.itamadersomajinc.banglatype.commons.models.contacts.Address
import com.itamadersomajinc.banglatype.commons.models.contacts.Email
import com.itamadersomajinc.banglatype.commons.models.contacts.Event
import com.itamadersomajinc.banglatype.commons.models.contacts.IM
import com.itamadersomajinc.banglatype.commons.models.contacts.LocalContact
import java.util.ArrayList
import javax.`annotation`.processing.Generated
import kotlin.ByteArray
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlin.text.StringBuilder

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class ContactsDao_Impl(
  __db: RoomDatabase,
) : ContactsDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfLocalContact: EntityInsertAdapter<LocalContact>

  private val __converters: Converters = Converters()
  init {
    this.__db = __db
    this.__insertAdapterOfLocalContact = object : EntityInsertAdapter<LocalContact>() {
      protected override fun createQuery(): String = "INSERT OR REPLACE INTO `contacts` (`id`,`prefix`,`first_name`,`middle_name`,`surname`,`suffix`,`nickname`,`photo`,`photo_uri`,`phone_numbers`,`emails`,`events`,`starred`,`addresses`,`notes`,`groups`,`company`,`job_position`,`websites`,`ims`,`ringtone`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: LocalContact) {
        val _tmpId: Int? = entity.id
        if (_tmpId == null) {
          statement.bindNull(1)
        } else {
          statement.bindLong(1, _tmpId.toLong())
        }
        statement.bindText(2, entity.prefix)
        statement.bindText(3, entity.firstName)
        statement.bindText(4, entity.middleName)
        statement.bindText(5, entity.surname)
        statement.bindText(6, entity.suffix)
        statement.bindText(7, entity.nickname)
        val _tmpPhoto: ByteArray? = entity.photo
        if (_tmpPhoto == null) {
          statement.bindNull(8)
        } else {
          statement.bindBlob(8, _tmpPhoto)
        }
        statement.bindText(9, entity.photoUri)
        val _tmp: String = __converters.phoneNumberListToJson(entity.phoneNumbers)
        statement.bindText(10, _tmp)
        val _tmp_1: String = __converters.emailListToJson(entity.emails)
        statement.bindText(11, _tmp_1)
        val _tmp_2: String = __converters.eventListToJson(entity.events)
        statement.bindText(12, _tmp_2)
        statement.bindLong(13, entity.starred.toLong())
        val _tmp_3: String = __converters.addressListToJson(entity.addresses)
        statement.bindText(14, _tmp_3)
        statement.bindText(15, entity.notes)
        val _tmp_4: String = __converters.longListToJson(entity.groups)
        statement.bindText(16, _tmp_4)
        statement.bindText(17, entity.company)
        statement.bindText(18, entity.jobPosition)
        val _tmp_5: String = __converters.stringListToJson(entity.websites)
        statement.bindText(19, _tmp_5)
        val _tmp_6: String = __converters.iMsListToJson(entity.IMs)
        statement.bindText(20, _tmp_6)
        val _tmpRingtone: String? = entity.ringtone
        if (_tmpRingtone == null) {
          statement.bindNull(21)
        } else {
          statement.bindText(21, _tmpRingtone)
        }
      }
    }
  }

  public override fun insertOrUpdate(contact: LocalContact): Long = performBlocking(__db, false, true) { _connection ->
    val _result: Long = __insertAdapterOfLocalContact.insertAndReturnId(_connection, contact)
    _result
  }

  public override fun getContacts(): List<LocalContact> {
    val _sql: String = "SELECT * FROM contacts"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPrefix: Int = getColumnIndexOrThrow(_stmt, "prefix")
        val _columnIndexOfFirstName: Int = getColumnIndexOrThrow(_stmt, "first_name")
        val _columnIndexOfMiddleName: Int = getColumnIndexOrThrow(_stmt, "middle_name")
        val _columnIndexOfSurname: Int = getColumnIndexOrThrow(_stmt, "surname")
        val _columnIndexOfSuffix: Int = getColumnIndexOrThrow(_stmt, "suffix")
        val _columnIndexOfNickname: Int = getColumnIndexOrThrow(_stmt, "nickname")
        val _columnIndexOfPhoto: Int = getColumnIndexOrThrow(_stmt, "photo")
        val _columnIndexOfPhotoUri: Int = getColumnIndexOrThrow(_stmt, "photo_uri")
        val _columnIndexOfPhoneNumbers: Int = getColumnIndexOrThrow(_stmt, "phone_numbers")
        val _columnIndexOfEmails: Int = getColumnIndexOrThrow(_stmt, "emails")
        val _columnIndexOfEvents: Int = getColumnIndexOrThrow(_stmt, "events")
        val _columnIndexOfStarred: Int = getColumnIndexOrThrow(_stmt, "starred")
        val _columnIndexOfAddresses: Int = getColumnIndexOrThrow(_stmt, "addresses")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfGroups: Int = getColumnIndexOrThrow(_stmt, "groups")
        val _columnIndexOfCompany: Int = getColumnIndexOrThrow(_stmt, "company")
        val _columnIndexOfJobPosition: Int = getColumnIndexOrThrow(_stmt, "job_position")
        val _columnIndexOfWebsites: Int = getColumnIndexOrThrow(_stmt, "websites")
        val _columnIndexOfIMs: Int = getColumnIndexOrThrow(_stmt, "ims")
        val _columnIndexOfRingtone: Int = getColumnIndexOrThrow(_stmt, "ringtone")
        val _result: MutableList<LocalContact> = mutableListOf()
        while (_stmt.step()) {
          val _item: LocalContact
          val _tmpId: Int?
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null
          } else {
            _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          }
          val _tmpPrefix: String
          _tmpPrefix = _stmt.getText(_columnIndexOfPrefix)
          val _tmpFirstName: String
          _tmpFirstName = _stmt.getText(_columnIndexOfFirstName)
          val _tmpMiddleName: String
          _tmpMiddleName = _stmt.getText(_columnIndexOfMiddleName)
          val _tmpSurname: String
          _tmpSurname = _stmt.getText(_columnIndexOfSurname)
          val _tmpSuffix: String
          _tmpSuffix = _stmt.getText(_columnIndexOfSuffix)
          val _tmpNickname: String
          _tmpNickname = _stmt.getText(_columnIndexOfNickname)
          val _tmpPhoto: ByteArray?
          if (_stmt.isNull(_columnIndexOfPhoto)) {
            _tmpPhoto = null
          } else {
            _tmpPhoto = _stmt.getBlob(_columnIndexOfPhoto)
          }
          val _tmpPhotoUri: String
          _tmpPhotoUri = _stmt.getText(_columnIndexOfPhotoUri)
          val _tmpPhoneNumbers: ArrayList<PhoneNumber>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfPhoneNumbers)
          _tmpPhoneNumbers = __converters.jsonToPhoneNumberList(_tmp)
          val _tmpEmails: ArrayList<Email>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfEmails)
          _tmpEmails = __converters.jsonToEmailList(_tmp_1)
          val _tmpEvents: ArrayList<Event>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfEvents)
          _tmpEvents = __converters.jsonToEventList(_tmp_2)
          val _tmpStarred: Int
          _tmpStarred = _stmt.getLong(_columnIndexOfStarred).toInt()
          val _tmpAddresses: ArrayList<Address>
          val _tmp_3: String
          _tmp_3 = _stmt.getText(_columnIndexOfAddresses)
          _tmpAddresses = __converters.jsonToAddressList(_tmp_3)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpGroups: ArrayList<Long>
          val _tmp_4: String
          _tmp_4 = _stmt.getText(_columnIndexOfGroups)
          _tmpGroups = __converters.jsonToLongList(_tmp_4)
          val _tmpCompany: String
          _tmpCompany = _stmt.getText(_columnIndexOfCompany)
          val _tmpJobPosition: String
          _tmpJobPosition = _stmt.getText(_columnIndexOfJobPosition)
          val _tmpWebsites: ArrayList<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfWebsites)
          _tmpWebsites = __converters.jsonToStringList(_tmp_5)
          val _tmpIMs: ArrayList<IM>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfIMs)
          _tmpIMs = __converters.jsonToIMsList(_tmp_6)
          val _tmpRingtone: String?
          if (_stmt.isNull(_columnIndexOfRingtone)) {
            _tmpRingtone = null
          } else {
            _tmpRingtone = _stmt.getText(_columnIndexOfRingtone)
          }
          _item = LocalContact(_tmpId,_tmpPrefix,_tmpFirstName,_tmpMiddleName,_tmpSurname,_tmpSuffix,_tmpNickname,_tmpPhoto,_tmpPhotoUri,_tmpPhoneNumbers,_tmpEmails,_tmpEvents,_tmpStarred,_tmpAddresses,_tmpNotes,_tmpGroups,_tmpCompany,_tmpJobPosition,_tmpWebsites,_tmpIMs,_tmpRingtone)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getFavoriteContacts(): List<LocalContact> {
    val _sql: String = "SELECT * FROM contacts WHERE starred = 1"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPrefix: Int = getColumnIndexOrThrow(_stmt, "prefix")
        val _columnIndexOfFirstName: Int = getColumnIndexOrThrow(_stmt, "first_name")
        val _columnIndexOfMiddleName: Int = getColumnIndexOrThrow(_stmt, "middle_name")
        val _columnIndexOfSurname: Int = getColumnIndexOrThrow(_stmt, "surname")
        val _columnIndexOfSuffix: Int = getColumnIndexOrThrow(_stmt, "suffix")
        val _columnIndexOfNickname: Int = getColumnIndexOrThrow(_stmt, "nickname")
        val _columnIndexOfPhoto: Int = getColumnIndexOrThrow(_stmt, "photo")
        val _columnIndexOfPhotoUri: Int = getColumnIndexOrThrow(_stmt, "photo_uri")
        val _columnIndexOfPhoneNumbers: Int = getColumnIndexOrThrow(_stmt, "phone_numbers")
        val _columnIndexOfEmails: Int = getColumnIndexOrThrow(_stmt, "emails")
        val _columnIndexOfEvents: Int = getColumnIndexOrThrow(_stmt, "events")
        val _columnIndexOfStarred: Int = getColumnIndexOrThrow(_stmt, "starred")
        val _columnIndexOfAddresses: Int = getColumnIndexOrThrow(_stmt, "addresses")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfGroups: Int = getColumnIndexOrThrow(_stmt, "groups")
        val _columnIndexOfCompany: Int = getColumnIndexOrThrow(_stmt, "company")
        val _columnIndexOfJobPosition: Int = getColumnIndexOrThrow(_stmt, "job_position")
        val _columnIndexOfWebsites: Int = getColumnIndexOrThrow(_stmt, "websites")
        val _columnIndexOfIMs: Int = getColumnIndexOrThrow(_stmt, "ims")
        val _columnIndexOfRingtone: Int = getColumnIndexOrThrow(_stmt, "ringtone")
        val _result: MutableList<LocalContact> = mutableListOf()
        while (_stmt.step()) {
          val _item: LocalContact
          val _tmpId: Int?
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null
          } else {
            _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          }
          val _tmpPrefix: String
          _tmpPrefix = _stmt.getText(_columnIndexOfPrefix)
          val _tmpFirstName: String
          _tmpFirstName = _stmt.getText(_columnIndexOfFirstName)
          val _tmpMiddleName: String
          _tmpMiddleName = _stmt.getText(_columnIndexOfMiddleName)
          val _tmpSurname: String
          _tmpSurname = _stmt.getText(_columnIndexOfSurname)
          val _tmpSuffix: String
          _tmpSuffix = _stmt.getText(_columnIndexOfSuffix)
          val _tmpNickname: String
          _tmpNickname = _stmt.getText(_columnIndexOfNickname)
          val _tmpPhoto: ByteArray?
          if (_stmt.isNull(_columnIndexOfPhoto)) {
            _tmpPhoto = null
          } else {
            _tmpPhoto = _stmt.getBlob(_columnIndexOfPhoto)
          }
          val _tmpPhotoUri: String
          _tmpPhotoUri = _stmt.getText(_columnIndexOfPhotoUri)
          val _tmpPhoneNumbers: ArrayList<PhoneNumber>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfPhoneNumbers)
          _tmpPhoneNumbers = __converters.jsonToPhoneNumberList(_tmp)
          val _tmpEmails: ArrayList<Email>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfEmails)
          _tmpEmails = __converters.jsonToEmailList(_tmp_1)
          val _tmpEvents: ArrayList<Event>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfEvents)
          _tmpEvents = __converters.jsonToEventList(_tmp_2)
          val _tmpStarred: Int
          _tmpStarred = _stmt.getLong(_columnIndexOfStarred).toInt()
          val _tmpAddresses: ArrayList<Address>
          val _tmp_3: String
          _tmp_3 = _stmt.getText(_columnIndexOfAddresses)
          _tmpAddresses = __converters.jsonToAddressList(_tmp_3)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpGroups: ArrayList<Long>
          val _tmp_4: String
          _tmp_4 = _stmt.getText(_columnIndexOfGroups)
          _tmpGroups = __converters.jsonToLongList(_tmp_4)
          val _tmpCompany: String
          _tmpCompany = _stmt.getText(_columnIndexOfCompany)
          val _tmpJobPosition: String
          _tmpJobPosition = _stmt.getText(_columnIndexOfJobPosition)
          val _tmpWebsites: ArrayList<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfWebsites)
          _tmpWebsites = __converters.jsonToStringList(_tmp_5)
          val _tmpIMs: ArrayList<IM>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfIMs)
          _tmpIMs = __converters.jsonToIMsList(_tmp_6)
          val _tmpRingtone: String?
          if (_stmt.isNull(_columnIndexOfRingtone)) {
            _tmpRingtone = null
          } else {
            _tmpRingtone = _stmt.getText(_columnIndexOfRingtone)
          }
          _item = LocalContact(_tmpId,_tmpPrefix,_tmpFirstName,_tmpMiddleName,_tmpSurname,_tmpSuffix,_tmpNickname,_tmpPhoto,_tmpPhotoUri,_tmpPhoneNumbers,_tmpEmails,_tmpEvents,_tmpStarred,_tmpAddresses,_tmpNotes,_tmpGroups,_tmpCompany,_tmpJobPosition,_tmpWebsites,_tmpIMs,_tmpRingtone)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getContactWithId(id: Int): LocalContact? {
    val _sql: String = "SELECT * FROM contacts WHERE id = ?"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPrefix: Int = getColumnIndexOrThrow(_stmt, "prefix")
        val _columnIndexOfFirstName: Int = getColumnIndexOrThrow(_stmt, "first_name")
        val _columnIndexOfMiddleName: Int = getColumnIndexOrThrow(_stmt, "middle_name")
        val _columnIndexOfSurname: Int = getColumnIndexOrThrow(_stmt, "surname")
        val _columnIndexOfSuffix: Int = getColumnIndexOrThrow(_stmt, "suffix")
        val _columnIndexOfNickname: Int = getColumnIndexOrThrow(_stmt, "nickname")
        val _columnIndexOfPhoto: Int = getColumnIndexOrThrow(_stmt, "photo")
        val _columnIndexOfPhotoUri: Int = getColumnIndexOrThrow(_stmt, "photo_uri")
        val _columnIndexOfPhoneNumbers: Int = getColumnIndexOrThrow(_stmt, "phone_numbers")
        val _columnIndexOfEmails: Int = getColumnIndexOrThrow(_stmt, "emails")
        val _columnIndexOfEvents: Int = getColumnIndexOrThrow(_stmt, "events")
        val _columnIndexOfStarred: Int = getColumnIndexOrThrow(_stmt, "starred")
        val _columnIndexOfAddresses: Int = getColumnIndexOrThrow(_stmt, "addresses")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfGroups: Int = getColumnIndexOrThrow(_stmt, "groups")
        val _columnIndexOfCompany: Int = getColumnIndexOrThrow(_stmt, "company")
        val _columnIndexOfJobPosition: Int = getColumnIndexOrThrow(_stmt, "job_position")
        val _columnIndexOfWebsites: Int = getColumnIndexOrThrow(_stmt, "websites")
        val _columnIndexOfIMs: Int = getColumnIndexOrThrow(_stmt, "ims")
        val _columnIndexOfRingtone: Int = getColumnIndexOrThrow(_stmt, "ringtone")
        val _result: LocalContact?
        if (_stmt.step()) {
          val _tmpId: Int?
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null
          } else {
            _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          }
          val _tmpPrefix: String
          _tmpPrefix = _stmt.getText(_columnIndexOfPrefix)
          val _tmpFirstName: String
          _tmpFirstName = _stmt.getText(_columnIndexOfFirstName)
          val _tmpMiddleName: String
          _tmpMiddleName = _stmt.getText(_columnIndexOfMiddleName)
          val _tmpSurname: String
          _tmpSurname = _stmt.getText(_columnIndexOfSurname)
          val _tmpSuffix: String
          _tmpSuffix = _stmt.getText(_columnIndexOfSuffix)
          val _tmpNickname: String
          _tmpNickname = _stmt.getText(_columnIndexOfNickname)
          val _tmpPhoto: ByteArray?
          if (_stmt.isNull(_columnIndexOfPhoto)) {
            _tmpPhoto = null
          } else {
            _tmpPhoto = _stmt.getBlob(_columnIndexOfPhoto)
          }
          val _tmpPhotoUri: String
          _tmpPhotoUri = _stmt.getText(_columnIndexOfPhotoUri)
          val _tmpPhoneNumbers: ArrayList<PhoneNumber>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfPhoneNumbers)
          _tmpPhoneNumbers = __converters.jsonToPhoneNumberList(_tmp)
          val _tmpEmails: ArrayList<Email>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfEmails)
          _tmpEmails = __converters.jsonToEmailList(_tmp_1)
          val _tmpEvents: ArrayList<Event>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfEvents)
          _tmpEvents = __converters.jsonToEventList(_tmp_2)
          val _tmpStarred: Int
          _tmpStarred = _stmt.getLong(_columnIndexOfStarred).toInt()
          val _tmpAddresses: ArrayList<Address>
          val _tmp_3: String
          _tmp_3 = _stmt.getText(_columnIndexOfAddresses)
          _tmpAddresses = __converters.jsonToAddressList(_tmp_3)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpGroups: ArrayList<Long>
          val _tmp_4: String
          _tmp_4 = _stmt.getText(_columnIndexOfGroups)
          _tmpGroups = __converters.jsonToLongList(_tmp_4)
          val _tmpCompany: String
          _tmpCompany = _stmt.getText(_columnIndexOfCompany)
          val _tmpJobPosition: String
          _tmpJobPosition = _stmt.getText(_columnIndexOfJobPosition)
          val _tmpWebsites: ArrayList<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfWebsites)
          _tmpWebsites = __converters.jsonToStringList(_tmp_5)
          val _tmpIMs: ArrayList<IM>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfIMs)
          _tmpIMs = __converters.jsonToIMsList(_tmp_6)
          val _tmpRingtone: String?
          if (_stmt.isNull(_columnIndexOfRingtone)) {
            _tmpRingtone = null
          } else {
            _tmpRingtone = _stmt.getText(_columnIndexOfRingtone)
          }
          _result = LocalContact(_tmpId,_tmpPrefix,_tmpFirstName,_tmpMiddleName,_tmpSurname,_tmpSuffix,_tmpNickname,_tmpPhoto,_tmpPhotoUri,_tmpPhoneNumbers,_tmpEmails,_tmpEvents,_tmpStarred,_tmpAddresses,_tmpNotes,_tmpGroups,_tmpCompany,_tmpJobPosition,_tmpWebsites,_tmpIMs,_tmpRingtone)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun getContactWithNumber(number: String): LocalContact? {
    val _sql: String = "SELECT * FROM contacts WHERE phone_numbers LIKE ?"
    return performBlocking(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, number)
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfPrefix: Int = getColumnIndexOrThrow(_stmt, "prefix")
        val _columnIndexOfFirstName: Int = getColumnIndexOrThrow(_stmt, "first_name")
        val _columnIndexOfMiddleName: Int = getColumnIndexOrThrow(_stmt, "middle_name")
        val _columnIndexOfSurname: Int = getColumnIndexOrThrow(_stmt, "surname")
        val _columnIndexOfSuffix: Int = getColumnIndexOrThrow(_stmt, "suffix")
        val _columnIndexOfNickname: Int = getColumnIndexOrThrow(_stmt, "nickname")
        val _columnIndexOfPhoto: Int = getColumnIndexOrThrow(_stmt, "photo")
        val _columnIndexOfPhotoUri: Int = getColumnIndexOrThrow(_stmt, "photo_uri")
        val _columnIndexOfPhoneNumbers: Int = getColumnIndexOrThrow(_stmt, "phone_numbers")
        val _columnIndexOfEmails: Int = getColumnIndexOrThrow(_stmt, "emails")
        val _columnIndexOfEvents: Int = getColumnIndexOrThrow(_stmt, "events")
        val _columnIndexOfStarred: Int = getColumnIndexOrThrow(_stmt, "starred")
        val _columnIndexOfAddresses: Int = getColumnIndexOrThrow(_stmt, "addresses")
        val _columnIndexOfNotes: Int = getColumnIndexOrThrow(_stmt, "notes")
        val _columnIndexOfGroups: Int = getColumnIndexOrThrow(_stmt, "groups")
        val _columnIndexOfCompany: Int = getColumnIndexOrThrow(_stmt, "company")
        val _columnIndexOfJobPosition: Int = getColumnIndexOrThrow(_stmt, "job_position")
        val _columnIndexOfWebsites: Int = getColumnIndexOrThrow(_stmt, "websites")
        val _columnIndexOfIMs: Int = getColumnIndexOrThrow(_stmt, "ims")
        val _columnIndexOfRingtone: Int = getColumnIndexOrThrow(_stmt, "ringtone")
        val _result: LocalContact?
        if (_stmt.step()) {
          val _tmpId: Int?
          if (_stmt.isNull(_columnIndexOfId)) {
            _tmpId = null
          } else {
            _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          }
          val _tmpPrefix: String
          _tmpPrefix = _stmt.getText(_columnIndexOfPrefix)
          val _tmpFirstName: String
          _tmpFirstName = _stmt.getText(_columnIndexOfFirstName)
          val _tmpMiddleName: String
          _tmpMiddleName = _stmt.getText(_columnIndexOfMiddleName)
          val _tmpSurname: String
          _tmpSurname = _stmt.getText(_columnIndexOfSurname)
          val _tmpSuffix: String
          _tmpSuffix = _stmt.getText(_columnIndexOfSuffix)
          val _tmpNickname: String
          _tmpNickname = _stmt.getText(_columnIndexOfNickname)
          val _tmpPhoto: ByteArray?
          if (_stmt.isNull(_columnIndexOfPhoto)) {
            _tmpPhoto = null
          } else {
            _tmpPhoto = _stmt.getBlob(_columnIndexOfPhoto)
          }
          val _tmpPhotoUri: String
          _tmpPhotoUri = _stmt.getText(_columnIndexOfPhotoUri)
          val _tmpPhoneNumbers: ArrayList<PhoneNumber>
          val _tmp: String
          _tmp = _stmt.getText(_columnIndexOfPhoneNumbers)
          _tmpPhoneNumbers = __converters.jsonToPhoneNumberList(_tmp)
          val _tmpEmails: ArrayList<Email>
          val _tmp_1: String
          _tmp_1 = _stmt.getText(_columnIndexOfEmails)
          _tmpEmails = __converters.jsonToEmailList(_tmp_1)
          val _tmpEvents: ArrayList<Event>
          val _tmp_2: String
          _tmp_2 = _stmt.getText(_columnIndexOfEvents)
          _tmpEvents = __converters.jsonToEventList(_tmp_2)
          val _tmpStarred: Int
          _tmpStarred = _stmt.getLong(_columnIndexOfStarred).toInt()
          val _tmpAddresses: ArrayList<Address>
          val _tmp_3: String
          _tmp_3 = _stmt.getText(_columnIndexOfAddresses)
          _tmpAddresses = __converters.jsonToAddressList(_tmp_3)
          val _tmpNotes: String
          _tmpNotes = _stmt.getText(_columnIndexOfNotes)
          val _tmpGroups: ArrayList<Long>
          val _tmp_4: String
          _tmp_4 = _stmt.getText(_columnIndexOfGroups)
          _tmpGroups = __converters.jsonToLongList(_tmp_4)
          val _tmpCompany: String
          _tmpCompany = _stmt.getText(_columnIndexOfCompany)
          val _tmpJobPosition: String
          _tmpJobPosition = _stmt.getText(_columnIndexOfJobPosition)
          val _tmpWebsites: ArrayList<String>
          val _tmp_5: String
          _tmp_5 = _stmt.getText(_columnIndexOfWebsites)
          _tmpWebsites = __converters.jsonToStringList(_tmp_5)
          val _tmpIMs: ArrayList<IM>
          val _tmp_6: String
          _tmp_6 = _stmt.getText(_columnIndexOfIMs)
          _tmpIMs = __converters.jsonToIMsList(_tmp_6)
          val _tmpRingtone: String?
          if (_stmt.isNull(_columnIndexOfRingtone)) {
            _tmpRingtone = null
          } else {
            _tmpRingtone = _stmt.getText(_columnIndexOfRingtone)
          }
          _result = LocalContact(_tmpId,_tmpPrefix,_tmpFirstName,_tmpMiddleName,_tmpSurname,_tmpSuffix,_tmpNickname,_tmpPhoto,_tmpPhotoUri,_tmpPhoneNumbers,_tmpEmails,_tmpEvents,_tmpStarred,_tmpAddresses,_tmpNotes,_tmpGroups,_tmpCompany,_tmpJobPosition,_tmpWebsites,_tmpIMs,_tmpRingtone)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun updateStarred(isStarred: Int, id: Int) {
    val _sql: String = "UPDATE contacts SET starred = ? WHERE id = ?"
    return performBlocking(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, isStarred.toLong())
        _argIndex = 2
        _stmt.bindLong(_argIndex, id.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun updateRingtone(ringtone: String, id: Int) {
    val _sql: String = "UPDATE contacts SET ringtone = ? WHERE id = ?"
    return performBlocking(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindText(_argIndex, ringtone)
        _argIndex = 2
        _stmt.bindLong(_argIndex, id.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun deleteContactId(id: Int) {
    val _sql: String = "DELETE FROM contacts WHERE id = ?"
    return performBlocking(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override fun deleteContactIds(ids: List<Long>) {
    val _stringBuilder: StringBuilder = StringBuilder()
    _stringBuilder.append("DELETE FROM contacts WHERE id IN (")
    val _inputSize: Int = ids.size
    appendPlaceholders(_stringBuilder, _inputSize)
    _stringBuilder.append(")")
    val _sql: String = _stringBuilder.toString()
    return performBlocking(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        for (_item: Long in ids) {
          _stmt.bindLong(_argIndex, _item)
          _argIndex++
        }
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
