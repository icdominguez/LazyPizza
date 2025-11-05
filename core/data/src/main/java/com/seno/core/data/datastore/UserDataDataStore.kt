package com.seno.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.seno.core.domain.userdata.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

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

    override fun getIsLogin(): Flow<Boolean> =
        dataStore.data.map {
            it[IS_LOGIN_KEY] ?: false
        }

    override suspend fun setIsLogin(isLogin: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_LOGIN_KEY] = isLogin
        }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }

    companion object {
        private val CART_ID_KEY = stringPreferencesKey("cart_id")
        private val IS_LOGIN_KEY = booleanPreferencesKey("is_login")
        private val TOTAL_ITEM_CART_KEY = intPreferencesKey("total_item_cart_key")
    }
}
