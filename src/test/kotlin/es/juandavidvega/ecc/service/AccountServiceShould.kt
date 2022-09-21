package es.juandavidvega.ecc.service

import assertk.assertThat
import assertk.assertions.isEqualTo
import es.juandavidvega.ecc.storage.AccountStorage
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test

internal class AccountServiceShould {
    @Test fun
    `store new account data`(){
        val givenNewAccount = NewAccount("username")
        val accountStorageMock = mockk<AccountStorage>()

        every { accountStorageMock.create("username") } returns OperationResult.Success

        val accountService = AccountService(accountStorageMock)

        val result = accountService.create(givenNewAccount)

        assertThat(result).isEqualTo(OperationResult.Success)
    }
}
