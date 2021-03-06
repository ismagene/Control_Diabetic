package com.ismasoft.controldiabetic

import android.app.Application
import com.ismasoft.controldiabetic.data.repository.LoginRepository
import com.ismasoft.controldiabetic.data.repository.interfaces.LoginRepositoryInterface
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    private var application = Application()
    private var repository = LoginRepository(application)
    lateinit var loginActivityInstance : LoginRepositoryInterface
    @Test
    fun login_correct(){
        repository.requestLogin("prova@prova.com","123456",loginActivityInstance)
    }
}