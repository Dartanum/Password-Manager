package ru.dartanum.passwordmanager.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import ru.dartanum.passwordmanager.R;
import ru.dartanum.passwordmanager.databinding.FragmentPasswordListBinding;
import ru.dartanum.passwordmanager.db.DbManager;
import ru.dartanum.passwordmanager.domain.Record;

import java.util.Timer;
import java.util.TimerTask;

public class PasswordListFragment extends Fragment {
    private FragmentPasswordListBinding binding;
    private int backPressedCount = 0;
    private final Timer backButtonTimer = new Timer();
    private DbManager db;

    private String[] names = {"Иван", "Марья", "Петр", "Антон", "Даша", "Борис",
        "Костя", "Игорь", "Анна", "Денис", "Андрей"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DbManager(getActivity());
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                backPressedCount++;
                Log.d("myLog", String.valueOf(backPressedCount));
                if (NavHostFragment.findNavController(PasswordListFragment.this).getPreviousBackStackEntry() == null) {
                    if (backPressedCount < 2) {
                        backButtonTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                backPressedCount = 0;
                            }
                        }, 1000);
                        Toast.makeText(requireActivity(), "Для выхода нажмите дважды", Toast.LENGTH_SHORT).show();
                    } else {
                        requireActivity().finishAndRemoveTask();
                    }
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPasswordListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] recordNames = db.getRecords().stream().map(Record::getName).toArray(String[]::new);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.password_list_item, recordNames);
        binding.pswdList.setAdapter(adapter);

        binding.pswdList.setOnItemClickListener((parent, view1, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("data", ((TextView) view1).getText().toString());
            NavHostFragment.findNavController(PasswordListFragment.this)
                .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
        });

        binding.createPswdButton.setOnClickListener((view1) -> {
            NavHostFragment.findNavController(PasswordListFragment.this)
                .navigate(R.id.action_FirstFragment_to_CreatePasswordFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}