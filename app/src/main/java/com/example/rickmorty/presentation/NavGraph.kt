package com.example.rickmorty.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rickmorty.presentation.characterdetails.CharacterDetailScreen
import com.example.rickmorty.presentation.characterslist.CharactersListScreen

object Routes {
  const val CHARACTERS_LIST = "characters_list"
  const val CHARACTER_DETAIL = "character_detail/{characterId}"
  fun characterDetailRoute(characterId: Int) = "character_detail/$characterId"
}

val LocalNavigator = compositionLocalOf<NavHostController> {
  error("No LocalNavController provided")
}

@Composable
fun AppNavHost(contentPadding: PaddingValues) {
  val navController = rememberNavController()
  CompositionLocalProvider(LocalNavigator provides navController) {
    NavHost(
      navController = navController,
      startDestination = Routes.CHARACTERS_LIST
    ) {
      composable(Routes.CHARACTERS_LIST) {
        CharactersListScreen(
          modifier = Modifier.padding(contentPadding),
          onCharacterClick = { character ->
            navController.navigate(Routes.characterDetailRoute(character.id))
          },
          onFilterClick = {}
        )
      }
      composable(
        route = Routes.CHARACTER_DETAIL,
        arguments = listOf(
          navArgument("characterId") { type = NavType.IntType }
        )
      ) { backStackEntry ->
        val characterId =
          backStackEntry.arguments?.getInt("characterId") ?: return@composable
        CharacterDetailScreen(
          modifier = Modifier.padding(contentPadding),
          characterId = characterId,
          onBack = { navController.popBackStack() }
        )
      }
    }
  }
}