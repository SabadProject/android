package farayan.sabad.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import farayan.sabad.R
import farayan.sabad.ui.composable.CategoriesCategoriesComposable
import farayan.sabad.ui.composable.OrdersOrdersComposable
import farayan.sabad.vm.CategoriesViewModel
import farayan.sabad.utility.appFont
import farayan.sabad.vm.HomepageViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val categoriesViewModel = CategoriesViewModel.Factory.create(CategoriesViewModel::class.java)
        val homepageViewModel = HomepageViewModel.Factory.create(HomepageViewModel::class.java)
        setContent {
            CompositionLocalProvider(
                LocalLifecycleOwner provides this@MainActivity,
                LocalLayoutDirection provides LayoutDirection.Rtl,
                LocalContentAlpha provides ContentAlpha.medium
            ) {
                val actionIcons = homepageViewModel.actionsReadOnly.collectAsStateWithLifecycle()

                Scaffold(
                    topBar = {
                        /*Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colors.primarySurface)
                                .height(48.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1.0f)
                                    .padding(8.dp, 4.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = stringResource(id = R.string.home_title), color = MaterialTheme.colors.onPrimary, fontFamily = appFont)
                            }
                            Row(
                                modifier = Modifier
                                    .padding(8.dp, 0.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val iconSize = 48.dp
                                val iconPadding = 12.dp
                                if (selectedCategories.isNotEmpty()) {
                                    if (selectedCategories.size == 1) {
                                        Icon(
                                            Icons.Filled.Edit, contentDescription = "edit", modifier = Modifier
                                                .clickable { editingCategory = selectedCategories.first() }
                                                .width(iconSize)
                                                .height(iconSize)
                                                .padding(iconPadding),
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Icon(
                                        Icons.Filled.Delete, contentDescription = "delete", modifier = Modifier
                                            .width(iconSize)
                                            .height(iconSize)
                                            .padding(0.dp)
                                            .clickable { removingCategories = selectedCategories }
                                            .padding(iconPadding),
                                        tint = Color.White
                                    )
                                } else {
                                    if (items.any()) {
                                        Icon(
                                            Icons.Filled.ShoppingCart,
                                            contentDescription = "checkout",
                                            modifier = Modifier
                                                .width(iconSize)
                                                .height(iconSize)
                                                .padding(0.dp)
                                                .clickable { categoriesViewModel.checkout() }
                                                .padding(iconPadding),
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                        }*/
                        TopAppBar(
                            title = { Text(text = stringResource(id = R.string.home_title), fontFamily = appFont) },
                            navigationIcon = {},
                            actions = {
                                val iconSize = 48.dp
                                val iconPadding = 12.dp
                                for (actionIcon in actionIcons.value.sortedBy { it.position }){
                                    Icon(
                                        actionIcon.icon,
                                        contentDescription = actionIcon.name,
                                        modifier = Modifier
                                            .clickable { actionIcon.onClick() }
                                            .width(iconSize)
                                            .height(iconSize)
                                            .padding(iconPadding),
                                        tint = Color.White
                                    )
                                }
                            },
                        )
                    },
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "/home") {
                        composable("/home") { CategoriesCategoriesComposable(categoriesViewModel) }
                        composable("/orders") { OrdersOrdersComposable() }
                    }
                }
            }
        }
    }
}
