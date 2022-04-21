package ru.dartanum.passwordmanager.activity;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import ru.dartanum.passwordmanager.R;
import ru.dartanum.passwordmanager.databinding.ActivityPasswordListBinding;
import ru.dartanum.passwordmanager.db.DbManager;
import ru.dartanum.passwordmanager.domain.Category;

public class PasswordListActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private ActivityPasswordListBinding binding;
    public DbManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbManager = new DbManager(this);
        binding = ActivityPasswordListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_password_list);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_password_list);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
            || super.onSupportNavigateUp();
    }
}