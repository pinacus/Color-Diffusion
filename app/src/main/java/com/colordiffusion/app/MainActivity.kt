package com.colordiffusion.app

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.colordiffusion.app.ui.FavoritesScreen
import com.colordiffusion.app.ui.HomeScreen
import com.colordiffusion.app.ui.theme.ColorDiffusionTheme
import com.colordiffusion.app.util.writePaletteImage
import com.colordiffusion.app.viewmodel.PaletteViewModel
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { ColorDiffusionApp() }
    }
}

@androidx.compose.runtime.Composable
private fun ColorDiffusionApp(viewModel: PaletteViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(0) }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    val snackbar = remember { SnackbarHostState() }
    val gallery = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { viewModel.extractFromUri(it) }
    }
    val camera = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { captured ->
        if (captured) cameraUri?.let { viewModel.extractFromUri(it) }
    }
    val cameraPermission = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) cameraUri?.let(camera::launch)
    }
    ColorDiffusionTheme {
        Scaffold(snackbarHost = { SnackbarHost(snackbar) }, bottomBar = {
            NavigationBar(Modifier.navigationBarsPadding()) {
                NavigationBarItem(selectedTab == 0, { selectedTab = 0 }, icon = { Icon(Icons.Default.Palette, "Palette") }, label = { androidx.compose.material3.Text("Create") })
                NavigationBarItem(selectedTab == 1, { selectedTab = 1 }, icon = { Icon(Icons.Default.Favorite, "Favorites") }, label = { androidx.compose.material3.Text("Favorites") })
            }
        }) { padding ->
            Column(Modifier.fillMaxSize().padding(padding)) {
                if (selectedTab == 0) HomeScreen(viewModel, snackbar, { gallery.launch("image/*") }, {
                    val file = File.createTempFile("palette_", ".jpg", viewModel.getApplication<android.app.Application>().cacheDir)
                    cameraUri = FileProvider.getUriForFile(viewModel.getApplication(), "${viewModel.getApplication<android.app.Application>().packageName}.fileprovider", file)
                    cameraPermission.launch(Manifest.permission.CAMERA)
                }, { text ->
                    val imageFile = File.createTempFile("palette_share_", ".png", viewModel.getApplication<android.app.Application>().cacheDir)
                    writePaletteImage(viewModel.colors.value, imageFile)
                    val imageUri = FileProvider.getUriForFile(viewModel.getApplication(), "${viewModel.getApplication<android.app.Application>().packageName}.fileprovider", imageFile)
                    val send = Intent(Intent.ACTION_SEND).apply {
                        type = "image/png"
                        putExtra(Intent.EXTRA_TEXT, text)
                        putExtra(Intent.EXTRA_STREAM, imageUri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    viewModel.getApplication<android.app.Application>().startActivity(Intent.createChooser(send, "Share palette").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }) else FavoritesScreen(viewModel)
            }
        }
    }
}

private fun PaletteViewModel.extractFromUri(uri: Uri) {
    getApplication<android.app.Application>().contentResolver.openInputStream(uri)?.use { stream ->
        BitmapFactory.decodeStream(stream)?.let(::extract)
    }
}
