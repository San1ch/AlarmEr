package com.example.alarmer.core.domain.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppMemoryStore @Inject constructor(){
    private val state = MutableStateFlow<ContextStoreState>(ContextStoreState())
}
class ContextStoreState(){

}
