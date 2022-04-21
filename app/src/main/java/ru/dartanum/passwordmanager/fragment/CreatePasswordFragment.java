package ru.dartanum.passwordmanager.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import ru.dartanum.passwordmanager.R;
import ru.dartanum.passwordmanager.databinding.FragmentCreatePasswordBinding;
import ru.dartanum.passwordmanager.db.DbManager;
import ru.dartanum.passwordmanager.domain.Category;
import ru.dartanum.passwordmanager.domain.Field;
import ru.dartanum.passwordmanager.domain.Record;
import ru.dartanum.passwordmanager.service.LocalStorageService;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class CreatePasswordFragment extends Fragment {
    private FragmentCreatePasswordBinding binding;
    private DbManager db;
    private int selectedCategoryIndex = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = new DbManager(getActivity());
        binding = FragmentCreatePasswordBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Category> categories = db.getCategories();
        String[] categoryNames = categories.stream().map(Category::getName).toArray(String[]::new);

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, categoryNames);
        categoriesAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        binding.categorySpinnerFcp.setAdapter(categoriesAdapter);
        binding.categorySpinnerFcp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedCategoryIndex = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.createPswdButton.setOnClickListener(view1 -> {
            Record record = new Record();
            record.setName(binding.nameEtFcp.getText().toString());
            record.setCategory(categories.get(selectedCategoryIndex));
            List<Pair<Field, String>> values = new ArrayList<>();
            record.setValuesByField(values);
            db.saveRecord(record);
            NavHostFragment.findNavController(this).popBackStack();
        });
    }
}
