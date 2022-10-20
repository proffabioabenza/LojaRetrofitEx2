package br.senac.lojaretrofit.services

import br.senac.lojaretrofit.model.Produto
import retrofit2.Call
import retrofit2.http.*

//Serviço visa centralizar os endpoints de determinado recurso
interface ProdutoService {

    //USADO NO PROJETO
    @GET("/android/rest/produto")
    fun listar(): Call<List<Produto>>

    //OUTROS EXEMPLOS
    //Parâmetro de URL (ex: http://endereco.com.br/endpoint/valorId)
    @GET("/android/rest/produto/{id}")
    fun get(@Path("id") id: Int): Call<Produto>

    //Outro exemplo de parâmetro de URL (ex: http://endereco.com.br/endpoint/valorNome)
    @GET("/android/rest/produto/{nome}")
    fun pesquisar(@Path("nome") nome: String): Call<List<Produto>>

    //Outro exemplo de parâmetro de URL (ex: http://endereco.com.br/endpoint?nome=valorNome)
    @GET("/android/rest/produto")
    fun pesquisar2(@Query("nome") nome: String): Call<List<Produto>>

    //Outro exemplo de parâmetro de corpo de requisição, normalmente utilizado
    //no envio por requisições de inserção
    @POST("/android/rest/produto")
    fun inserir(@Body produto: Produto): Call<Produto>

    //Outro exemplo de parâmetro de corpo de requisição, normalmente utilizado
    //no envio por requisições de deleção
    @PUT("/android/rest/produto")
    fun atualizar(@Body produto: Produto, @Query("id") id: Int): Call<Produto>

    //Requisição de deleção
    @DELETE("/android/rest/produto")
    fun excluir(@Query("id") id: Int): Call<Produto>
}