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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{

	Button buttonSearch, buttonImportance;
	EditText editTextZipCode;
	TextView textViewResult;
	String objectKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		buttonSearch = findViewById(R.id.buttonSearch);
		buttonSearch.setOnClickListener(this);

		buttonImportance = findViewById(R.id.buttonImportance);
		buttonImportance.setOnClickListener(this);
		buttonImportance.setVisibility(View.INVISIBLE);

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
			this.finish();
		}

		if (item.getItemId() == R.id.itemReport) {
			Intent intent = new Intent(this, ReportActivity.class);
			startActivity(intent);
			this.finish();
		}

		if (item.getItemId() == R.id.itemHighestImportance) {
			Intent intent = new Intent(this, ImportanceActivity.class);
			startActivity(intent);
			this.finish();
		}

		if (item.getItemId() == R.id.itemLogout) {
			FirebaseAuth.getInstance().signOut();
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			this.finish();
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		final DatabaseReference myRef = database.getReference("BirdSightings");
		final String zipCode = editTextZipCode.getText().toString();
		final View view = v;
		if (zipCode.isEmpty()) {
			//Showing error message when zipcode is empty and not doing any processing
			Toast.makeText(SearchActivity.this, "Zip Code is Empty!", Toast.LENGTH_SHORT).show();
		}
		else {
			myRef.orderByChild("zipCode").equalTo(zipCode).limitToLast(1).addChildEventListener(new ChildEventListener() {
				@Override
				public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
					BirdSighting foundSighting = dataSnapshot.getValue(BirdSighting.class);
					Integer currentImportance = dataSnapshot.child("importance").getValue(Integer.class); //Fetching importance of last bird sighting of this zip code
					objectKey = dataSnapshot.getKey();
					String birdName = foundSighting.birdName;
					String reporter = foundSighting.reporterEmail;

					//Displaying result when Search button is pressed
					if (view == buttonSearch) {
						textViewResult.setText("Most recent sighting at " + zipCode + ":\n" + birdName + "\t" + " by " + reporter);
						buttonImportance.setVisibility(View.VISIBLE);
					}
					//Increasing the importance by 1 and showing a message to user
					else if (view == buttonImportance) {
						textViewResult.setText("Most recent sighting at " + zipCode + ":\n" + birdName + "\t" + " by " + reporter);
						myRef.child(objectKey).child("importance").setValue(currentImportance + 1);
						Toast.makeText(SearchActivity.this, "This sighting's importance increased by 1", Toast.LENGTH_SHORT).show();
					}
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
}
