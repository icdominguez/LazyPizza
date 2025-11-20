package com.seno.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.seno.core.domain.userdata.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class UserDataDataStore(
    private val dataStore: DataStore<Preferences>
) : UserData {
    override fun getCartId(): Flow<String?> =
        dataStore.data.map {
            it[CART_ID_KEY]
        }

    override suspend fun setCardId(cartId: String) {
        dataStore.edit { preferences ->
            preferences[CART_ID_KEY] = cartId
        }
    }

    override fun getTotalItemCart(): Flow<Int> =
        dataStore.data.map {
            it[TOTAL_ITEM_CART_KEY] ?: 0
        }

    override suspend fun setTotalItemCart(totalItem: Int) {
        dataStore.edit { preferences ->
            preferences[TOTAL_ITEM_CART_KEY] = totalItem
        }
    }

    override fun getUserId(): Flow<String?> =
        dataStore.data.map {
            it[USER_ID_KEY]
        }

    override suspend fun setUserId(phoneNumber: String) {
        dataStore.edit { preferences ->
            preferences[USER_ID_KEY] = UUID.nameUUIDFromBytes(phoneNumber.toByteArray()).toString()
        }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    companion object {
        private val CART_ID_KEY = stringPreferencesKey("cart_id")
        private val USER_ID_KEY = stringPreferencesKey("user_id")
        private val TOTAL_ITEM_CART_KEY = intPreferencesKey("total_item_cart_key")
    }
}
