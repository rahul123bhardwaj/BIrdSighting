package com.example.birdsighting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

	Button buttonSearch;
	EditText editTextZipCode;
	TextView textViewResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		buttonSearch = findViewById(R.id.buttonSearch);
		buttonSearch.setOnClickListener(this);

		editTextZipCode = findViewById(R.id.editTextZipCode);
		textViewResult = findViewById(R.id.textViewResult);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {

		if (item.getItemId() == R.id.itemSearch) {
			Intent intent = new Intent(this, SearchActivity.class);
			startActivity(intent);
		}

		if (item.getItemId() == R.id.itemReport) {
			Intent intent = new Intent(this, ReportActivity.class);
			startActivity(intent);
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference myRef = database.getReference("BirdSightings");

		String zipCode = editTextZipCode.getText().toString();
		myRef.orderByChild("zipCode").equalTo(zipCode).addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				BirdSighting foundSighting = dataSnapshot.getValue(BirdSighting.class);
				String birdName = foundSighting.birdName;
				String reporter = foundSighting.reporter;

				textViewResult.setText("Most recent spotting:\n" + birdName + "\t"+ "by " + reporter);
			}

			@Override
			public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

			}

			@Override
			public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

			}

			@Override
			public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});

	}
}
