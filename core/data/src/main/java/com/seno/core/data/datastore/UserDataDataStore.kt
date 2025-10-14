package com.seno.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
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

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(CART_ID_KEY)
        }
    }

    companion object {
        private val CART_ID_KEY = stringPreferencesKey("cart_id")
    }
}
