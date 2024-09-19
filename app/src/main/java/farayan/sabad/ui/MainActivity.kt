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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import farayan.sabad.R
import farayan.sabad.ui.composable.CategoriesCategoriesComposable
import farayan.sabad.ui.composable.InvoicesInvoicesComposable
import farayan.sabad.utility.appFont
import farayan.sabad.vm.CategoriesViewModel
import farayan.sabad.vm.HomepageViewModel
import farayan.sabad.vm.InvoicesViewModel

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val homepageViewModel = HomepageViewModel.Factory.create(HomepageViewModel::class.java)
        val categoriesViewModel = CategoriesViewModel.Factory.create(CategoriesViewModel::class.java)
        val invoicesViewModel = InvoicesViewModel.Factory.create(InvoicesViewModel::class.java)
        setContent {
            CompositionLocalProvider(
                LocalLifecycleOwner provides this@MainActivity,
                LocalLayoutDirection provides LayoutDirection.Rtl,
                LocalContentAlpha provides ContentAlpha.medium
            ) {
                val actionIcons = homepageViewModel.actionsReadOnly.collectAsStateWithLifecycle()
                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = stringResource(id = R.string.home_title), fontFamily = appFont, textAlign = TextAlign.Right) },
                            navigationIcon = {},
                            actions = {
                                val iconSize = 48.dp
                                val iconPadding = 12.dp
                                for (actionIcon in actionIcons.value.sortedBy { it.position }) {
                                    Icon(
                                        actionIcon.icon,
                                        contentDescription = actionIcon.name,
                                        modifier = Modifier
                                            .clickable { actionIcon.onClick(navController) }
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

                    NavHost(navController = navController, startDestination = "/home") {
                        composable("/home") { CategoriesCategoriesComposable(categoriesViewModel, { homepageViewModel.changeActions(it) }, navController) }
                        composable("/orders") { InvoicesInvoicesComposable(invoicesViewModel) }
                    }
                }
            }
        }
    }
}
