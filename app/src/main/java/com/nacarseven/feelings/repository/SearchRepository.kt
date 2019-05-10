package com.nacarseven.feelings.repository

import com.nacarseven.feelings.network.api.SearchApi


private const val TOKEN =
    "AAAAAAAAAAAAAAAAAAAAAC2J%2BQAAAAAAX5B58aZYrknMxkcsseexgaLO2Ec%3DN2WSSTifrM1VvmsfzzuDdSLHKEymLHSzmhHd36RQ1XYLtdkuFD"

class SearchRepository(private var searchApi: SearchApi) : SearchRepositoryContract {


    override fun getAccessToken(): String {
        return TOKEN
    }

}