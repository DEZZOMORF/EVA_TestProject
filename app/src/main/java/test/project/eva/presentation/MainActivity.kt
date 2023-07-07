package test.project.eva.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

        setUpUI()
        requestPermissions()
    }

    private fun requestPermissions() {
        PermissionsManager(this).requestPermissions()
    }

    private fun setUpUI() {
        val backgroundColor = ContextCompat.getColor(this, R.color.background)
        window.statusBarColor = backgroundColor
        binding.root.setBackgroundColor(backgroundColor)
    }
}