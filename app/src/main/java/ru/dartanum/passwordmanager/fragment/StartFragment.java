package ru.dartanum.passwordmanager.fragment;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import ru.dartanum.passwordmanager.R;
import ru.dartanum.passwordmanager.databinding.FragmentStartBinding;
import ru.dartanum.passwordmanager.service.LocalStorageService;

import static android.content.Context.MODE_PRIVATE;

public class StartFragment extends Fragment {
    private FragmentStartBinding binding;
    private LocalStorageService localStorageService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        localStorageService = LocalStorageService.getInstance(getActivity().getSharedPreferences(LocalStorageService.name, MODE_PRIVATE));
        binding = FragmentStartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController controller = NavHostFragment.findNavController(this);

        if (!localStorageService.isMasterPasswordExist()) {
            Log.d("myLog", "Мастер-пароля не существует");
            localStorageService.createMasterPassword("1234");
        } else {
            binding.enterMasterPswdBtn.setOnClickListener(view1 -> {
                if (localStorageService.checkMasterPassword(binding.masterPswdEt.getText().toString())) {
                    controller.navigate(StartFragmentDirections.actionStartFragmentToPasswordListFragment());
                }
            });
        }
    }
}