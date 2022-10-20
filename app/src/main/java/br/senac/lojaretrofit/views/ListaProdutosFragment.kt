package br.senac.lojaretrofit.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.senac.lojaretrofit.R
import br.senac.lojaretrofit.databinding.CardItemBinding
import br.senac.lojaretrofit.databinding.FragmentListaProdutosBinding
import br.senac.lojaretrofit.model.Produto
import br.senac.lojaretrofit.services.API
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListaProdutosFragment : Fragment() {

    lateinit var binding: FragmentListaProdutosBinding

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentListaProdutosBinding.inflate(inflater)

        //Configura o evento de "puxar para baixo"
        binding.swipeRefresh.setOnRefreshListener {
            //Chama a função que vai até o back end e obtém os produtos
            atualizarProdutos()
        }

        activity?.title = "Todos os Produtos"

        return binding.root
    }

    //Callback chamado quando a tela é reaberta
    override fun onResume() {
        super.onResume()

        //Atualiza a lista de produtos quando a tela é reaberta
        atualizarProdutos()

        val activity = activity as MainActivity
        activity.esconderUp()
    }

    //Função que, quando chamada, vai até o endpoint da API e atualiza os produtos
    fun atualizarProdutos() {

        //Callback acionado quando a execução da API concluir
        val callback = object : Callback<List<Produto>> {

            //Chamada quando o endpoint responder
            override fun onResponse(call: Call<List<Produto>>, response: Response<List<Produto>>) {

                desabilitarCarregamento()

                if (response.isSuccessful) {
                    val listaProdutos = response.body()
                    atualizarUI(listaProdutos)
                }
                else {
                    //val error = response.errorBody().toString()
                    Snackbar.make(binding.container, "Não é possível autualizar os produtos",
                        Snackbar.LENGTH_LONG).show()

                    Log.e("ERROR", response.errorBody().toString())
                }
            }

            //Chamada caso aconteça algum problema e não seja possível bater no endpoint
            //Ou a resposta seja incompatível
            override fun onFailure(call: Call<List<Produto>>, t: Throwable) {
                desabilitarCarregamento()

                Snackbar.make(binding.container, "Não foi possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                Log.e("ERROR", "Falha ao executar serviço", t)
            }
        }

        //Faz a chamada a API
        API().produto.listar().enqueue(callback)

        //Chama uma função para habilitar o carregamento
        habilitarCarregamento()
    }

    //Indica ao Swipe Refresh para mostrar o indicador de carregamento
    private fun desabilitarCarregamento() {
        binding.swipeRefresh.isRefreshing = false

    }

    //Indica ao Swipe Refresh para esconder o indicador de carregamento
    private fun habilitarCarregamento() {
        binding.swipeRefresh.isRefreshing = true
    }

    //Utilizado para atualizar a tela quando a resposta voltar
    fun atualizarUI(lista: List<Produto>?) {
        //Limpa a lista de itens
        binding.container.removeAllViews()

        //Itera pela lista de respostas
        lista?.forEach {
            //ELEMENTOS DINÂMICOS
            //Cria um cartão dinamicamente
            val cardBinding = CardItemBinding.inflate(layoutInflater)

            //Configura os itens do cartão com os valores do
            //item do array
            cardBinding.textTitulo.text = it.nomeProduto
            cardBinding.textDesc.text = it.descProduto

            //Solicita o carregamento da imagem
            Picasso.get().load(
                "https://oficinacordova.azurewebsites.net/android/rest/produto/image/${it.idProduto}"
            ).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(cardBinding.imagem)

            cardBinding.root.setOnClickListener { cartao ->
                val frag = DetalheProdutoFragment(it.idProduto)
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.container, frag)?.addToBackStack("Detalhe do Produto")?.commit()
            }

            //Adiciona o cartão no container para que apareça na tela
            binding.container.addView(cardBinding.root)
        }
    }
}