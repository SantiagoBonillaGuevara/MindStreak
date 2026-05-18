package com.example.mindstreak.data.repository

import com.example.mindstreak.data.mock.MockData
import com.example.mindstreak.data.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockUserRepository : UserRepository {
    private val _userFlow = MutableStateFlow<User?>(MockData.USER)
    override val userFlow: Flow<User?> = _userFlow.asStateFlow()

    override suspend fun getProfile(userId: String): User = MockData.USER

    override suspend fun updateProfile(user: User) {
        _userFlow.value = user
    }

    override fun refresh() {
        // No-op for mock
    }
}
