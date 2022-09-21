package es.juandavidvega.ecc.service

import es.juandavidvega.ecc.storage.AccountStorage

data class NewAccount(
    val userName: String?
)

class AccountService (private val accountStorage: AccountStorage) {
    fun create(newAccount: NewAccount): OperationResult {
        accountStorage.create(newAccount.userName!!)
        return OperationResult.Success
    }

}
