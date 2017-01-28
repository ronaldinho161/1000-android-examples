package eu.aplusteam.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import eu.aplusteam.retrofit.data.model.Item;
import eu.aplusteam.retrofit.data.model.SOAnswersResponse;
import eu.aplusteam.retrofit.data.remote.ApiUtils;
import eu.aplusteam.retrofit.data.remote.SOService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AnswersAdapter adapter;
    private RecyclerView recyclerView;
    private SOService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        service = ApiUtils.getSOService();
        recyclerView = (RecyclerView) findViewById(R.id.rv_answers);
        adapter = new AnswersAdapter(this, new ArrayList<Item>(0),
                                        new AnswersAdapter.PostItemListener() {

                                            @Override
                                            public void onPostClick(long id) {
                                                Toast.makeText(MainActivity.this,
                                                                "Post id is" + id,
                                                                    Toast.LENGTH_SHORT
                                                        ).show();
                                            }
                                        }
                                    );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        loadAnswers();
    }

    public void loadAnswers() {
        service.getAnswers().enqueue(new Callback<SOAnswersResponse>() {
            @Override
            public void onResponse(Call<SOAnswersResponse> call, Response<SOAnswersResponse> response) {
                if (response.isSuccessful()) {
                    adapter.updateAnswers(response.body().getItems());
                    Log.d("MainActivity", "posts loaded from API");
                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<SOAnswersResponse> call, Throwable t) {
                showErrorMessage();
                Log.d("MainActivity", "error loading from API");
            }
        });
    }

    public void showErrorMessage() {
        Toast.makeText(this, "Error loading posts", Toast.LENGTH_SHORT).show();
    }

}
