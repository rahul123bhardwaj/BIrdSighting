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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

	EditText editTextBirdName, editTextZipCode;
	Button buttonReport;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);

		buttonReport = findViewById(R.id.buttonReport);

		editTextBirdName = findViewById(R.id.editTextBirdName);
		editTextZipCode = findViewById(R.id.editTextZipCode);

		buttonReport.setOnClickListener(this);

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

		String birdName = editTextBirdName.getText().toString();
		String zipCode = editTextZipCode.getText().toString();
		String reporterEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail(); //extracting current user's email

		//Creating a bird sighting object with default importance 0
		BirdSighting newSighting = new BirdSighting(birdName, zipCode, reporterEmail, 0);
		myRef.push().setValue(newSighting); //inserting Sighting in the database
		Toast.makeText(ReportActivity.this, "Reported Successfully!", Toast.LENGTH_SHORT).show();
	}
}
