package br.senac.lojaretrofit.model

//Uma data class representa uma classe de negócio em Kotlins
//Deve ser equivalente ao JSON que vai/volta para/da API
data class Produto(
	val descProduto: String,
	val qtdMinEstoque: Int,
	val idProduto: Int,
	val precProduto: Double,
	val descontoPromocao: Double,
	val idCategoria: Int,
	val nomeProduto: String,
	val ativoProduto: Boolean
)
