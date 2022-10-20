package br.senac.lojaretrofit.services

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//Classe para centralizar as chamadas a API
class API {

    //Instância do Retrofit usada pelas chamadas
    private val retrofit: Retrofit
        get() {
            return Retrofit
                .Builder()
                .baseUrl("https://oficinacordova.azurewebsites.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    //Retorna os serviços para possibilitar a chamada
    val produto: ProdutoService
        get() {
            return retrofit.create(ProdutoService::class.java)
        }

}

