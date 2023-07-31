package com.example.distantdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button btn;
    EditText edit;
    RecyclerView listTasks;
    MainAdapter adapter = null;
    String url = "http://127.0.0.1/calender/";
    List<String> tasks = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listTasks = findViewById(R.id.recycler_view);
        btn = findViewById(R.id.addButton);
        edit = findViewById(R.id.editTask);

        //Initialiser linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);

        // Set layout manager
        listTasks.setLayoutManager(linearLayoutManager);


        //Création  d'instance de la classe Retrofit pour envoyer des requêtes HTTP et gérer les réponses.
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();

        // Création d'nstance de l'interface myapi à l'aide de Retrofit pour appeler les services Web de l'API.
        myapi api = retrofit.create(myapi.class);

        // L'objet Call<List<Task>> est utilisé pour exécuter la requête et recevoir la réponse, qui sera une liste d'objets Task.
        Call<List<Task>> call = api.getAllTasks();

        // L'exécution de la requête de manière asynchrone en utilisant enqueue(), la requête sera effectuée en arrière-plan sans bloquer le thread principal.
        loadData(call);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                process();
            }
        });


    }

    private void loadData(Call<List<Task>> call) {
        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                List<Task> data = response.body();

                adapter = new MainAdapter(Listin(data), MainActivity.this);
                listTasks.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {

            }
        });
    }

    public void process() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();

        myapi api = retrofit.create(myapi.class);
        final String task = edit.getText().toString();
        Call<Task> call = api.addTask(task);
        call.enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {

                edit.setText("");

                Log.i("retrofit answer", response.toString());
                Toast.makeText(getApplicationContext(), "Successfully inserted", Toast.LENGTH_LONG).show();
                int id = Integer.parseInt(tasks.get(tasks.size() - 1).split(":")[0]);
                tasks.add(String.valueOf(id + 1) + ": " + task);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {

            }
        });


    }

    List<String> Listin(List<Task> l) {
        for (int i = 0; i < l.size(); i++) {
            tasks.add(l.get(i).getId() + ": " + l.get(i).getTask());
        }
        return tasks;
    }
}





