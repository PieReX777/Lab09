package com.example.lab09

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.annotations.SerializedName
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.lang.reflect.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProgPrincipal9()
        }
    }

    @Composable
    fun ProgPrincipal9() {
        val urlBase = "https://jsonplaceholder.typicode.com/"
        val retrofit = Retrofit.Builder().baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val servicio = retrofit.create(PostApiService::class.java)
        val navController = rememberNavController()

        Scaffold(
            topBar =    { BarraSuperior() },
            bottomBar = { BarraInferior(navController) },
            content =   { paddingValues -> Contenido(paddingValues, navController, servicio) }
        )
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BarraSuperior() {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = "JSONPlaceHolder Access",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )
    }

    @Composable
    fun BarraInferior(navController: NavHostController) {
        NavigationBar(
            containerColor = Color.LightGray
        ) {
            NavigationBarItem(
                icon = { Icon(Icons.Outlined.Home, contentDescription = "Inicio") },
                label = { Text("Inicio") },
                selected = navController.currentDestination?.route == "inicio",
                onClick = { navController.navigate("inicio") }
            )
            NavigationBarItem(
                icon = { Icon(Icons.Outlined.Favorite, contentDescription = "Posts") },
                label = { Text("Posts") },
                selected = navController.currentDestination?.route == "posts",
                onClick = { navController.navigate("posts") }
            )
        }
    }


    @Composable
    fun Contenido(
        pv: PaddingValues,
        navController: NavHostController,
        servicio: PostApiService
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(pv)
        ) {
            NavHost(
                navController = navController,
                startDestination = "inicio" // Ruta de inicio
            ) {
                composable("inicio") { ScreenInicio() }

                composable("posts") { ScreenPosts(navController, servicio) }
                composable("postsVer/{id}", arguments = listOf(
                    navArgument("id") { type = NavType.IntType })
                ) {
                    ScreenPost(navController, servicio, it.arguments!!.getInt("id"))
                }
            }
        }
    }
}
