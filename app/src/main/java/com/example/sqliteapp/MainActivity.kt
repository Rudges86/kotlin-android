package com.example.sqliteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sqliteapp.data.local.database.AppDatabase
import com.example.sqliteapp.data.repository.ContatoRepository
import com.example.sqliteapp.ui.screens.CadastroScreen
import com.example.sqliteapp.ui.screens.ListaContatosScreen
import com.example.sqliteapp.viewmodel.ContatoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val repository = ContatoRepository(db.contatoDao())
        val viewModel = ContatoViewModel(repository)

        setContent {

            val navController = rememberNavController()


            NavHost(navController = navController, startDestination = "lista") {


                composable("lista") {
                    ListaContatosScreen(
                        viewModel = viewModel,
                        onNovoContatoClick = {
                            viewModel.contatoSelecionado = null
                            navController.navigate("cadastro")
                        },
                        onEditContatoClick = { contato ->
                            viewModel.contatoSelecionado = contato
                            navController.navigate("cadastro")
                        }
                    )
                }
                composable("cadastro") {
                    CadastroScreen(viewModel = viewModel) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}