package test.project.eva.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import test.project.eva.R
import test.project.eva.databinding.ActivityMainBinding
import test.project.eva.managers.PermissionsManager

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setTransparentStatusBar()
        requestPermissions()
    }

    private fun requestPermissions() {
        PermissionsManager(this).requestPermissions()
    }

    private fun setTransparentStatusBar() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = ContextCompat.getColor(this, R.color.dark_transparent_background)
    }
}