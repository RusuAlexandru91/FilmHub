package com.example.andoid.filmhub;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Discover Settings");
    }

    public static class FilmHubPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            int key = getActivity().getIntent().getExtras().getInt("categoryDiscoverKey",-1);

            if( key == 0){
                addPreferencesFromResource(R.xml.settings_movie);

                Preference numberMovies = findPreference(getString(R.string.settings_min_number_key_movies));
                bindPreferenceSummaryToValue(numberMovies);

                Preference orderByMovies = findPreference(getString(R.string.settings_order_by_key_movies));
                bindPreferenceSummaryToValue(orderByMovies);

                Preference categoryMovies = findPreference(getString(R.string.settings_category_by_key_movies));
                bindPreferenceSummaryToValue(categoryMovies);

            }else{
                addPreferencesFromResource(R.xml.settings_shows);

                Preference numberShows = findPreference(getString(R.string.settings_min_number_key_shows));
                bindPreferenceSummaryToValue(numberShows);

                Preference orderByShows = findPreference(getString(R.string.settings_order_by_key_shows));
                bindPreferenceSummaryToValue(orderByShows);

                Preference categoryShows = findPreference(getString(R.string.settings_category_by_key_shows));
                bindPreferenceSummaryToValue(categoryShows);
            }
        }
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
                preference.setOnPreferenceChangeListener(this);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
                String preferenceString = preferences.getString(preference.getKey(), "");
                onPreferenceChange(preference, preferenceString);
        }
    }
}