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
class LoginTest : LoginRepositoryInterface {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    private var application = Application()
    private var repository = LoginRepository(application)

    @Test
    fun login_ok(){
        repository.requestLogin("prova@prova.com","123456",this)
    }

    @Test
    fun login_nok(){
        repository.requestLogin("prova@prova.com","111111",this)
    }

    override fun credencialsOK() {
    }

    override fun credencialsNOK() {
    }
}