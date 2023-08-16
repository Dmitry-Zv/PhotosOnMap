package by.zharikov.photosonmap.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.zharikov.photosonmap.R
import by.zharikov.photosonmap.databinding.ActivityMainBinding
import by.zharikov.photosonmap.databinding.AppBarMainBinding
import by.zharikov.photosonmap.databinding.NavHeaderMainBinding
import by.zharikov.photosonmap.domain.model.User
import by.zharikov.photosonmap.presentation.authorization.AuthorizationActivity
import by.zharikov.photosonmap.utils.Constants.DATA_KEY
import by.zharikov.photosonmap.utils.showAlert
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val sharedViewModel: SharedViewModel by viewModels()
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(DATA_KEY, User.Data::class.java)
        } else {
            intent.getParcelableExtra(DATA_KEY)
        }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)

        setUpUi(data)



        data?.let {
            sharedViewModel.setUser(it.token)
        }


    }

    private fun setUpUi(user: User.Data?) {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        val navHeaderMainBinding = NavHeaderMainBinding.inflate(layoutInflater)
        navHeaderMainBinding.textView.text = user?.login
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navView.addHeaderView(navHeaderMainBinding.root)
        val navController = navHostFragment.findNavController()
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_photos, R.id.navigation_map
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in arrayOf(R.id.detailFragment, R.id.cameraFragment)) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
        }
        toolBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.sign_out -> {
                    performAlert()
                    true
                }
                else -> false
            }
        }
    }

    private fun performAlert() {

        showAlert(
            title = R.string.sign_out,
            message = R.string.approve_sign_out,
            positiveButtonFun = {
                viewModel.signOut()
                startActivity(Intent(this, AuthorizationActivity::class.java))
            },
            negativeButtonFun = {}
        )

    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}