package com.metawebthree.keykeeper.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.metawebthree.keykeeper.utils.ErpDBHelper

class ErpMemberProvider : ContentProvider() {
    private lateinit var helper: ErpDBHelper

    /**
     * 匹配URi的容器
     *  构造的code：匹配不上的时候，code是多少，默认是NO_MATCH -1
     */
    @JvmField
    val matcher = UriMatcher(UriMatcher.NO_MATCH).apply {
        addURI("com.metawebthree.keykeeper.provider.ErpMemberProvider","/erp", CODE_MEMBER)
        addURI("com.metawebthree.keykeeper.provider.ErpMemberProvider","/erp/#", CODE_MEMBER_ID)
    }
    @JvmField
    final val CODE_MEMBER = 1
    @JvmField
    final val CODE_MEMBER_ID = 2

    override fun onCreate(): Boolean {
        helper = ErpDBHelper(context, 1)
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val db = helper.getReadableDatabase()
        val code = matcher.match(uri)
        when (code) {
            CODE_MEMBER -> return db.query("erp",projection,selection,selectionArgs,null,null,sortOrder)
            CODE_MEMBER_ID -> {
                val id = ContentUris.parseId(uri);  //uri中的id
                return db.query(
                    "erp",
                    projection,
                    "_id=?",
                    arrayOf<String>(id.toString()),
                    null,
                    null,
                    sortOrder
                )
            }
            else -> throw RuntimeException("Uri不符合规则")
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val db = helper.getWritableDatabase()
        val code = matcher.match(uri)
        if (code == CODE_MEMBER) {
            val id = db.insert("erp", null, values)
            db.close()
            return ContentUris.withAppendedId(uri, id)
        }
        else {
            db.close()
            throw RuntimeException("Uri不符合规则")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = helper.getWritableDatabase()
        val code = matcher.match(uri)
        var count = 0
        when (code) {
            CODE_MEMBER -> {
                count = db.delete("stu", selection, selectionArgs)
                db.close()
            }
            CODE_MEMBER_ID -> {
                val id = ContentUris.parseId(uri)
                count = db.delete("stu", "_id=?", arrayOf(id.toString()))
                db.close();
            }
            else -> throw RuntimeException("Uri不符合规则")
        }
        return count
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

}