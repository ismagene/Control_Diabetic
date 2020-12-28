package com.ismasoft.controldiabetic

import android.app.Application
import com.ismasoft.controldiabetic.data.repository.RegistreRepository
import com.ismasoft.controldiabetic.data.repository.interfaces.RegistreRepositoryInterface
import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RegisterTest : RegistreRepositoryInterface {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    private var application = Application()
    private var repository = RegistreRepository(application)

    @Test
    fun login_ok(){
        repository.comprobarExisteixEmail("prova@prova.com",this)
    }

    @Test
    fun login_nok(){
        repository.comprobarExisteixEmail("prova@prova.com",this)
    }

    override fun comprobarExisteixEmailOK() {
    }

    override fun comprobarExisteixEmailNOK() {
    }

    override fun registreOK() {
    }

    override fun registreNOK() {
    }

    override fun registreInsertarOK() {
    }

    override fun registreInsertarNOK() {
    }
}