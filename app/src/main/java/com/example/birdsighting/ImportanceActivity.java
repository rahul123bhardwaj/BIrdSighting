package com.example.birdsighting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ImportanceActivity extends AppCompatActivity {

	TextView textViewHighestImportance;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_importance);

		textViewHighestImportance = findViewById(R.id.textViewHighestImportance);

		FirebaseDatabase database = FirebaseDatabase.getInstance();
		final DatabaseReference myRef = database.getReference("BirdSightings");

		myRef.orderByChild("importance").limitToLast(1).addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				BirdSighting foundSighting = dataSnapshot.getValue(BirdSighting.class);
				String birdName = foundSighting.birdName;
				String reporter = foundSighting.reporterEmail;
				String zipCode = foundSighting.zipCode;
				Integer importance = foundSighting.importance;
				textViewHighestImportance.setText("Most important sighting:\n" + birdName + " by " +reporter + " at " + zipCode + "\n" + "Importance: " + importance.toString());
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
}
