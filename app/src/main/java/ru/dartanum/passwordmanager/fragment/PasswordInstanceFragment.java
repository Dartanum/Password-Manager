package ru.dartanum.passwordmanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import ru.dartanum.passwordmanager.R;
import ru.dartanum.passwordmanager.databinding.FragmentPasswordInstanceBinding;

public class PasswordInstanceFragment extends Fragment {
    private FragmentPasswordInstanceBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPasswordInstanceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.textviewSecond.setText(getArguments().getString("data"));
        binding.buttonSecond.setOnClickListener(view1 -> NavHostFragment
            .findNavController(this)
            .navigate(R.id.action_SecondFragment_to_FirstFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}