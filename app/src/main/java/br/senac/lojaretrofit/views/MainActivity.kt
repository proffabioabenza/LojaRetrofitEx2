package br.senac.lojaretrofit.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.senac.lojaretrofit.R
import br.senac.lojaretrofit.databinding.ActivityMainBinding

//Atividade que lista os produtos
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    //Chamado quando a atividade é criada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Carrega o fragmento principal
        val fragment = ListaProdutosFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    //PARA PERMITIR O BOTÃO UP DE VOLTAR UM FRAGMENTO
    fun mostrarUp() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    fun esconderUp() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.getBackStackEntryCount() > 1) {
            supportFragmentManager.popBackStackImmediate()
        } else {
            super.onBackPressed()
        }
    }
}