package br.senac.lojaretrofit.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.senac.lojaretrofit.R
import br.senac.lojaretrofit.databinding.FragmentDetalheProdutoBinding
import br.senac.lojaretrofit.model.Produto
import br.senac.lojaretrofit.services.API
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*

//O parâmetro indica o id do produto a exibir
class DetalheProdutoFragment(val idProduto: Int) : Fragment() {
    //binding do fragmento
    lateinit var binding: FragmentDetalheProdutoBinding

    //Executado quando o fragmento for criado
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetalheProdutoBinding.inflate(inflater)

        //Chama a função para customizar o visual da atividade
        alterarVisual()
        //Chama a função para obter o produto e mostrar na tela
        obterDadosDoProduto(idProduto)

        //Volta o layout inflado pelo binding
        return binding.root
    }

    //Executado quando o fragmento for aberto novamente
    override fun onResume() {
        super.onResume()
        //Mostra o botão up (voltar) na atividade
        val activity = activity as MainActivity
        activity.mostrarUp()
    }

    //Chamado quando um botão de menu for clicado, inclusive o up
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.getItemId()) {
            //Se for o botão uo
            android.R.id.home -> {
                //Simula o clique no voltar pela atividade
                (activity as MainActivity).onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //Customiza o visual da atividade e habilita o pressionamento do botão up
    fun alterarVisual() {
        activity?.title = "Detalhe do Produto"
        setHasOptionsMenu(true)
    }

    //Obtpem dados do produto indicado
    fun obterDadosDoProduto(id: Int) {

        //Callback acionado quando a execução da API concluir
        val callback = object : Callback<Produto> {

            //Chamada quando o endpoint responder
            override fun onResponse(call: Call<Produto>, response: Response<Produto>) {

                //Desliga o indicador de carregamento
                desabilitarCarregamento(true)

                //Verifica se a resposta teve sucesso
                if (response.isSuccessful) {
                    //Obtém os dados do produto em formato de modelo
                    val produto = response.body()
                    //Chama a função para atualizar a tela
                    atualizarUI(produto)
                }
                else {
                    //Mostra uma mensagem de falha de carregamento para o usuário
                    Snackbar.make(binding.root, "Não é possível obter dados de produto",
                        Snackbar.LENGTH_LONG).show()

                    //Registra possíveis erros no console
                    Log.e("ERROR", response.errorBody().toString())
                }
            }

            //Chamada caso aconteça algum problema e não seja possível bater no endpoint
            //Ou a resposta seja incompatível
            override fun onFailure(call: Call<Produto>, t: Throwable) {
                //Desabilita o indicador de carregamento
                desabilitarCarregamento(false)

                //Mostra uma mensagem de falha de carregamento para o usuário
                Snackbar.make(binding.root, "Não foi possível se conectar ao servidor",
                    Snackbar.LENGTH_LONG).show()

                //Registra possíveis erros no console
                Log.e("ERROR", "Falha ao executar serviço", t)
            }
        }

        //Faz a chamada a API para obter um produto
        API().produto.get(idProduto).enqueue(callback)

        //Chama uma função para habilitar o indicador de carregamento
        habilitarCarregamento()
    }

    //Indica ao Swipe Refresh para mostrar o indicador de carregamento
    private fun desabilitarCarregamento(success: Boolean) {
        //Mostra o cartão e desliga o indicador de carregamento
        binding.progressBar.visibility = View.INVISIBLE
        if (success) {
            binding.cardProduto.visibility = View.VISIBLE
        }
    }

    //Indica ao Swipe Refresh para esconder o indicador de carregamento
    private fun habilitarCarregamento() {
        //Desliga o cartão e mostra o indicador de carregamento
        binding.progressBar.visibility = View.VISIBLE
        binding.cardProduto.visibility = View.INVISIBLE
    }

    //Utilizado para atualizar a tela quando a resposta voltar
    fun atualizarUI(produto: Produto?) {

        //Atualiza a tela com os dados do produto
        produto?.let {
            binding.txtNome.text = it.nomeProduto
            //Usado para formatação de real
            val format = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
            binding.txtPreco.text = format.format(it.precProduto)
            binding.txtDesconto.text = "Desconto: ${format.format(it.descontoPromocao)}"
            binding.txtDescricao.text = it.descProduto
            binding.txtQtdEstoque.text = "Em Estoque: ${it.qtdMinEstoque}"

            //Solicita o carregamento da imagem
            Picasso.get().load(
                "https://oficinacordova.azurewebsites.net/android/rest/produto/image/${it.idProduto}"
            ).placeholder(R.drawable.no_image).error(R.drawable.no_image).into(binding.imagem)
        }

    }
}