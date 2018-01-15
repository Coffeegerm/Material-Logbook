/*
 * Copyright 2018 Coffee and Cream Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.coffeegerm.materiallogbook.ui.statistics.children;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.coffeegerm.materiallogbook.MaterialLogbookApplication;
import io.github.coffeegerm.materiallogbook.R;
import io.github.coffeegerm.materiallogbook.data.model.EntryItem;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by David Yarzebinski on 7/29/2017.
 * <p>
 * Fragment used with Statistics ViewPager to show
 * the all amount of statistics
 */

public class AllStatisticsFragment extends Fragment {
  
  @Inject
  public SharedPreferences sharedPreferences;
  
  @BindView(R.id.all_days_statistics_average)
  TextView averageBloodGlucose;
  @BindView(R.id.highest_of_all_glucose)
  TextView highestBloodGlucose;
  @BindView(R.id.lowest_of_all_glucose)
  TextView lowestBloodGlucose;
  @BindView(R.id.imgAvg)
  ImageView ivAvg;
  @BindView(R.id.imgUpArrow)
  ImageView ivUpArrow;
  @BindView(R.id.imgDownArrow)
  ImageView ivDownArrow;
  private Realm realm;
  
  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    MaterialLogbookApplication.syringe.inject(this);
  }
  
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View allStatsView = inflater.inflate(R.layout.fragment_all_stats, container, false);
    ButterKnife.bind(this, allStatsView);
    realm = Realm.getDefaultInstance();
    setValues();
    setImages();
    return allStatsView;
  }
  
  public void setValues() {
    RealmResults<EntryItem> entryItems = realm.where(EntryItem.class).greaterThan("bloodGlucose", 0).findAll();
    if (entryItems.size() == 0) {
      averageBloodGlucose.setText(R.string.dash);
      highestBloodGlucose.setText(R.string.dash);
      lowestBloodGlucose.setText(R.string.dash);
    } else {
      averageBloodGlucose.setText(String.valueOf(getAverage()));
      highestBloodGlucose.setText(String.valueOf(getHighestBloodGlucose()));
      lowestBloodGlucose.setText(String.valueOf(getLowestBloodGlucose()));
    }
  }
  
  public int getAverage() {
    int averageCalculated = 0;
    RealmResults<EntryItem> entryItems = realm.where(EntryItem.class).greaterThan("bloodGlucose", 0).findAll();
    if (entryItems.size() == 0) {
      Toast.makeText(getContext(), "Unable to show data at this time.", Toast.LENGTH_SHORT).show();
    } else {
      int total = 0;
      for (int position = 0; position < entryItems.size(); position++) {
        EntryItem item = entryItems.get(position);
        assert item != null;
        total += item.getBloodGlucose();
      }
      averageCalculated = total / entryItems.size();
    }
    return averageCalculated;
  }
  
  public int getHighestBloodGlucose() {
    int highest = 0;
    RealmResults<EntryItem> entryItems = realm.where(EntryItem.class).greaterThan("bloodGlucose", 0).findAll();
    for (int position = 0; position < entryItems.size(); position++) {
      EntryItem item = entryItems.get(position);
      assert item != null;
      if (item.getBloodGlucose() > highest) {
        highest = item.getBloodGlucose();
      }
    }
    return highest;
  }
  
  
  public int getLowestBloodGlucose() {
    int lowest = 1000;
    RealmResults<EntryItem> entryItems = realm.where(EntryItem.class).greaterThan("bloodGlucose", 0).findAll();
    for (int position = 0; position < entryItems.size(); position++) {
      EntryItem item = entryItems.get(position);
      assert item != null;
      if (item.getBloodGlucose() < lowest) {
        lowest = item.getBloodGlucose();
      }
    }
    return lowest;
  }
  
  private void setImages() {
    if (sharedPreferences.getBoolean("pref_dark_mode", false)) {
      ivAvg.setImageResource(R.drawable.ic_average_dark);
      ivUpArrow.setImageResource(R.drawable.ic_up_arrow_dark);
      ivDownArrow.setImageResource(R.drawable.ic_down_arrow_dark);
    }
  }
}