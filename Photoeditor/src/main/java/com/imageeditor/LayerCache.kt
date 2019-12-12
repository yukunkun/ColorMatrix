package com.imageeditor

import androidx.collection.LruCache
import com.miracle.view.imageeditor.bean.EditorCacheData

/**
 * ## Layer data cache for save layer data and restore
 *
 * Created by lxw
 */
object LayerCache {
    private val mLayerCache = androidx.collection.LruCache<String, MutableMap<String, EditorCacheData>>(5)

    fun getCacheDataById(editorId: String): MutableMap<String, EditorCacheData> {
        var cache = mLayerCache[editorId]
        if (cache == null) {
            cache = mutableMapOf()
            mLayerCache.put(editorId, cache)
        }
        return cache
    }

    fun cacheEditorData(editorId: String, data: MutableMap<String, EditorCacheData>?) {
        data?.let {
            mLayerCache.put(editorId, it)
        }
    }
}