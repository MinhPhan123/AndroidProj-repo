package rmit.ad.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.Locale;

public class ItemList extends AppCompatActivity {

    private String categoryName;
    RecyclerView recycler_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        categoryName = getIntent().getExtras().get("category").toString();
        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOption<>
    }
}